package com.cinetime.service;

import com.cinetime.entity.Image;
import com.cinetime.repository.ImageRepository;
import com.cinetime.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    //Resmi yukle ve kaydet

    public String uploadImage(MultipartFile file) throws IOException {
        Image image = imageRepository.save(Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())

                // UTILS ILE SIKISTIRIP KAYEDIYORUZ

                .imageData(ImageUtils.compressImage(file.getBytes()))
                .build());

        return "Resim basariyla yuklendi:" + file.getOriginalFilename();
    }

    //Resmi Indir Goruntule

    public byte[] downloadImage(String fileName) {
        Image dbImage = imageRepository.findByName(fileName)
                .orElseThrow(() -> new RuntimeException(" Resim bulunamadi: " + fileName));


    //Utils ile sikistirmayi acip gonderiyoruz

     return ImageUtils.decompressImage(dbImage.getImageData());

}

}
