package com.cloudrand.arcapi.service;

import com.cloudrand.arcapi.api.model.*;
import com.cloudrand.arcapi.repository.FileRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    
    public List<File> getUserFiles(User user) {
        return fileRepository.findByUser(user);
    }
}
