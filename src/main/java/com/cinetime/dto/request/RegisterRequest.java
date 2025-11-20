package com.cinetime.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull; // LocalDate için bu gerekli
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

     @NotBlank(message = "İsim boş bırakılamaz")
     @Size(min = 3, max = 20)
     private String firstname; // UserService'de getFirstname() olarak çağıracağız

     @NotBlank(message = "Soyisim boş bırakılamaz")
     @Size(min = 3, max = 25)
     private String lastname; // UserService'de getLastname() olarak çağıracağız

     @NotBlank(message = "Email boş bırakılamaz")
     @Email(message = "Geçerli bir email giriniz")
     private String email;

     @NotBlank(message = "Şifre boş bırakılamaz")
     @Size(min = 8, max = 64)
     private String password;

     @NotBlank(message = "Telefon numarası boş bırakılamaz")
     private String phoneNumber; // Yazım hatası düzeltildi (phoneMumber -> phoneNumber)

     @NotNull(message = "Doğum tarihi boş bırakılamaz") // @NotBlank yerine @NotNull yapıldı
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") // Tarih formatı belirlendi
     private LocalDate birthday;
}