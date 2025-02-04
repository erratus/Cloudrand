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
    @Column(name = "fileId")
    private Long fileId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "folderId", nullable = true)
    private Folder folder;

    @Column(name = "fileName", nullable = false)
    private String fileName;
    @Column(name = "filePath",nullable = false)
    private String filePath;
    @Column(name = "fileSize")
    private Long fileSize;
    @Column(name = "version")
    private Integer version; // Add a version field

}

