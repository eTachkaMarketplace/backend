package com.sellbycar.marketplace.controllers;

import com.sellbycar.marketplace.models.dto.AdvertisementDTO;
import com.sellbycar.marketplace.services.AdvertisementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/advertisements")
@RequiredArgsConstructor
@Tag(name = "ADV Library", description = "Endpoints for ADV")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @GetMapping("")
    @Operation(summary = "Get all advertisement")
    public ResponseEntity<?> showAllAd() {
        return ResponseEntity.ok(advertisementService.findAllAd());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get advertisement by id")
    public ResponseEntity<?> getAdById(@PathVariable Long id) {
        AdvertisementDTO adv = advertisementService.getAd(id);
        return ResponseEntity.ok(adv);
    }

    @PostMapping("/create")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create a new advertisement")
    public ResponseEntity<?> createAd(@RequestBody AdvertisementDTO advertisementDTO) {
        advertisementService.saveNewAd(advertisementDTO);
        return ResponseEntity.ok("Success");
    }

    @PutMapping("/{id}/update")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update datas of advertisement by id")
    public ResponseEntity<?> changeADv(@RequestBody AdvertisementDTO advertisementDTO,
                                                      @PathVariable Long id) {
        return ResponseEntity.ok(advertisementService.updateADv(advertisementDTO, id));
    }
}
