package com.gokkan.gokkan.domain.category.repository;

import com.gokkan.gokkan.domain.category.domain.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByName(String name);

	boolean existsByName(String name);
}
