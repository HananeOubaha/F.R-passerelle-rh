package com.passerellerh.config;

import com.passerellerh.entity.Badge;
import com.passerellerh.repository.BadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final BadgeRepository badgeRepository;

    @Override
    public void run(String... args) throws Exception {
        if (badgeRepository.count() == 0) {
            Badge bronze = new Badge();
            bronze.setNom("Bronze");
            bronze.setSeuilScore(20.0);
            bronze.setIconeUrl("🥉");

            Badge argent = new Badge();
            argent.setNom("Argent");
            argent.setSeuilScore(50.0);
            argent.setIconeUrl("🥈");

            Badge or = new Badge();
            or.setNom("Or");
            or.setSeuilScore(80.0);
            or.setIconeUrl("🥇");

            badgeRepository.saveAll(List.of(bronze, argent, or));
            System.out.println(" Badges par défaut initialisés.");
        }
    }
}
