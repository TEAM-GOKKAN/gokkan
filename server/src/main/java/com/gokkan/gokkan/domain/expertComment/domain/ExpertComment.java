package com.gokkan.gokkan.domain.expertComment.domain;

import com.gokkan.gokkan.domain.expertInfo.domain.ExpertInfo;
import com.gokkan.gokkan.domain.item.domain.Item;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpertComment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "expert_comment_id")
	private Long id;

	private String comment;
	private Long minPrice;
	private Long maxPrice;

	@JoinColumn(name = "expert_info_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ExpertInfo expertInfo;

	@JoinColumn(name = "item_id")
	@OneToOne(fetch = FetchType.LAZY)
	private Item item;

	@Builder
	public ExpertComment(String comment, Long minPrice, Long maxPrice, ExpertInfo expertInfo,
		Item item) {
		this.comment = comment;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.expertInfo = expertInfo;
		this.item = item;
	}

	public void update(String comment, Long minPrice, Long maxPrice) {
		this.comment = comment;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
	}
}
