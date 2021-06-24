package com.anilson.chesshealthexam.networking.models;

public class CountryResponse {
    private String countryId;
    private double probability;

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
