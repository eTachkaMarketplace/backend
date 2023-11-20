package com.sellbycar.marketplace.rest;

import com.sellbycar.marketplace.repository.model.Advertisement;
import com.sellbycar.marketplace.service.AdvertisementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/testing")
@RequiredArgsConstructor
@Tag(name = "ADV Library", description = "Endpoints for ADV")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @GetMapping("/test")
    @Operation(summary = "Login User")
    public void saveAdv(@RequestBody Advertisement adv){
        advertisementService.save(adv);
    }
}
