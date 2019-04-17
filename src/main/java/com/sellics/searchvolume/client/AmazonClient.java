package com.sellics.searchvolume.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sellics.searchvolume.configuration.AmazonSettings;
import com.sellics.searchvolume.web.dto.AmazonSuggestions;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class AmazonClient
{
    private static final Logger LOGGER = getLogger(AmazonClient.class);

    private static final String MERCHANT_ID = "ATVPDKIKX0DER";

    private static final String ALIAS = "aps";

    private final ObjectMapper objectMapper;

    private final OkHttpClient client;

    private final AmazonSettings amazonSettings;

    @Autowired
    public AmazonClient(ObjectMapper objectMapper, AmazonSettings amazonSettings)
    {
        this.objectMapper = objectMapper;
        this.amazonSettings = amazonSettings;
        client = new OkHttpClient();
    }

    public AmazonSuggestions getSearchSuggestions(String keyword)
    {
        HttpUrl url = new HttpUrl.Builder()
            .scheme("https")
            .host(amazonSettings.host)
            .addPathSegments(amazonSettings.api)
            .addQueryParameter("mid", MERCHANT_ID)
            .addQueryParameter("alias", ALIAS)
            .addQueryParameter("prefix", keyword)
            .build();

        Request request = new Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .get()
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                return objectMapper.readValue(response.body().string(), AmazonSuggestions.class);
            }
        } catch (Exception e) {
            LOGGER.error("Error getting suggestions from Amazon client", e);
        }
        return null;
    }
}
