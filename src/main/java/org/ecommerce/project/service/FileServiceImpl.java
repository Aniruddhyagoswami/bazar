package org.ecommerce.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {


    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();

        // Sanitize filename and generate unique name
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId + originalFilename.substring(originalFilename.lastIndexOf('.'));

        // Create directory if it doesn't exist
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs(); // create full directory path
        }

        // Use Paths.get to construct safe path
        String filePath = Paths.get(path, fileName).toString();

        // Upload the file
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName;
    }
}
