package com.cloudrand.arcapi.service;

import com.cloudrand.arcapi.api.model.File;
import com.cloudrand.arcapi.api.model.User;
import com.cloudrand.arcapi.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    // Directory where files will be stored (adjust path as needed)
    private final Path rootLocation = Paths.get("uploaded-files");

    // Upload a file
    public File uploadFile(MultipartFile file, User user, Long folderId) throws IOException {
        // Check if the directory exists, if not create it
        if (!Files.exists(rootLocation)) {
            Files.createDirectories(rootLocation);
        }

        // Create the file path (name will be unique based on timestamp)
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path destinationPath = rootLocation.resolve(fileName);
        
        // Save the file to the disk
        Files.copy(file.getInputStream(), destinationPath);

        // Create a File object to store in the database
        File dbFile = new File();
        dbFile.setUser(user);
        dbFile.setFileName(fileName);
        dbFile.setFilePath(destinationPath.toString());
        dbFile.setFileSize(file.getSize());

        // Optionally, associate the file with a folder (if folderId is provided)
        if (folderId != null) {
            // Retrieve the folder from the DB and set it
            // Folder folder = folderService.getFolderById(folderId); // You need to implement folder service
            // dbFile.setFolder(folder);
        }

        // Save file info to the database
        return fileRepository.save(dbFile);
    }

    // Download a file
    public File downloadFile(Long fileId) {
        Optional<File> file = fileRepository.findById(fileId);
        return file.orElseThrow(() -> new RuntimeException("File not found"));
    }

    // Delete a file
    public void deleteFile(Long fileId) throws IOException {
        Optional<File> file = fileRepository.findById(fileId);
        if (file.isPresent()) {
            Path filePath = Paths.get(file.get().getFilePath());
            Files.delete(filePath);  // Delete the file from the disk
            fileRepository.delete(file.get());  // Delete the file entry from the database
        } else {
            throw new RuntimeException("File not found");
        }
    }

    // Share a file (e.g., generating a URL or a shareable link)
    public String generateShareLink(Long fileId) {
        Optional<File> file = fileRepository.findById(fileId);
        if (file.isPresent()) {
            // Generate a link based on the file's ID (you can customize this as needed)
            return "http://localhost:8080/files/download/" + fileId;
        }
        throw new RuntimeException("File not found");
    }

    
 // Update file metadata
    public File updateFileMetadata(Long fileId, File updatedFile) {
        Optional<File> existingFile = fileRepository.findById(fileId);
        if (existingFile.isPresent()) {
            File file = existingFile.get();
            file.setFileName(updatedFile.getFileName()); // Update file name
            file.setFolder(updatedFile.getFolder()); // Update folder if provided
            // You can add other fields to update as needed
            return fileRepository.save(file); // Save updated file metadata
        }
        throw new RuntimeException("File not found");
    }

    // Get files by user
    public List<File> getFilesByUser(User user) {
        return fileRepository.findByUser(user);
    }
    
    
    public List<File> getFileVersionHistory(Long fileId) {
        return fileRepository.findByFileIdOrderByVersionAsc(fileId); // Assuming versioning is implemented
    }
}
