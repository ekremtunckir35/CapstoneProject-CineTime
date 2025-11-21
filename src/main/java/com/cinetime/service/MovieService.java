package com.cinetime.service;

import com.cinetime.dto.request.CreateMovieRequest;
import com.cinetime.entity.Movie;
import com.cinetime.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
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

    // TEK FİLM KAYDETME
    public Movie createMovie(CreateMovieRequest request) {
        if (movieRepository.existsByTitle(request.getTitle())) {
            throw new RuntimeException("Bu isimde bir film zaten mevcut!");
        }

        Movie movie = Movie.builder()
                .title(request.getTitle())
                .summary(request.getSummary())
                .releaseDate(request.getReleaseDate())
                .duration(request.getDuration())
                .director(request.getDirector())
                .cast(request.getCast())
                .formats(request.getFormats())
                .genre(request.getGenre())
                .poster(request.getPoster())
                .status(request.getStatus())
                .slug(toSlug(request.getTitle()))
                .build();

        return movieRepository.save(movie);
    }

    // TOPLU FİLM KAYDETME (Bulk Insert)
    public List<Movie> createMovies(List<CreateMovieRequest> requests) {
        List<Movie> savedMovies = new ArrayList<>();

        // Döngü: Listeden tek tek 'request' çekiyoruz
        for (CreateMovieRequest request : requests) {
            try {
                savedMovies.add(createMovie(request));
            } catch (Exception e) {
                // DİKKAT: Burada 'requests' değil 'request' kullanıyoruz!
                System.out.println("Film eklenemedi: " + request.getTitle() + " -> " + e.getMessage());
            }
        }
        return savedMovies;
    }

    // SLUG ÜRETME METODU
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

    // FİLMLERİ DURUMUNA GÖRE GETİRME (Vizyondakiler, Yakında vb.)
    public List<Movie> getMoviesByStatus(com.cinetime.entity.enums.MovieStatus status) {
        return movieRepository.findAllByStatus(status);
    }
}