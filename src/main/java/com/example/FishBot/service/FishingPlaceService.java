package com.example.FishBot.service;
import com.example.FishBot.model.FishingPlace;
import com.example.FishBot.repo.FishingPlaceRepository;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
//Todo Добавить анотацию сервис
@Service
@Component
public class FishingPlaceService {
    private final FishingPlaceRepository fishingPlaceRepository;
    private final Random random = new Random();

    @Autowired
    public FishingPlaceService(FishingPlaceRepository fishingPlaceRepository) {
        this.fishingPlaceRepository = fishingPlaceRepository;
    }

    public FishingPlace getRandomFishingPlace() {
        List<FishingPlace> allPlaces = fishingPlaceRepository.findAll();
        if (allPlaces.isEmpty()) {
            return null;
        }
        return allPlaces.get(random.nextInt(allPlaces.size()));
    }
}

