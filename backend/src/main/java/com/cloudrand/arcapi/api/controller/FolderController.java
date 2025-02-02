package com.cloudrand.arcapi.api.controller;

import com.cloudrand.arcapi.api.model.Folder;
import com.cloudrand.arcapi.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {
    private final FolderService folderService;

    @PostMapping("/create")
    public ResponseEntity<Folder> createFolder(@RequestBody Folder folder) {
        return ResponseEntity.ok(folderService.createFolder(folder));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Folder>> getUserFolders(@PathVariable Long userId) {
        return ResponseEntity.ok(folderService.getUserFolders(userId));
    }
}
