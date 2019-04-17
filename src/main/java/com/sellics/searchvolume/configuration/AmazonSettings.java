package com.sellics.searchvolume.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("amazon")
public class AmazonSettings
{
    public String host;

    public String api;

    public void setApi(String api)
    {
        this.api = api;
    }

    public void setHost(String host)
    {
        this.host = host;
    }
}
