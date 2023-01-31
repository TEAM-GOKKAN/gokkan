package com.gokkan.gokkan.domain.style.repository;

import com.gokkan.gokkan.domain.style.domain.StyleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StyleItemRepository extends JpaRepository<StyleItem, Long> {

}
