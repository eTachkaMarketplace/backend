package com.sellbycar.marketplace.controllers;

import com.sellbycar.marketplace.models.dto.AdvertisementDTO;
import com.sellbycar.marketplace.models.entities.Advertisement;
import com.sellbycar.marketplace.services.AdvertisementService;
import com.sellbycar.marketplace.utilities.exception.CustomUserException;
import com.sellbycar.marketplace.utilities.exception.FavoritesCarsNotFoundException;
import com.sellbycar.marketplace.utilities.handlers.ResponseHandler;
import com.sellbycar.marketplace.utilities.mapper.AdvertisementMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
    public ResponseEntity<?> createAd(@RequestBody AdvertisementDTO advertisementDTO) {
        advertisementService.saveNewAd(advertisementDTO);
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

    @GetMapping("/favorites")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get all favorites")
    public ResponseEntity<?> getAllFavorites() {
        try {
            Set<Advertisement> favoritesCars = advertisementService.getAllFavorites();
            Set<AdvertisementDTO> favCarsDto = advertisementMapper.toDtoSet(favoritesCars);
            return ResponseHandler.generateResponse("Data was gotten successfully", HttpStatus.OK, favCarsDto);
        } catch (FavoritesCarsNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new FavoritesCarsNotFoundException());
        }
    }

    @PostMapping("/{id}/favorites")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Add an advertisement to favorite list")
    public ResponseEntity<?> addToFavoriteList(@PathVariable Long id) {
        Advertisement advertisement = advertisementService.addToFavoriteList(id);
        AdvertisementDTO advertisementDTO = advertisementMapper.toDTO(advertisement);
        return ResponseHandler.generateResponse("The advertisement was added to your favorite list", HttpStatus.OK, advertisementDTO);
    }

    @DeleteMapping("/{id}/favorites")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Remove the advertisement from favorite list")
    public ResponseEntity<?> removeFromFavoriteList(@PathVariable Long id) {
        advertisementService.removeFromFavoriteList(id);
        return ResponseHandler.generateResponse("The advertisement was removed", HttpStatus.OK);
    }
}
