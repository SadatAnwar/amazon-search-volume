package com.sellics.searchvolume.web.dto;

public class SearchVolumeEstimate
{
    private String keyword;

    private double score;

    public SearchVolumeEstimate(String keyword, double score)
    {
        this.keyword = keyword;
        this.score = score;
    }

    public String getKeyword()
    {
        return keyword;
    }

    public double getScore()
    {
        return score;
    }
}
