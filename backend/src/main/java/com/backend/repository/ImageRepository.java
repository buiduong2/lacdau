package com.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entities.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByProductDetailId(long detailId);
}
