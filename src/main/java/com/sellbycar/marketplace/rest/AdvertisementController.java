package com.sellbycar.marketplace.rest;

import com.sellbycar.marketplace.persistance.model.Advertisement;
import com.sellbycar.marketplace.rest.dto.AdDTO;
import com.sellbycar.marketplace.service.AdvertisementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<Advertisement>> showAllAd() {
        return ResponseEntity.ok(advertisementService.findAllAd());
    }

    @GetMapping("/get/ad/{id}")
    public ResponseEntity<Advertisement> getAd(@PathVariable Long id) {
        Advertisement adv = advertisementService.getAd(id);
//        AdDTO dto = new AdDTO();
//        dto.setName(adv.getName());
//        dto.setDescription(adv.getDescription());
        return ResponseEntity.ok(adv);
    }

    @PostMapping("/users/create/ad")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> createAd(@RequestBody Advertisement advertisement) {
        advertisementService.saveNewAd(advertisement);
        return ResponseEntity.ok("Success");
    }
}
