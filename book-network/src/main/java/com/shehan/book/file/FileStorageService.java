package com.shehan.book.file;

import jakarta.annotation.Nonnull;
import org.springframework.web.multipart.MultipartFile;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    @Value("${application.file.upload.photos-output-path}")
    private String fileUploadPath;

    public String saveFile(
            @Nonnull MultipartFile sourceFile,
            @Nonnull Long userId) {

        final String fileUploadSubPath = "users" + File.separator + userId;

        return uploadFile(sourceFile, fileUploadSubPath);
    }

    private String uploadFile(
            @NonNull MultipartFile sourceFile,
            @Nonnull String fileUploadSubPath) {

        final String finalUploadPath = fileUploadPath + File.separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);
        if(!targetFolder.exists()){
            boolean folderCreated = targetFolder.mkdirs();
            if(!folderCreated){
                log.info("Failed to create target folder.");
                return null;
            }

            final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
            String targetFilePath = fileUploadPath + File.separator
                    + System.currentTimeMillis()
                    + "."
                    + fileExtension;

            Path targetPath = Paths.get(targetFilePath);
            try{
                Files.write(targetPath,sourceFile.getBytes());
                log.info("File saved");
                return targetFilePath;
            } catch (IOException e) {
                log.info("File not saved");
            }
            return null;
        }
        return null;
    }

    private String getFileExtension(String originalFilename) {
        if(originalFilename == null || originalFilename.isEmpty()){
            return "";
        }
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if(lastDotIndex == -1){
            return "";
        }
        return originalFilename.substring(lastDotIndex+1).toLowerCase();
    }
}
