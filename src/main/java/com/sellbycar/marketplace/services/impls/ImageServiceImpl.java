package com.sellbycar.marketplace.services.impls;

import com.sellbycar.marketplace.models.entities.Advertisement;
import com.sellbycar.marketplace.models.entities.Image;
import com.sellbycar.marketplace.repositories.AdvertisementRepository;
import com.sellbycar.marketplace.repositories.ImageRepository;
import com.sellbycar.marketplace.services.ImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final AdvertisementRepository advertisementRepository;

    @Override
    public Image createImage(MultipartFile multipartFile) throws IOException {
        Image image = new Image();
        image.setName(multipartFile.getName());
        image.setContentType(multipartFile.getContentType());
        image.setSize(multipartFile.getSize());
        image.setResource(multipartFile.getBytes());
        return imageRepository.save(image);
    }

    @Override
    public Optional<Image> getImageById(Long id) {
        return imageRepository.findById(id);
    }

    @Override
    @Transactional
    public void addPreviewImageToAdvertisement(Long advertisementID, Long imageId) {
        Advertisement advertisement = advertisementRepository.getReferenceById(advertisementID);
        Image image = imageRepository.getReferenceById(imageId);

        image.setPreviewImage(true);
        image.setAdvertisement(advertisement);

        imageRepository.save(image);
        advertisementRepository.save(advertisement);
    }
}
