package com.cinetime.service;

import com.cinetime.dto.request.CreateMovieRequest;
import com.cinetime.dto.tmdb.TmdbMovie;
import com.cinetime.dto.tmdb.TmdbResponse;
import com.cinetime.entity.Movie;
import com.cinetime.entity.enums.MovieStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TmdbService {

    private final MovieService movieService;
    private final RestTemplate restTemplate = new RestTemplate(); // HTTP istekleri için

    // --- AYARLAR ---
    private final String API_KEY = "51ec09416d77ea5823af5bcd3971015e"; // <-- SENİN ANAHTARIN
    private final String BASE_URL = "https://api.themoviedb.org/3";
    private final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    public void importMoviesFromTmdb() {
        System.out.println("🌍 TMDB'den filmler çekiliyor...");

        // 1. Vizyondaki Filmleri Çek
        String url = BASE_URL + "/movie/now_playing?api_key=" + API_KEY + "&language=tr-TR&page=1";

        try {
            // JSON'u Java Nesnesine (TmdbResponse) çeviriyoruz
            ResponseEntity<TmdbResponse> response = restTemplate.getForEntity(url, TmdbResponse.class);

            if (response.getBody() != null && response.getBody().getResults() != null) {
                List<TmdbMovie> tmdbMovies = response.getBody().getResults();

                for (TmdbMovie tmdbMovie : tmdbMovies) {
                    saveTmdbMovieToDb(tmdbMovie);
                }
            }
        } catch (Exception e) {
            System.err.println("❌ TMDB Bağlantı Hatası: " + e.getMessage());
        }
    }

    private void saveTmdbMovieToDb(TmdbMovie tmdbMovie) {
        try {
            // Tarih Formatını Çevir (YYYY-MM-DD -> LocalDate)
            LocalDate releaseDate = LocalDate.parse(tmdbMovie.getReleaseDate());

            // DTO Hazırla
            CreateMovieRequest request = new CreateMovieRequest();
            request.setTitle(tmdbMovie.getTitle());
            request.setSummary(tmdbMovie.getOverview().isEmpty() ? "Özet bulunamadı." : tmdbMovie.getOverview());
            request.setReleaseDate(releaseDate);
            request.setDuration(120); // TMDB listesinde süre gelmez, detay isteği gerekir. Şimdilik 120 sabit.
            request.setDirector("Bilinmiyor"); // Yönetmen için de ekstra istek gerekir.
            request.setGenre("Genel"); // Tür ID'lerini çevirmekle uğraşmayalım şimdilik.

            // Resim yolunu tam URL yap
            String fullPosterUrl = IMAGE_BASE_URL + tmdbMovie.getPosterPath();
            request.setPoster(fullPosterUrl);

            request.setStatus(MovieStatus.IN_THEATERS);
            request.setCast(List.of("Oyuncu 1", "Oyuncu 2")); // Dummy veri
            request.setFormats(List.of("2D", "Altyazılı"));

            // Java Service'ini kullanarak kaydet
            movieService.createMovie(request);
            System.out.println("✅ Eklendi: " + tmdbMovie.getTitle());

        } catch (Exception e) {
            // Aynı film varsa hata verir, pas geçelim
            System.out.println("⚠️ Geçildi: " + tmdbMovie.getTitle());
        }
    }
}