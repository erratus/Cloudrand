package com.cloudrand.arcapi.service;

import com.cloudrand.arcapi.api.model.Folder;
import com.cloudrand.arcapi.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;

    public Folder createFolder(Folder folder) {
        return folderRepository.save(folder);
    }

    public List<Folder> getUserFolders(Long userId) {
        return folderRepository.findByUser_UserId(userId);
    }
}
