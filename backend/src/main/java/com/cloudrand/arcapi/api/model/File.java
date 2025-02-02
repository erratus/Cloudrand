package com.cloudrand.arcapi.api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "files")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String fileName;
    private String filePath;
    private Long fileSize;

    @ManyToOne
    @JoinColumn(name = "folder_id", nullable = true)
    private Folder folder;
}

