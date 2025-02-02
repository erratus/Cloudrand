package com.cloudrand.arcapi.repository;
import java.util.List;
import com.cloudrand.arcapi.api.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByUser(User user);
}
