package com.sellics.searchvolume.service;

import com.sellics.searchvolume.client.AmazonClient;
import com.sellics.searchvolume.web.dto.AmazonSuggestions;
import com.sellics.searchvolume.web.dto.Suggestion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchVolumeEstimationServiceTest
{
    @InjectMocks
    private SearchVolumeEstimationService subject;

    @Mock
    private AmazonClient amazonClient;

    @Test
    public void estimate_volume_should_call_amazon_client_iteratively_till_keyword_is_found()
    {
        when(amazonClient.getSearchSuggestions("a")).thenReturn(makeSuggestions());
        when(amazonClient.getSearchSuggestions("ab")).thenReturn(makeSuggestions("ab"));

        subject.estimateVolume("ab");

        verify(amazonClient, times(2)).getSearchSuggestions(anyString());
    }

    @Test
    public void estimate_should_append_next_char_to_each_consecutive_call()
    {
        String keyword = "iPhone";

        when(amazonClient.getSearchSuggestions(anyString())).thenReturn(makeSuggestions());

        subject.estimateVolume(keyword);

        verify(amazonClient, times(6)).getSearchSuggestions(anyString());
        verify(amazonClient).getSearchSuggestions("i");
        verify(amazonClient).getSearchSuggestions("iP");
        verify(amazonClient).getSearchSuggestions("iPh");
        verify(amazonClient).getSearchSuggestions("iPho");
        verify(amazonClient).getSearchSuggestions("iPhon");
        verify(amazonClient).getSearchSuggestions("iPhone");
    }

    private AmazonSuggestions makeSuggestions(String... keywordSuggestions)
    {
        AmazonSuggestions suggestions = new AmazonSuggestions();
        suggestions.suggestions = Arrays.stream(keywordSuggestions)
            .map(s -> {
                Suggestion sug = new Suggestion();
                sug.value = s;
                return sug;
            }).collect(Collectors.toList());

        return suggestions;
    }
}
