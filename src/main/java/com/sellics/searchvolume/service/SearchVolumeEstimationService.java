package com.sellics.searchvolume.service;

import com.sellics.searchvolume.client.AmazonClient;
import com.sellics.searchvolume.web.dto.AmazonSuggestions;
import com.sellics.searchvolume.web.dto.SearchVolumeEstimate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class SearchVolumeEstimationService
{
    private static final int MAX_TIME_SECONDS = 10;

    private final AmazonClient amazonClient;

    @Autowired
    public SearchVolumeEstimationService(AmazonClient amazonClient)
    {
        this.amazonClient = amazonClient;
    }

    /**
     * Make multiple calls to the Amazon API to estimate the traffic volume for a given keyword
     * This method also keeps a track of time and will abort after 10 seconds
     *
     * @param keyword the word for which the traffic volume needs to be estimated
     *
     * @return An estimated search volume
     */
    public SearchVolumeEstimate estimateVolume(String keyword)
    {
        Instant end = Instant.now().plus(MAX_TIME_SECONDS, ChronoUnit.SECONDS);

        for (int i = 0; i < keyword.length(); i++) {

            if (Instant.now().isBefore(end)) {

                String textToSearch = keyword.substring(0, i + 1);
                AmazonSuggestions amazonSuggestions = amazonClient.getSearchSuggestions(textToSearch);

                if (containsKeyword(keyword, amazonSuggestions)) {
                    double score = computeScore(keyword.length(), i);
                    return new SearchVolumeEstimate(keyword, score);
                }
            }
        }

        return new SearchVolumeEstimate(keyword, 0);
    }

    /**
     * Score is computed as a percentage function of the length of the keyword and the number of requests made to Amazon to find
     * keyword. If keyword is found in the first request made, we want it to be 100 and if its not found we want it to be 0.
     *
     * @param lengthOfKeyword the length of the keyword we are looking for
     * @param requestsMade    number of requests that were made to amazon to find
     *
     * @return score
     */
    private double computeScore(int lengthOfKeyword, int requestsMade)
    {
        return 100 - (requestsMade * 100.0 / lengthOfKeyword);
    }

    private boolean containsKeyword(String keyword, AmazonSuggestions suggestions)
    {
        return suggestions.suggestions.stream()
            .map(s -> s.value)
            .anyMatch(t -> t.equalsIgnoreCase(keyword));
    }
}
