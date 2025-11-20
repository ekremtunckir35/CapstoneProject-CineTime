package com.cinetime.dto.request;
// kullanici girs yaparken gonderecegi verileri karsilayacak pencere


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

       @NotBlank(message = "Email boş bırakılamaz")
       private String email;

       @NotBlank(message = "Şifre boş bırakılamaz")
       private String password;

}
