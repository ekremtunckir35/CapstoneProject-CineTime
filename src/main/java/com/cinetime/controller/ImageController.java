package com.cinetime.controller;


import com.cinetime.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;;

    //Resim yukle (POST)

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
     public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file)
         throws IOException {
         String uploadImage = imageService.uploadImage(file);
         return ResponseEntity.status(HttpStatus.OK).body(uploadImage);

     }

     //Resim Goruntule (GET)

    public ResponseEntity<?>downloadImage(@PathVariable String fileName) {
        byte[] imageData = imageService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))//Varsayilan PNG dedik,dinamik de yapilabilir
                .body(imageData);

    }
    }


