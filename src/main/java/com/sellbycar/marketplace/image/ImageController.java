package com.sellbycar.marketplace.image;

import com.sellbycar.marketplace.web.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@Tag(name = "Image Library", description = "Endpoints for work with images")
@CrossOrigin(origins = "*")
public class ImageController {

    private final ImageService imageService;
    private final ImageMapper imageMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Download image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<?> downloadImage(@PathVariable Long id) {
        Optional<ImageDAO> optionalImage = imageService.getImageById(id);

        if (optionalImage.isEmpty()) {
            return ResponseUtil.error("Image not found.", HttpStatus.NOT_FOUND);
        }

        ImageDAO image = optionalImage.get();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(image.getContent().length);
        headers.setContentType(MediaType.valueOf(image.getContentType()));

        return new ResponseEntity<>(new InputStreamResource(new ByteArrayInputStream(image.getContent())), headers, HttpStatus.OK);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Upload image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> uploadImage(@RequestPart MultipartFile image) {
        if (image.isEmpty()) {
            return ResponseUtil.error("Image should not be empty.", HttpStatus.BAD_REQUEST);
        }
        ImageDAO imageDAO = imageService.createImage(image);
        return ResponseUtil.created(imageMapper.toDTO(imageDAO));
    }
}
