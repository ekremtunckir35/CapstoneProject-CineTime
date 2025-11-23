package com.cinetime.controller;

import com.cinetime.config.JwtService;
import com.cinetime.dto.request.LoginRequest;
import com.cinetime.dto.request.RegisterRequest;
import com.cinetime.entity.User;
import com.cinetime.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(userService.register(request), HttpStatus.CREATED);
    }

    // LOGIN (Hata Burada Çıkıyordu, Düzelttik)
    @PostMapping("/login")
    // DİKKAT: Aşağıdaki parantez içindeki ismin 'request' olması şart!
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {

        // 1. Kullanıcı adı ve şifreyi doğrula
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. Doğrulama başarılıysa kullanıcıyı veritabanından bul
        User user = userService.findByEmail(request.getEmail());

        // 3. Token üret
        String token = jwtService.generateToken(user);

        // 4. Token'ı döndür
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
    // Kullanıcı Sil
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Kullanıcı silindi.");
    }

    // Kullanıcı Güncelle
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody com.cinetime.dto.request.UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        userService.forgotPassword(email);
        return ResponseEntity.ok("Sıfırlama kodu mail adresinize gönderildi.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String code, @RequestParam String newPassword) {
        userService.resetPassword(code, newPassword);
        return ResponseEntity.ok("Şifreniz başarıyla güncellendi.");
    }
    // Profil Resmi Yükle
    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfileImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) throws IOException {
        userService.uploadProfileImage(id, file);
        return ResponseEntity.ok("Profil resmi güncellendi.");
    }
}