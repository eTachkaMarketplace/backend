package com.sellbycar.marketplace.service;

import com.sellbycar.marketplace.persistance.AdvertisementRepository;
import com.sellbycar.marketplace.persistance.model.Advertisement;
import com.sellbycar.marketplace.persistance.model.User;
import com.sellbycar.marketplace.rest.dto.AdvertisementDTO;
import com.sellbycar.marketplace.rest.exception.UserDataException;
import com.sellbycar.marketplace.rest.mapper.AdvertisementMapper;
import com.sellbycar.marketplace.service.jwt.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertisementService {

    private final JwtUtils jwtUtils;
    private final AdvertisementRepository advertisementRepository;
    private final UserService userService;
    private final AdvertisementMapper advertisementMapper;

    private String getTokenFromRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getRequest();
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    @Transactional
    public List<AdvertisementDTO> findAllAd() {
        List<Advertisement> advertisements = advertisementRepository.findAll();
        return advertisements.stream()
                .map(advertisementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AdvertisementDTO getAd(Long id) {
        Optional<Advertisement> ad = advertisementRepository.findById(id);
        if (ad.isPresent()) {
            return advertisementMapper.toDTO(ad.get());
        }
        //TODO
        throw new UserDataException("ADv with ID " + id + " not found");
    }

    @Transactional
    public void saveNewAd(AdvertisementDTO advertisementDTO) {
        Advertisement advertisement = advertisementMapper.toModel(advertisementDTO);
        User user = userService.getUserFromSecurityContextHolder();
        advertisement.setUser(user);
        advertisementRepository.save(advertisement);
    }

    public AdvertisementDTO updateADv(AdvertisementDTO advertisementDTO, Long id) {
        User user = userService.getUserFromSecurityContextHolder();

        // Отримати існуючий об'єкт з бази даних
        Advertisement existingAd = advertisementRepository.findById(id)
                .orElseThrow(() -> new UserDataException("Ad with ID " + id + " not found"));

        // Перевірка, чи користувач, який оновлює оголошення, є його власником
        if (user.getId().equals(existingAd.getUser().getId())) {
            // Оновлення полів існуючого об'єкта
            existingAd.setDescription(advertisementDTO.getDescription());
            // Оновіть інші поля за потреби

            advertisementRepository.save(existingAd);  // Збережіть оновлений об'єкт
            return advertisementMapper.toDTO(existingAd);
        } else {
            throw new UserDataException("You don't have permission to update this ad");
        }
    }



}
