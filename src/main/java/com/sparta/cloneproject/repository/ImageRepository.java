package com.sparta.cloneproject.repository;

import com.sparta.cloneproject.model.Img;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Img,Long> {
}
