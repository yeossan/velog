package org.example.velogproject.service;

import lombok.AllArgsConstructor;
import org.example.velogproject.domain.Image;
import org.example.velogproject.repository.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ImageService {

    private ImageRepository imageRepository;

    public Image save(Image image) {
        return imageRepository.save(image);
    }

    public List<Image> findAll() {
        return imageRepository.findAll();
    }

    public void saveAll(List<Image> images) {
        imageRepository.saveAll(images);
    }
}
