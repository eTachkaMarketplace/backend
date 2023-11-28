package com.sellbycar.marketplace.rest;

import com.sellbycar.marketplace.rest.dto.AdvertisementDTO;
import com.sellbycar.marketplace.service.AdvertisementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "ADV Library", description = "Endpoints for ADV")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @GetMapping("/get/all/ad")
    @Operation(summary = "Login User")
    public ResponseEntity<List<AdvertisementDTO>> showAllAd() {
        return ResponseEntity.ok(advertisementService.findAllAd());
    }

    @GetMapping("/get/ad/{id}")
    public ResponseEntity<AdvertisementDTO> getAd(@PathVariable Long id) {
        AdvertisementDTO adv = advertisementService.getAd(id);
        return ResponseEntity.ok(adv);
    }

    @PostMapping("/users/create/ad")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> createAd(@RequestBody AdvertisementDTO advertisementDTO) {
        advertisementService.saveNewAd(advertisementDTO);
        return ResponseEntity.ok("Success");
    }

    @PutMapping("users/change/ad/{id}")
    public ResponseEntity<AdvertisementDTO> changeADv(@RequestBody AdvertisementDTO advertisementDTO
            , @PathVariable Long id) {
        advertisementService.updateADv(advertisementDTO, id);
        return ResponseEntity.ok(advertisementDTO);
    }
}
