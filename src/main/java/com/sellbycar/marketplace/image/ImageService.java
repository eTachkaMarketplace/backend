package com.sellbycar.marketplace.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface ImageService {

    /**
     * Creates an Image entity from a multipart file.
     *
     * @param multipartFile The MultipartFile representing the image file.
     * @return The created Image entity.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    ImageDAO createImage(MultipartFile multipartFile) throws IOException;

    /**
     * Retrieves an Image entity by its unique identifier.
     *
     * @param id The unique identifier of the image.
     * @return An Optional containing the Image entity if found, or empty if not found.
     */
    Optional<ImageDAO> getImageById(Long id);

    /**
     * Adds an image as a preview to an advertisement.
     *
     * @param advertisementID The identifier of the advertisement to which the preview should be added.
     * @param imageId         The identifier of the image to be set as the preview.
     */
    void addPreviewImageToAdvertisement(Long advertisementID, Long imageId);
}
