package com.example.FishBot.service;
import com.example.FishBot.model.FishingPlace;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
//Todo Добавить анотацию сервис
@Component

public class FishingPlaceService {
    private final List<FishingPlace> fishingPlaces = new ArrayList<>();
    private final Random random = new Random();

    public FishingPlaceService() {
        initializeFishingPlaces();
    }

    private void initializeFishingPlaces() {
       /* fishingPlaces.add(new FishingPlace("Река Дон", "Популярное место для рыбалки на леща и судака.", "51.6551, 39.1606"));
        fishingPlaces.add(new FishingPlace("Озеро Воронежское", "Отличное место для ловли карпа и щуки.", "51.7033, 39.2730"));
        fishingPlaces.add(new FishingPlace("Река Воронеж", "Рыбалка на окуня и плотву в живописной местности.", "51.6700, 39.2100"));*/
        fishingPlaces.add(new FishingPlace("Нововоронежский охладитель", "Рыбалка на телапию рядом с атомной станцией.", "51.311811, 39.204135"));
        fishingPlaces.add(new FishingPlace("Воронежский водосброс", "Рыбалка на карася, плотву и леща на водосбросе воронежской плотины.", "51.537365, 39.136901"));
        //Todo добавить другие места, интегрировать БД для удобного хранения, сделать DTO
    }

    public FishingPlace getRandomFishingPlace() {
        int index = random.nextInt(fishingPlaces.size());
        return fishingPlaces.get(index);
    }
}

