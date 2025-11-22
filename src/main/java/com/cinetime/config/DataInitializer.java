package com.cinetime.config;

import com.cinetime.entity.*;
import com.cinetime.entity.enums.*;
import com.cinetime.repository.*;
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

    @Override
    public void run(String... args) throws Exception {
        // Eğer veritabanında veri varsa tekrar ekleme yapma
        if (userRepository.count() > 0) {
            return;
        }

        System.out.println("🚀 Veritabanı boş, test verileri yükleniyor...");

        // 1. Kullanıcı Oluştur (Şifre: 123456)
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

        // 2. Şehir -> Sinema -> Salon Zinciri
        City istanbul = cityRepository.save(City.builder().name("İstanbul").build());

        Cinema cinema = cinemaRepository.save(Cinema.builder()
                .name("CineTime Marmara")
                .address("Beylikdüzü AVM")
                .city(istanbul)
                .build());

        Hall hall = hallRepository.save(Hall.builder()
                .name("IMAX Salon 1")
                .seatCapacity(100)
                .isSpecial(true)
                .cinema(cinema)
                .build());

        // 3. Film Ekle (Hızlı ve Öfkeli)
        Movie movie = Movie.builder()
                .title("Hızlı ve Öfkeli 10")
                .summary("Dom Toretto ve ailesinin son macerası.")
                .releaseDate(LocalDate.of(2023, 5, 19))
                .duration(141)
                .director("Louis Leterrier")
                .genre("Aksiyon")
                .poster("fastx_poster.jpg") // Resim yolu (String)
                .status(MovieStatus.IN_THEATERS)
                .slug("hizli-ve-ofkeli-10")
                .cast(List.of("Vin Diesel", "Jason Momoa"))
                .formats(List.of("IMAX", "3D"))
                .build();
        movieRepository.save(movie);

        // 4. Seans Ekle (Otomatik olarak filme ve salona bağlanır)
        Showtime showtime = Showtime.builder()
                .movie(movie)
                .hall(hall)
                .date(LocalDate.now()) // Bugün
                .startTime(LocalTime.of(20, 0))
                .endTime(LocalTime.of(20, 0).plusMinutes(movie.getDuration()))
                .build();
        showtimeRepository.save(showtime);

        System.out.println("✅ TEST VERİLERİ YÜKLENDİ!");
        System.out.println("👉 Kullanıcı ID: " + admin.getId() + " (Email: admin@cinetime.com / Şifre: 123456)");
        System.out.println("👉 Film ID: " + movie.getId());
        System.out.println("👉 Seans ID: " + showtime.getId());
    }
}