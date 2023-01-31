package com.gokkan.gokkan.domain.expertComment.repository;

import com.gokkan.gokkan.domain.expertComment.domain.ExpertComment;
import com.gokkan.gokkan.domain.expertInfo.domain.ExpertInfo;
import com.gokkan.gokkan.domain.item.domain.Item;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertCommentRepository extends JpaRepository<ExpertComment, Long> {


	List<ExpertComment> findAllByExpertInfo(ExpertInfo expertInfo);

	boolean existsByExpertInfoAndItem(ExpertInfo expertInfo, Item item);

	ExpertComment findByExpertInfoAndItem(ExpertInfo expertInfo, Item item);

	Optional<ExpertComment> findByItem(Item item);
}
