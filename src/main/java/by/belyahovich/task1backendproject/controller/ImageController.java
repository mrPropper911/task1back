package by.belyahovich.task1backendproject.controller;

import by.belyahovich.task1backendproject.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImageToFileSystem(@RequestParam("image") MultipartFile multipartFile) {
        String uploadImagePath = imageService.uploadImageToFileSystem(multipartFile);
        return ResponseEntity.ok(uploadImagePath);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> downloadImageFromFileSystem(@PathVariable String fileName){
        byte[] imageData = imageService.downloadImageFromFileSystem(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

}
