package com.sellbycar.marketplace.ad;

import com.sellbycar.marketplace.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/advertisement")
@RequiredArgsConstructor
@Tag(name = "Advertisement Library", description = "Endpoints for Advertisement business logic")
@CrossOrigin(origins = "*")
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final AdvertisementMapper advertisementMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get advertisement by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> getAdvertisementById(@PathVariable Long id) {
        AdvertisementDAO advertisement = advertisementService.getAdvertisement(id);
        return ResponseUtil.ok(advertisementMapper.toDTO(advertisement));
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create a new advertisement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    public ResponseEntity<?> createAdvertisement(
            @RequestPart("payload") AdvertisementDTO advertisementDTO,
            @RequestPart("images") List<MultipartFile> images
    ) throws IOException {
        AdvertisementDAO advertisement = advertisementService.createAdvertisement(advertisementDTO, images);
        return ResponseUtil.created(advertisementMapper.toDTO(advertisement));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update an advertisement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> changeAdvertisement(@RequestBody AdvertisementDTO advertisementDTO) {
        AdvertisementDAO advertisement = advertisementService.updateAdvertisement(advertisementDTO);
        AdvertisementDTO advDTO = advertisementMapper.toDTO(advertisement);

        return ResponseUtil.ok("The advertisement was updated.", advDTO);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> deleteAdvertisement(@PathVariable Long id) {
        advertisementService.removeAdvertisement(id);
        return ResponseUtil.ok("The advertisement was deleted.");
    }

    @GetMapping("/favorites")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get all favorites")
    public ResponseEntity<?> getAllFavorites() {
        Set<AdvertisementDAO> favorites = advertisementService.getAllFavorites();
        return ResponseUtil.ok(advertisementMapper.toDtoSet(favorites));
    }

    @PostMapping("/favorites/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Add an advertisement to favorite list")
    public ResponseEntity<?> addToFavoriteList(@PathVariable Long id) {
        AdvertisementDAO advertisement = advertisementService.addToFavoriteList(id);
        AdvertisementDTO advertisementDTO = advertisementMapper.toDTO(advertisement);
        return ResponseUtil.created(advertisementDTO);
    }

    @DeleteMapping("/favorites/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Remove the advertisement from favorite list")
    public ResponseEntity<?> removeFromFavoriteList(@PathVariable Long id) {
        advertisementService.removeFromFavoriteList(id);
        return ResponseUtil.ok("The advertisement was removed from your favorite list.");
    }
}
