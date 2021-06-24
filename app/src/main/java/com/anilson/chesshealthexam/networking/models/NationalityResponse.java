package com.anilson.chesshealthexam.networking.models;

import java.util.List;

public class NationalityResponse {

    private String name;
    private List<CountryResponse> country;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CountryResponse> getCountry() {
        return country;
    }

    public void setCountry(List<CountryResponse> country) {
        this.country = country;
    }
}
