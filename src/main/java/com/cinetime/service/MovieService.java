package com.cinetime.service;

import com.cinetime.dto.request.CreateMovieRequest;
import com.cinetime.entity.Movie;
import com.cinetime.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    // 1. TEK FİLM EKLEME
    public Movie createMovie(CreateMovieRequest request) {
        if (movieRepository.existsByTitle(request.getTitle())) {
            throw new RuntimeException("Bu isimde bir film zaten mevcut!");
        }
        return movieRepository.save(mapRequestToMovie(request, new Movie()));
    }

    // 2. TOPLU FİLM EKLEME
    public List<Movie> createMovies(List<CreateMovieRequest> requests) {
        List<Movie> savedMovies = new ArrayList<>();
        for (CreateMovieRequest request : requests) {
            try {
                savedMovies.add(createMovie(request));
            } catch (Exception e) {
                System.out.println("Film eklenemedi: " + request.getTitle());
            }
        }
        return savedMovies;
    }

    // 3. GELİŞMİŞ LİSTELEME
    public Page<Movie> getAllMovies(String title, int page, int size, String sortField, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        if (title != null && !title.isEmpty()) {
            return movieRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else {
            return movieRepository.findAll(pageable);
        }
    }

    // 4. DURUMA GÖRE GETİRME
    public List<Movie> getMoviesByStatus(com.cinetime.entity.enums.MovieStatus status) {
        return movieRepository.findAllByStatus(status);
    }

    // --- YENİ EKLENENLER ---

    // 5. ID İLE FİLM GETİRME (PDF: M09)
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Film bulunamadı!"));
    }

    // 6. FİLM SİLME (PDF: M13)
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new RuntimeException("Silinecek film bulunamadı!");
        }
        movieRepository.deleteById(id);
    }

    // 7. FİLM GÜNCELLEME (PDF: M12)
    public Movie updateMovie(Long id, CreateMovieRequest request) {
        Movie existingMovie = getMovieById(id); // Var mı kontrol et
        return movieRepository.save(mapRequestToMovie(request, existingMovie)); // Güncelle ve kaydet
    }

    // YARDIMCI METOD: DTO'yu Entity'ye çevirir (Kod tekrarını önlemek için)
    private Movie mapRequestToMovie(CreateMovieRequest request, Movie movie) {
        movie.setTitle(request.getTitle());
        movie.setSummary(request.getSummary());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setDuration(request.getDuration());
        movie.setDirector(request.getDirector());
        movie.setCast(request.getCast());
        movie.setFormats(request.getFormats());
        movie.setGenre(request.getGenre());
        movie.setPoster(request.getPoster());
        movie.setStatus(request.getStatus());
        movie.setSlug(toSlug(request.getTitle()));
        return movie;
    }

    // YARDIMCI METOD: SLUG ÜRETME
    private String toSlug(String input) {
        if (input == null) return "";
        String nonLatin = Pattern.compile("[^\\w-]")
                .matcher(Normalizer.normalize(input, Normalizer.Form.NFD))
                .replaceAll("");
        return nonLatin.toLowerCase(Locale.ENGLISH).replaceAll(" ", "-").replaceAll("--", "-");
    }
}