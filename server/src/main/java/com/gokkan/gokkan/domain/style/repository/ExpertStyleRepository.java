package com.gokkan.gokkan.domain.style.repository;

import com.gokkan.gokkan.domain.expertInfo.domain.ExpertInfo;
import com.gokkan.gokkan.domain.style.domain.ExpertStyle;
import com.gokkan.gokkan.domain.style.domain.Style;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertStyleRepository extends JpaRepository<ExpertStyle, Long> {

	List<ExpertStyle> findAllByExpertInfo(ExpertInfo expertInfo);

	Optional<ExpertStyle> findByExpertInfoAndStyle(ExpertInfo expertInfo, Style style);
}