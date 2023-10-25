package com.telegrambot.animailsshelter.service;


import com.telegrambot.animailsshelter.model.Animal;
import com.telegrambot.animailsshelter.model.Photo;
import com.telegrambot.animailsshelter.repository.AnimalRepository;
import com.telegrambot.animailsshelter.repository.PhotoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Slf4j
@Service
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final AnimalRepository animalRepository;

    public PhotoService(PhotoRepository photoRepository, AnimalRepository animalRepository) {
        this.photoRepository = photoRepository;
        this.animalRepository = animalRepository;
    }
    @Value("photos")
    private String photosDir;

    public void uploadPhoto(Long animalId, MultipartFile photoFile) throws IOException {
        log.info( "uploadAvatar = OK!");
        Animal animal = animalRepository.getById(animalId);
        Path filePath = Path.of(photosDir, animal + "." + getExtensions(Objects.requireNonNull(photoFile.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = photoFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        Photo photo = new Photo();
        photo.setAnimal(animal);
        photo.setFilePath(filePath.toString());
        photo.setFileSize(photoFile.getSize());
        photo.setMediaType(photoFile.getContentType());
        photo.setData(photoFile.getBytes());
        photoRepository.save(photo);
    }
    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
        public Photo findPhoto(long animalId){
        return photoRepository.findPhotoByAnimalId(animalId);
        }
        public List<Photo> avatarPage(Integer page){
        log.info( "avatarPage = OK!");
        return photoRepository.findAll(PageRequest.of(page - 1, 3)).getContent();
    }
}
