package com.cinetime.service;

import com.cinetime.dto.request.RegisterRequest;
import com.cinetime.entity.User;
import com.cinetime.entity.enums.Gender;
import com.cinetime.entity.enums.RoleType;
import com.cinetime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(RegisterRequest request) {

        // 1. Email kontrolü
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Bu email adresi zaten kullanımda!");
        }

        // 2. DTO -> Entity Dönüşümü
        User user = User.builder()
                .name(request.getFirstname())
                .surname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                // DÜZELTME BURADA: .role() yerine .roleType() yaptık
                .roleType(RoleType.MEMBER)
                .builtin(false)
                .birthDate(request.getBirthday())
                .gender(Gender.NO_DISCLOSURE)
                .build();

        // 3. Kaydetme
        return userRepository.save(user);
    }

    public User findByEmail(String email){
        return  userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanici bulunamadi: " + email));
    }
    // Kullanıcı Silme (PDF: U07)
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Kullanıcı bulunamadı!");
        }
        userRepository.deleteById(id);
    }

    // Kullanıcı Güncelleme (PDF: U06)
    public User updateUser(Long id, com.cinetime.dto.request.UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        // Sadece dolu gelen alanları güncelle
        if (request.getName() != null) user.setName(request.getName());
        if (request.getSurname() != null) user.setSurname(request.getSurname());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());

        return userRepository.save(user);
    }
}