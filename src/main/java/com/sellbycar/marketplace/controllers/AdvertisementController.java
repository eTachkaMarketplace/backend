package com.sellbycar.marketplace.controllers;

import com.sellbycar.marketplace.models.dto.AdvertisementDTO;
import com.sellbycar.marketplace.models.entities.Advertisement;
import com.sellbycar.marketplace.services.AdvertisementService;
import com.sellbycar.marketplace.utilities.exception.FavoritesCarsNotFoundException;
import com.sellbycar.marketplace.utilities.handlers.ResponseHandler;
import com.sellbycar.marketplace.utilities.mapper.AdvertisementMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/advertisements")
@RequiredArgsConstructor
@Tag(name = "Advertisement Library", description = "Endpoints for manage Advertisement")
@CrossOrigin(origins = "*")
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final AdvertisementMapper advertisementMapper;

    @GetMapping("")
    @Operation(summary = "Get all advertisement")
    @ApiResponse(responseCode = "200", description = "Ok")
    public ResponseEntity<?> showAllAd(@RequestParam(name = "sortByDate") boolean flag) {
        if (flag) {
            Sort sort = Sort.by(
                    Sort.Order.asc("dateAdded"));
            var advertisementDto = advertisementService.findAllAd(sort);

            return ResponseHandler.generateResponse("All advertisements", HttpStatus.OK, advertisementDto);
        }

        var advertisementDto = advertisementService.findAllAd();
        return ResponseHandler.generateResponse("All advertisements", HttpStatus.OK, advertisementDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get advertisement by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> getAdById(@PathVariable Long id) {
        Advertisement adv = advertisementService.getAd(id);
        AdvertisementDTO advertisementDTO = advertisementMapper.toDTO(adv);

        return ResponseHandler.generateResponse("Advertisement by id", HttpStatus.OK, advertisementDTO);
    }

    @PostMapping("/create")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create a new advertisement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> createAd(@RequestPart(value = "images") List<MultipartFile> images,
                                      @RequestPart("advertisementDTO") AdvertisementDTO advertisementDTO) throws IOException {
        advertisementService.createAdvertisement(advertisementDTO, images);

        return ResponseHandler.generateResponse("Created", HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update data's of advertisement by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404",description = "Not Found")
    })
    public ResponseEntity<?> changeADv(@RequestBody AdvertisementDTO advertisementDTO,
                                       @PathVariable Long id) {
        Advertisement advertisement = advertisementService.updateADv(advertisementDTO, id);
        AdvertisementDTO advDTO = advertisementMapper.toDTO(advertisement);

        return ResponseHandler.generateResponse("Updated", HttpStatus.OK, advDTO);
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
            return ResponseHandler.generateError("You did not add a cars to your favorite list",
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/favorites")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Add an advertisement to favorite list")
    public ResponseEntity<?> addToFavoriteList(@PathVariable Long id) {

        try {
            Advertisement advertisement = advertisementService.addToFavoriteList(id);
            AdvertisementDTO advertisementDTO = advertisementMapper.toDTO(advertisement);

            return ResponseHandler.generateResponse("The advertisement was added to your favorite list", HttpStatus.OK, advertisementDTO);
        } catch (FavoritesCarsNotFoundException e) {
            return ResponseHandler.generateError(e.getMessage(), HttpStatus.NOT_FOUND);
        }


    }

    @DeleteMapping("/{id}/favorites")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Remove the advertisement from favorite list")
    public ResponseEntity<?> removeFromFavoriteList(@PathVariable Long id) {
        advertisementService.removeFromFavoriteList(id);
        return ResponseHandler.generateResponse("The advertisement was removed", HttpStatus.OK);
    }

    @DeleteMapping("/{id}/remove")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> deleteAdvertisement(@PathVariable Long id) {
        advertisementService.removeAdvertisement(id);
        return ResponseHandler.generateResponse("Ok", HttpStatus.OK);
    }
}
