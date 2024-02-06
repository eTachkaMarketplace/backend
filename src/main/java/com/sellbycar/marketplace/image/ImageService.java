package com.sellbycar.marketplace.image;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface ImageService {

    /**
     * Creates an Image entity from a multipart file.
     *
     * @param multipartFile The MultipartFile representing the image file.
     * @return The created Image entity.
     */
    ImageDAO createImage(MultipartFile multipartFile);

    /**
     * Retrieves an Image entity by its unique identifier.
     *
     * @param id The unique identifier of the image.
     * @return An Optional containing the Image entity if found, or empty if not found.
     */
    Optional<ImageDAO> getImageById(Long id);
}
