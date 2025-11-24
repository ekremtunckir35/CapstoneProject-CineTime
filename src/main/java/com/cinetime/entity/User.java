package com.cinetime.entity; // <-- DOĞRU PAKET BU OLMALI

import com.cinetime.entity.enums.Gender;
import com.cinetime.entity.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    @Size(min = 3, max = 20)
    private String name;

    @Column(nullable = false, length = 25)
    @Size(min = 3, max = 25)
    private String surname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    @Email(message = "Lütfen geçerli bir email giriniz")
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType roleType;

    @Column(nullable = false)
    @Builder.Default
    private Boolean builtin = false;

    private String resetPasswordCode;

    // Profil resmi (Resmin adı veya yolu tutulur)
    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    // --- UserDetails Metodları ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(roleType.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}