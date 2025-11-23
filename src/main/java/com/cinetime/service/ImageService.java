package com.cinetime.service;


//Mantık: Kullanıcı yanlış bir resim yüklediğinde onu silebilmeli (delete)
// veya değiştirebilmelidir (update). Veritabanında ID ile resmi bulup işlem yapacağız.

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

    // Resim Silme (I03)
    public void deleteImage(Long id) {
        if (!imageRepository.existsById(id)) {
            throw new RuntimeException("Resim bulunamadı!");
        }
        imageRepository.deleteById(id);
    }

    // Resim Güncelleme (I04)
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
