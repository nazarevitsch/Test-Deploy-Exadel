package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {

}
