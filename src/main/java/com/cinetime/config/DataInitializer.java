package com.cinetime.config;

import com.cinetime.entity.*;
import com.cinetime.entity.enums.*;
import com.cinetime.repository.*;
import com.cinetime.service.TmdbService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final CityRepository cityRepository;
    private final CinemaRepository cinemaRepository;
    private final HallRepository hallRepository;
    private final ShowtimeRepository showtimeRepository;
    private final PasswordEncoder passwordEncoder;
    private final TmdbService tmdbService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 DataInitializer çalışıyor...");

        // 1. KULLANICI YOKSA EKLE
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .name("Admin")
                    .surname("User")
                    .email("admin@cinetime.com")
                    .password(passwordEncoder.encode("123456"))
                    .phoneNumber("5551112233")
                    .roleType(RoleType.ADMIN)
                    .birthDate(LocalDate.of(1990, 1, 1))
                    .gender(Gender.NO_DISCLOSURE)
                    .build();
            userRepository.save(admin);
            System.out.println("✅ Admin kullanıcısı eklendi.");
        }

        // 2. FİLM YOKSA TMDB'DEN ÇEK (Kullanıcı olsa bile burası çalışır!)
        if (movieRepository.count() == 0) {
            System.out.println("🌍 Veritabanında film yok, TMDB servisi başlatılıyor...");

            // Önce altyapı (Şehir/Sinema) var mı kontrol et, yoksa ekle
            if (cityRepository.count() == 0) {
                initializeCinemaStructure();
            }

            // Filmleri Çek
            tmdbService.importMoviesFromTmdb();
        } else {
            System.out.println("📦 Veritabanında zaten filmler var, TMDB pas geçildi.");
        }
    }

    private void initializeCinemaStructure() {
        City istanbul = cityRepository.save(City.builder().name("İstanbul").build());

        Cinema cinema = cinemaRepository.save(Cinema.builder()
                .name("CineTime Marmara")
                .address("Beylikdüzü AVM")
                .city(istanbul)
                .build());

        hallRepository.save(Hall.builder()
                .name("IMAX Salon 1")
                .seatCapacity(100)
                .isSpecial(true)
                .cinema(cinema)
                .build());
        System.out.println("✅ Şehir ve Sinema altyapısı oluşturuldu.");
    }
}