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

    // 5. ID İLE FİLM GETİRME
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Film bulunamadı!"));
    }

    // 6. FİLM SİLME
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new RuntimeException("Silinecek film bulunamadı!");
        }
        movieRepository.deleteById(id);
    }

    // 7. FİLM GÜNCELLEME
    public Movie updateMovie(Long id, CreateMovieRequest request) {
        Movie existingMovie = getMovieById(id);
        return movieRepository.save(mapRequestToMovie(request, existingMovie));
    }

    // YARDIMCI METOD: DTO'yu Entity'ye çevirir
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

    // YARDIMCI METOD: SLUG ÜRETME (Hata buradaydı, düzeltildi)
    private String toSlug(String input) {
        if (input == null) {
            return "";
        }
        String nonLatin = Pattern.compile("[^\\w-]")
                .matcher(Normalizer.normalize(input, Normalizer.Form.NFD))
                .replaceAll("");
        return nonLatin.toLowerCase(Locale.ENGLISH)
                .replaceAll(" ", "-")
                .replaceAll("--", "-");
    }

    public List<Movie> getMoviesByCinema(Long cinemaId) {
        return movieRepository.findMoviesByCinemaId(cinemaId);
    }

    public List<Movie> getMoviesByHall(String hallName) {
        return movieRepository.findMoviesByHallName(hallName);
    }
}