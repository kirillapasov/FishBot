package com.example.FishBot.repo;

import com.example.FishBot.model.FishingPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FishingPlaceRepository extends JpaRepository<FishingPlace, Long> {
}
