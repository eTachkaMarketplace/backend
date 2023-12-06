package com.sellbycar.marketplace.controllers;

import com.sellbycar.marketplace.models.dto.AdvertisementDTO;
import com.sellbycar.marketplace.models.entities.Advertisement;
import com.sellbycar.marketplace.services.AdvertisementService;
import com.sellbycar.marketplace.utilities.mapper.AdvertisementMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/advertisements")
@RequiredArgsConstructor
@Tag(name = "ADV Library", description = "Endpoints for ADV")
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final AdvertisementMapper advertisementMapper;

    @GetMapping("")
    @Operation(summary = "Get all advertisement")
    public ResponseEntity<?> showAllAd() {
        return ResponseEntity.ok(advertisementService.findAllAd());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get advertisement by id")
    public ResponseEntity<?> getAdById(@PathVariable Long id) {
        Advertisement adv = advertisementService.getAd(id);
        AdvertisementDTO advertisementDTO = advertisementMapper.toDTO(adv);

        return ResponseEntity.ok(advertisementDTO);
    }

    @PostMapping("/create")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create a new advertisement")
    public ResponseEntity<?> createAd(@RequestPart(value = "files") List<MultipartFile> files,
//                                      @RequestPart(value = "file2") MultipartFile file2,
//                                      @RequestPart(value = "file3") MultipartFile file3,
                                      @RequestPart("advertisementDTO") AdvertisementDTO advertisementDTO) throws IOException {
        advertisementService.createAdvertisement(advertisementDTO, files);
        return ResponseEntity.ok("Success");
    }

    @PutMapping("/{id}/update")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update datas of advertisement by id")
    public ResponseEntity<?> changeADv(@RequestBody AdvertisementDTO advertisementDTO,
                                       @PathVariable Long id) {
        Advertisement advertisement = advertisementService.updateADv(advertisementDTO, id);
        AdvertisementDTO advDTO = advertisementMapper.toDTO(advertisement);

        return ResponseEntity.ok(advDTO);
    }
}
