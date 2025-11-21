package com.cinetime.service;

import com.cinetime.dto.request.CreateShowtimeRequest;
import com.cinetime.entity.Hall;
import com.cinetime.entity.Movie;
import com.cinetime.entity.Showtime;
import com.cinetime.repository.HallRepository;
import com.cinetime.repository.MovieRepository;
import com.cinetime.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowtimeService { // Sınıf adının dosya adıyla (ShowTimeService veya ShowtimeService) aynı olduğuna emin ol

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;

    public Showtime createShowtime(CreateShowtimeRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Film bulunamadı!"));

        Hall hall = hallRepository.findById(request.getHallId())
                .orElseThrow(() -> new RuntimeException("Salon bulunamadı!"));

        // LocalTime ile dakika toplama işlemi
        LocalTime endTime = request.getStartTime().plusMinutes(movie.getDuration());

        Showtime showtime = Showtime.builder()
                .date(request.getDate())
                .startTime(request.getStartTime()) // Artık türler uyuşuyor (LocalTime -> LocalTime)
                .endTime(endTime)
                .movie(movie)
                .hall(hall)
                .build();

        return showtimeRepository.save(showtime);
    }

    public List<Showtime> getShowtimesByMovie(Long movieId) {
        return showtimeRepository.findAllByMovie_Id(movieId);

    }
    //TUM SEANSLARI GETIRME
    public List<Showtime>getAllShowtimes(){
        return showtimeRepository.findAll();
    }
}