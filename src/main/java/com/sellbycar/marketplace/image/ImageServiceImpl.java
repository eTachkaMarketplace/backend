package com.sellbycar.marketplace.image;

import com.sellbycar.marketplace.ad.AdvertisementDAO;
import com.sellbycar.marketplace.ad.AdvertisementRepository;
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
    public ImageDAO createImage(MultipartFile multipartFile) throws IOException {
        ImageDAO image = new ImageDAO();
        image.setName(multipartFile.getName());
        image.setContentType(multipartFile.getContentType());
        image.setSize(multipartFile.getSize());
        image.setResource(multipartFile.getBytes());
        return imageRepository.save(image);
    }

    @Override
    public Optional<ImageDAO> getImageById(Long id) {
        return imageRepository.findById(id);
    }

    @Override
    @Transactional
    public void addPreviewImageToAdvertisement(Long advertisementID, Long imageId) {
        AdvertisementDAO advertisement = advertisementRepository.getReferenceById(advertisementID);
        ImageDAO image = imageRepository.getReferenceById(imageId);

        image.setPreviewImage(true);
        image.setAdvertisement(advertisement);

        imageRepository.save(image);
        advertisementRepository.save(advertisement);
    }
}
