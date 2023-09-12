package by.belyahovich.task1backendproject.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImageToFileSystem(MultipartFile multipartFile);
    byte[] downloadImageFromFileSystem(String filename);
}
