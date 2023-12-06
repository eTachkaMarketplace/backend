package com.sellbycar.marketplace.controllers;

import com.sellbycar.marketplace.models.entities.Image;
import com.sellbycar.marketplace.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImageController {

    private final ImageRepository imageRepository;

    @GetMapping("/images/{id}")
    public ResponseEntity<InputStreamResource> getImageById(@PathVariable Long id) {
        Optional<Image> optionalImage = imageRepository.findById(id);

        if (optionalImage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Image image = optionalImage.get();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(image.getResource().length);
        headers.setContentType(MediaType.valueOf(image.getContentType()));

        return new ResponseEntity<>(new InputStreamResource(new ByteArrayInputStream(image.getResource())), headers, HttpStatus.OK);
    }
}
