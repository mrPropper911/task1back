package by.belyahovich.task1backendproject.model;

import lombok.extern.log4j.Log4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Log4j
public class FileUploadUtil {

    public static void saveFile (String uploadDir, String fileName, MultipartFile multipartFile){

        Path path = Path.of(uploadDir);

        if(!Files.exists(path)){
            try {
                Files.createDirectories(path);
            } catch (IOException e){
                log.error("CAN'T CREATE DIRECTORY FOR IMAGE " + e.toString());
            }
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = path.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e){
            log.error("COULD NOT SAVE IMAGE FILE: " + fileName + e.toString());
        }
    }
}
