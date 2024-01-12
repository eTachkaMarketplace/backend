package com.sellbycar.marketplace.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    public ImageDAO createImage(MultipartFile multipartFile) throws IOException {
        ImageDAO image = new ImageDAO();
        image.setName(multipartFile.getName());
        image.setContentType(multipartFile.getContentType());
        image.setSize(multipartFile.getSize());
        image.setContent(multipartFile.getBytes());
        image.setCreatedTimestamp(Instant.now());
        return imageRepository.save(image);
    }

    @Override
    public Optional<ImageDAO> getImageById(Long id) {
        return imageRepository.findById(id);
    }
}
