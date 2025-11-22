package com.cinetime.service;

import com.cinetime.entity.Favorite;
import com.cinetime.entity.Movie;
import com.cinetime.entity.User;
import com.cinetime.repository.FavoriteRepository;
import com.cinetime.repository.MovieRepository;
import com.cinetime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    // Metodun adı: addMovieToFavorites ('s' harfine dikkat)
    public Favorite addMovieToFavorites(Long userId, Long movieId) {
        // Daha önce eklenmiş mi kontrolü
        if (favoriteRepository.existsByUser_Id_AndMovie_Id(userId, movieId)) {
            throw new RuntimeException("Bu film zaten favorilerinizde!");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Film bulunamadı"));

        Favorite favorite = Favorite.builder()
                .user(user)
                .movie(movie)
                .build();

        return favoriteRepository.save(favorite);
    }

    public List<Favorite> getUserFavorites(Long userId) {
        return favoriteRepository.findAllByUser_Id(userId);
    }
}