package com.sellbycar.marketplace.ad;

import com.sellbycar.marketplace.util.ResponseUtil;
import com.sellbycar.marketplace.util.exception.FavoritesCarsNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public ResponseEntity<?> showAllAdvertisement(@RequestParam(name = "sortByDate") boolean flag) {
        if (flag) {
            Sort sort = Sort.by(
                    Sort.Order.asc("dateAdded"));
            var advertisementDto = advertisementService.findAllAd(sort);

            return ResponseUtil.create("All advertisements", HttpStatus.OK, advertisementDto);
        }

        var advertisementDto = advertisementService.findAllAd();
        return ResponseUtil.create("All advertisements", HttpStatus.OK, advertisementDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get advertisement by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> getAdvertisementById(@PathVariable Long id) {
        AdvertisementDAO adv = advertisementService.getAdvertisement(id);
        AdvertisementDTO advertisementDTO = advertisementMapper.toDTO(adv);

        return ResponseUtil.create("Advertisement by id", HttpStatus.OK, advertisementDTO);
    }

    @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
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
        long advertisementId = advertisementService.createAdvertisement(advertisementDTO, images);

        return ResponseUtil.create("Created", HttpStatus.CREATED, advertisementId);
    }

    @PutMapping("/{id}/update")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update data's of advertisement by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<?> changeAdvertisement(@RequestBody AdvertisementDTO advertisementDTO,
                                                 @PathVariable Long id) {
        AdvertisementDAO advertisement = advertisementService.updateADv(advertisementDTO, id);
        AdvertisementDTO advDTO = advertisementMapper.toDTO(advertisement);

        return ResponseUtil.create("Updated", HttpStatus.OK, advDTO);
    }

    @GetMapping("/favorites")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get all favorites")
    public ResponseEntity<?> getAllFavorites() {
        try {
            Set<AdvertisementDAO> favoritesCars = advertisementService.getAllFavorites();
            Set<AdvertisementDTO> favCarsDto = advertisementMapper.toDtoSet(favoritesCars);
            return ResponseUtil.create("Data was gotten successfully", HttpStatus.OK, favCarsDto);
        } catch (FavoritesCarsNotFoundException e) {
            return ResponseUtil.createError("You did not add a cars to your favorite list",
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/favorites")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Add an advertisement to favorite list")
    public ResponseEntity<?> addToFavoriteList(@PathVariable Long id) {

        try {
            AdvertisementDAO advertisement = advertisementService.addToFavoriteList(id);
            AdvertisementDTO advertisementDTO = advertisementMapper.toDTO(advertisement);

            return ResponseUtil.create("The advertisement was added to your favorite list", HttpStatus.OK, advertisementDTO);
        } catch (FavoritesCarsNotFoundException e) {
            return ResponseUtil.createError(e.getMessage(), HttpStatus.NOT_FOUND);
        }


    }

    @DeleteMapping("/{id}/favorites")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Remove the advertisement from favorite list")
    public ResponseEntity<?> removeFromFavoriteList(@PathVariable Long id) {
        advertisementService.removeFromFavoriteList(id);
        return ResponseUtil.create("The advertisement was removed", HttpStatus.OK);
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
        return ResponseUtil.create("Ok", HttpStatus.OK);
    }
}
