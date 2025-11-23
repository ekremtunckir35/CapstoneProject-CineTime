package com.cinetime.service;

import com.cinetime.dto.request.CreateCinemaRequest;
import com.cinetime.dto.request.CreateCityRequest;
import com.cinetime.dto.request.CreateHallRequest;
import com.cinetime.entity.Cinema;
import com.cinetime.entity.City;
import com.cinetime.entity.Hall;
import com.cinetime.repository.CinemaRepository;
import com.cinetime.repository.CityRepository;
import com.cinetime.repository.HallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class CinemaService {

      private final CityRepository cityRepository;
      private final CinemaRepository cinemaRepository;
      private final HallRepository hallRepository;

      // 1. Şehir Ekle (Controller'ın aradığı metod bu)
      public City createCity(CreateCityRequest request) {
            City city = City.builder().name(request.getName()).build();
            return cityRepository.save(city);
      }

      // 2. Sinema Ekle
      public Cinema createCinema(CreateCinemaRequest request) {
            City city = cityRepository.findById(request.getCityId())
                    .orElseThrow(() -> new RuntimeException("Şehir bulunamadı!"));

            Cinema cinema = Cinema.builder()
                    .name(request.getName())
                    .address(request.getAddress())
                    .city(city)
                    .build();
            return cinemaRepository.save(cinema);
      }

      // 3. Salon Ekle
      public Hall createHall(CreateHallRequest request) {
            Cinema cinema = cinemaRepository.findById(request.getCinemaId())
                    .orElseThrow(() -> new RuntimeException("Sinema bulunamadı!"));

            Hall hall = Hall.builder()
                    .name(request.getName())
                    .seatCapacity(request.getSeatCapacity())
                    .isSpecial(request.getIsSpecial())
                    .cinema(cinema)
                    .build();
            return hallRepository.save(hall);
      }


      // Tüm Şehirleri Getir
      public List<City> getAllCities() {
            return cityRepository.findAll();
      }

      // Şehre göre filtrele, şehir yoksa hepsini getir
      public List<Cinema> getCinemas(Long cityId) {
            if (cityId != null) {
                  return cinemaRepository.findAllByCity_Id(cityId);
            }
            return cinemaRepository.findAll();
      }

      public List<Hall> getHallsByCinema(Long cinemaId) {
            return hallRepository.findAllByCinema_Id(cinemaId);
      }

      public List<Hall> getSpecialHalls() {
            return hallRepository.findAllByIsSpecialTrue();
      }

      //Sinema Detayi Getirme

      // Sinema Detayı Getir (PDF: C03)
      public Cinema getCinemaById(Long id) {
            return cinemaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Sinema bulunamadı!"));
      }

}