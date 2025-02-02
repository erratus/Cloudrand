package com.cloudrand.arcapi.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "folders")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long folderId;

    @Column(nullable = false)
    private String folderName;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Folder belongs to a user

    @ManyToOne
    @JoinColumn(name = "parent_folder_id", nullable = true)
    private Folder parentFolder; // Nested folders

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
    private List<File> files; // List of files in this folder

    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL)
    private List<Folder> subfolders; // List of subfolders
}
