package com.example.FishBot.model;
//Todo добавить аннотацию model или схожее
public class FishingPlace {
    private String name;
    private String description;
    private String coordinates;

    public FishingPlace(String name, String description, String coordinates) {
        this.name = name;
        this.description = description;
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCoordinates() {
        return coordinates;
    }
}
