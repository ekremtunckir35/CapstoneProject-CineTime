package com.cinetime.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
     private String firstname; // name -> firstname oldu (PDF uyumlu)

     @NotBlank(message = "Soyisim boş bırakılamaz")
     @Size(min = 3, max = 25)
     private String lastname; // surname -> lastname oldu (PDF uyumlu)

     @NotBlank(message = "Email boş bırakılamaz")
     @Email(message = "Geçerli bir email giriniz")
     private String email;

     @NotBlank(message = "Şifre boş bırakılamaz")
     @Size(min = 8, max = 64)
     private String password;

     @NotBlank(message = "Telefon numarası boş bırakılamaz")
     private String phoneNumber;

     @NotNull(message = "Doğum tarihi boş bırakılamaz")
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
     private LocalDate birthDate; // birthday -> birthDate oldu (Entity uyumlu)
}