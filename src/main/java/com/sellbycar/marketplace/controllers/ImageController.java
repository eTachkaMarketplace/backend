package com.sellbycar.marketplace.controllers;

import com.sellbycar.marketplace.models.entities.Image;
import com.sellbycar.marketplace.services.ImageService;
import com.sellbycar.marketplace.utilities.handlers.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.Optional;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("{id}")
    @Operation(summary = "Get image by id")
    public ResponseEntity<InputStreamResource> getImageById(@PathVariable Long id) {
        Optional<Image> optionalImage = imageService.getImageById(id);

        if (optionalImage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Image image = optionalImage.get();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(image.getResource().length);
        headers.setContentType(MediaType.valueOf(image.getContentType()));

        return new ResponseEntity<>(new InputStreamResource(new ByteArrayInputStream(image.getResource())), headers, HttpStatus.OK);
    }


    @PostMapping("/set/{imageId}/as-preview/{advertisementId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Set preview image")
    public ResponseEntity<?> setAsPreviewImage(@PathVariable Long imageId,
                                               @PathVariable Long advertisementId) {
        imageService.addPreviewImageToAdvertisement(advertisementId, imageId);
        return ResponseHandler.generateResponse("Image set as preview successfully",HttpStatus.OK);
    }
}
