package com.cloudrand.arcapi.repository;


import com.cloudrand.arcapi.api.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findByUser_UserId(Long userId);
}
