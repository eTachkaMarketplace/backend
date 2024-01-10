package com.sellbycar.marketplace.image;

import com.sellbycar.marketplace.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Image Library", description = "Endpoints for work with images")
@CrossOrigin(origins = "*")
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/{id}")
    @Operation(summary = "Get image by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<InputStreamResource> getImageById(@PathVariable Long id) {
        Optional<ImageDAO> optionalImage = imageService.getImageById(id);

        if (optionalImage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        ImageDAO image = optionalImage.get();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(image.getResource().length);
        headers.setContentType(MediaType.valueOf(image.getContentType()));

        return new ResponseEntity<>(new InputStreamResource(new ByteArrayInputStream(image.getResource())), headers, HttpStatus.OK);
    }


    @PostMapping("/set/{imageId}/as-preview/{advertisementId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Set preview image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> setAsPreviewImage(@PathVariable Long imageId,
                                               @PathVariable Long advertisementId) {
        imageService.addPreviewImageToAdvertisement(advertisementId, imageId);
        return ResponseUtil.create("Image set as preview successfully", HttpStatus.OK);
    }
}
