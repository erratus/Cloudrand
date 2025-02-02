package com.cloudrand.arcapi.api.controller;
import com.cloudrand.arcapi.service.*;

import java.util.List;
import com.cloudrand.arcapi.api.model.*;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<File>> getUserFiles(@PathVariable Long userId) {
        User user = new User();
        user.setUserId(userId);
        return ResponseEntity.ok(fileService.getUserFiles(user));
    }
}
