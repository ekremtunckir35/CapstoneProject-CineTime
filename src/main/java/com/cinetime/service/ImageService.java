package com.cinetime.service;

import com.cinetime.entity.Image;
import com.cinetime.repository.ImageRepository;
import com.cinetime.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    // Resim Yükle
    public String uploadImage(MultipartFile file) throws IOException {
        Image image = imageRepository.save(Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes()))
                .build());

        return "Resim basariyla yuklendi: " + file.getOriginalFilename();
    }

    // Resim İndir (Hatayı Çözen Kısım)
    @Transactional
    public byte[] downloadImage(String fileName) {
        // Aynı isimde birden fazla resim varsa İLKİNİ getirir (Hata vermez)
        Image dbImage = imageRepository.findFirstByName(fileName)
                .orElseThrow(() -> new RuntimeException("Resim bulunamadi: " + fileName));

        return ImageUtils.decompressImage(dbImage.getImageData());
    }

    // Resim Sil
    public void deleteImage(Long id) {
        if (!imageRepository.existsById(id)) {
            throw new RuntimeException("Resim bulunamadı!");
        }
        imageRepository.deleteById(id);
    }

    // Resim Güncelle
    public String updateImage(Long id, MultipartFile file) throws IOException {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resim bulunamadı!"));

        image.setName(file.getOriginalFilename());
        image.setType(file.getContentType());
        image.setImageData(ImageUtils.compressImage(file.getBytes()));

        imageRepository.save(image);
        return "Resim güncellendi: " + file.getOriginalFilename();
    }
}