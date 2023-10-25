package com.telegrambot.animailsshelter.controller;

import com.telegrambot.animailsshelter.model.Photo;
import com.telegrambot.animailsshelter.service.PhotoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
public class PhotoController {
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PostMapping(value = "/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPhoto(@PathVariable long id,
                                            @RequestParam MultipartFile photo) throws IOException {
        photoService.uploadPhoto(id, photo);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/photo")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        Photo photo = photoService.findPhoto(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(photo.getMediaType()));
        headers.setContentLength(photo.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(photo.getData());
    }

    @GetMapping("/{id}/photo-from-file")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Photo photo = photoService.findPhoto(id);
        Path path = Path.of(photo.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream()) {
            response.setStatus(200);
            response.setContentType(photo.getMediaType());
            response.setContentLength((int) photo.getFileSize());
            is.transferTo(os);
        }
    }
    //Метод для узнавания размера фотографии
    @GetMapping("photo/page/{page}")
    public List<Photo> photoPage (@PathVariable Integer page){
        return photoService.avatarPage(page);
    }
}
