package com.cinetime.service;

import com.cinetime.dto.request.RegisterRequest;
import com.cinetime.dto.request.UpdateUserRequest;
import com.cinetime.entity.User;
import com.cinetime.entity.enums.Gender;
import com.cinetime.entity.enums.RoleType;
import com.cinetime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    // Tüm bağımlılıkları (Dependency Injection) sınıfın en başında tanımlıyoruz
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final ImageService imageService;

    // 1. KAYIT OL (REGISTER)
    public User register(RegisterRequest request) {

        // Email kontrolü
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Bu email adresi zaten kullanımda!");
        }

        // DTO -> Entity Dönüşümü
        User user = User.builder()
                .name(request.getFirstname())
                .surname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .roleType(RoleType.MEMBER)
                .builtin(false)
                .birthDate(request.getBirthDate()) // RegisterRequest'te 'birthday' alanı var
                .gender(Gender.NO_DISCLOSURE)
                .build();

        return userRepository.save(user);
    }

    // EMAIL İLE KULLANICI BUL
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + email));
    }

    // KULLANICI SİLME (PDF: U07)
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Kullanıcı bulunamadı!");
        }
        userRepository.deleteById(id);
    }

    // KULLANICI GÜNCELLEME (PDF: U06)
    public User updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        // Sadece dolu gelen alanları güncelle
        if (request.getName() != null) user.setName(request.getName());
        if (request.getSurname() != null) user.setSurname(request.getSurname());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());

        return userRepository.save(user);
    }

    // ŞİFREMİ UNUTTUM (PDF: U03)
    public void forgotPassword(String email) {
        User user = findByEmail(email);

        // Rastgele kod üret
        String resetCode = UUID.randomUUID().toString().substring(0, 8); // Örn: a1b2c3d4
        user.setResetPasswordCode(resetCode);
        userRepository.save(user);

        // Mail gönderme (Hata verirse loglayıp geçiyoruz, test ortamında çökmemesi için)
        try {
            sendEmail(email, "Şifre Sıfırlama Kodu", "Kodunuz: " + resetCode);
        } catch (Exception e) {
            System.out.println("Mail gönderilemedi (Test ortamı - App Password kontrol edin): " + e.getMessage());
        }
    }

    // ŞİFRE SIFIRLAMA (PDF: U04)
    public void resetPassword(String resetCode, String newPassword) {
        // Bu metodun çalışması için UserRepository'de 'findByResetPasswordCode' metodu olmalı.
        // Şu anlık boş bırakıyoruz veya mantığını buraya ekleyebilirsin.
    }

    // YARDIMCI MAIL METODU
    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    // PROFIL RESMİ YÜKLEME
    public void uploadProfileImage(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // 1. Resmi Image tablosuna kaydet
        imageService.uploadImage(file);

        // 2. Kullanıcıya resim adını ata
        // NOT: User tablosunda 'profileImage' alanı oluşturduysan aşağıdaki yorumu kaldır.
        // user.setProfileImage(file.getOriginalFilename());

        userRepository.save(user);
    }
}