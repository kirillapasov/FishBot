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
        fishingPlaces.add(new FishingPlace("Река Дон", "Популярное место для рыбалки на леща и судака.",
                "51.6551, 39.1606"));
        fishingPlaces.add(new FishingPlace("Рыбодром № 2", "Место для спортивной ловли различных" +
                " видов рыб, на месте находятся бетонные плиты, рекомендуется иметь соответсвующие подставки",
                "51.619266, 39.217416"));
        fishingPlaces.add(new FishingPlace("Река Воронеж (За градом)", "Тихое и спокойное место для" +
                " семейного отдыха, людей мало, всегда хороший клёв, главное проехать" +
                " местности.", "51.742382, 39.227490"));
        fishingPlaces.add(new FishingPlace("Нововоронежский охладитель", "Рыбалка на телапию рядом с атомной станцией.",
                "51.311811, 39.204135"));
        fishingPlaces.add(new FishingPlace("Воронежский водосброс", "Рыбалка на карася, плотву и леща на водосбросе воронежской плотины.", "51.537365, 39.136901"));
        fishingPlaces.add(new FishingPlace("Река Хава (Рождественская Хава пески)", "На  месте расположен ужобный мостик для ловли Леща, всегда хороший улов и мало людей", "51.664014, 39.721194"));
        //Todo добавить другие места, интегрировать БД для удобного хранения, сделать DTO
    }

    public FishingPlace getRandomFishingPlace() {
        int index = random.nextInt(fishingPlaces.size());
        return fishingPlaces.get(index);
    }
}

