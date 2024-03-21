package com.sellbycar.marketplace.ad;

import com.sellbycar.marketplace.web.ResponseBody;
import com.sellbycar.marketplace.web.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final AdvertisementMapper advertisementMapper;

    @GetMapping(value = "/search")
    @Operation(summary = "Get advertisements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok")
    })
    public ResponseEntity<ResponseBody<List<AdvertisementDTO>>> searchAdvertisement(
            @Parameter(
                    name = "sort",
                    description = "Sort method",
                    schema = @Schema(
                            implementation = String.class,
                            examples = {"cheap", "expensive", "new", "old"},
                            pattern = "^(cheap|expensive|new|old)$"
                    )
            )
            @RequestParam("sort") String sortBy,
            @Parameter(
                    description = "Index of page",
                    schema = @Schema(implementation = Integer.class)
            )
            @RequestParam("page") Integer page,
            @Parameter(
                    description = "Number of elements per page",
                    schema = @Schema(
                            implementation = Integer.class,
                            defaultValue = "10",
                            maximum = "100"
                    )
            )
            @RequestParam("size") Integer size,
            @Parameter(
                    description = "Filter parameters",
                    schema = @Schema(
                            implementation = AdvertisementFilter.class,
                            description = "Fill in the fields you want to filter by"
                    )
            )
            @ModelAttribute AdvertisementFilter filter
    ) {
        Sort sort = switch (sortBy) {
            case "cheap" -> Sort.by("car.price").ascending();
            case "expensive" -> Sort.by("car.price").descending();
            case "new" -> Sort.by("createdTimestamp").descending();
            case "old" -> Sort.by("createdTimestamp").ascending();
            default -> Sort.unsorted();
        };
        int pageOrDefault = (page != null) ? page : 0;
        int sizeOrDefault = (size != null) ? size : 10;
        Page<AdvertisementDTO> pageResult = advertisementService.findAdvertisements(filter, sort, pageOrDefault, sizeOrDefault);
        List<AdvertisementDTO> list = pageResult.getContent();
        long totalAdvertisements = pageResult.getTotalElements();

        return ResponseUtil.ok("Total advertisement = " + totalAdvertisements, list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get advertisement by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<ResponseBody<AdvertisementDTO>> getAdvertisementById(@PathVariable Long id) {
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
    public ResponseEntity<ResponseBody<AdvertisementDTO>> createAdvertisement(
            @RequestPart("payload") AdvertisementDTO advertisementDTO,
            @RequestPart("images") List<MultipartFile> images
    ) throws IOException {
        AdvertisementDAO advertisement = advertisementService.createAdvertisement(advertisementDTO, images);
        return ResponseUtil.created(advertisementMapper.toDTO(advertisement));
    }

    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update an advertisement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<ResponseBody<AdvertisementDTO>> changeAdvertisement(
            @RequestPart("payload") AdvertisementDTO updated,
            @RequestPart("images") List<MultipartFile> images) {
        AdvertisementDAO advertisement = advertisementService.updateAdvertisement(updated, images);
        return ResponseUtil.ok("The advertisement was updated.", advertisementMapper.toDTO(advertisement));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<ResponseBody<Void>> deleteAdvertisement(@PathVariable Long id) {
        advertisementService.removeAdvertisement(id);
        return ResponseUtil.ok("The advertisement was deleted.");
    }

    @GetMapping("/favorite")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get all favorites")
    public ResponseEntity<ResponseBody<Set<AdvertisementDTO>>> getAllFavorites() {
        Set<AdvertisementDAO> favorites = advertisementService.getAllFavorites();
        return ResponseUtil.ok(advertisementMapper.toDtoSet(favorites));
    }

    @PostMapping("/favorite/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Add an advertisement to favorite list")
    public ResponseEntity<ResponseBody<AdvertisementDTO>> addToFavoriteList(@PathVariable Long id) {
        AdvertisementDAO advertisement = advertisementService.addToFavoriteList(id);
        return ResponseUtil.created(advertisementMapper.toDTO(advertisement));
    }

    @DeleteMapping("/favorite/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Remove the advertisement from favorite list")
    public ResponseEntity<ResponseBody<Void>> removeFromFavoriteList(@PathVariable Long id) {
        advertisementService.removeFromFavoriteList(id);
        return ResponseUtil.ok("The advertisement was removed from your favorite list.");
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Show user's advertisement")
    public ResponseEntity<?> getUserAdvertisement() {
        List<AdvertisementDTO> advertisement = advertisementService.getUserAdvertisement();
        return ResponseUtil.ok(advertisement);
    }

    @PostMapping("/enable/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Enable user's advertisement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Conflict")
    })
    public ResponseEntity<ResponseBody<Void>> enableAdvertisement(@PathVariable Long id) {
        advertisementService.enableAdvertisement(id);
        return ResponseUtil.ok("Advertisement is enabled");
    }

    @PostMapping("/disable/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Disable user's advertisement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Conflict")
    })
    public ResponseEntity<ResponseBody<Void>> disableAdvertisement(@PathVariable Long id) {
        advertisementService.disableAdvertisement(id);
        return ResponseUtil.ok("Advertisement is disabled");
    }
}
