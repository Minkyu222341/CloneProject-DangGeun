package com.sparta.cloneproject.repository;

import com.sparta.cloneproject.model.Img;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Img,Long> {
    List<Img> findByFileName(String filename);

    boolean existsByFileName(String filename);

}
