package by.belyahovich.task1backendproject.service.imageServiceImpl;

import by.belyahovich.task1backendproject.config.ResourceNotFoundException;
import by.belyahovich.task1backendproject.config.StorageException;
import by.belyahovich.task1backendproject.model.Image;
import by.belyahovich.task1backendproject.repository.ImageRepositoryJpa;
import by.belyahovich.task1backendproject.service.ImageService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.Objects;

@Log4j
@Service
public class ImageServiceImpl implements ImageService {

    @Value("${path.server}")
    private String rootLocationPath;

    private final ImageRepositoryJpa imageRepositoryJpa;

    public ImageServiceImpl(ImageRepositoryJpa imageRepositoryJpa) {
        this.imageRepositoryJpa = imageRepositoryJpa;
    }


    @Override
    public String uploadImageToFileSystem(MultipartFile multipartFile) {

        var fileName = new Date().getTime() + "-" + StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()))
                .toLowerCase().replaceAll(" ", "-");

        var filePath = rootLocationPath + fileName;

        //Security check
        if (fileName.contains("..")){
            log.info("TRYING SAVE IMAGE TO ANOTHER DIRECTORY");
            throw new StorageException("CANNOT STORE FILE WITH RELATIVE PATH OUTSIDE CURRENT DIRECTORY "
                    + fileName);
        } else if(multipartFile.isEmpty()){
            throw new StorageException("FAILED TO STORE EMPTY FILE " + fileName);
        }

        try {
            Image image = imageRepositoryJpa.save(Image.builder()
                    .name(fileName)
                    .type(multipartFile.getContentType())
                    .file_path(rootLocationPath).build());
            multipartFile.transferTo(new File(filePath));
            return "File upload successfully: " + fileName;
        } catch (IOException e) {
            log.error("ERROR WITH SAVING IMAGE ON NEW FILE SYSTEM" + e);
        }
        return null;
    }

    @Override
    public byte[] downloadImageFromFileSystem(String filename) {
        var fileData = imageRepositoryJpa.findByName(filename)
                .orElseThrow(() -> new ResourceNotFoundException("IMAGE WITH FILENAME " + filename + " NOT FOUND"));
        var filePath = fileData.getFile_path();
        try {
            return Files.readAllBytes(new File(filePath + filename).toPath());
        } catch (IOException e) {
            log.error("ERROR WITH READING IMAGE " + e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
