package com.example.FishBot.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Data
@Getter
@Setter
//
//"Рыбодром № 2", "Место для спортивной ловли различных" +
//        " видов рыб, на месте находятся бетонные плиты, рекомендуется иметь соответсвующие подставки",
//        "51.619266, 39.217416"));

public class FishingPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String coordinates;


}
