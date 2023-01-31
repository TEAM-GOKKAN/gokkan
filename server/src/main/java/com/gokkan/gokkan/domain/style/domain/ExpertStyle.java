package com.gokkan.gokkan.domain.style.domain;

import com.gokkan.gokkan.domain.expertInfo.domain.ExpertInfo;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ExpertStyle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "expert_style_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "expert_info_id")
	@ToString.Exclude
	private ExpertInfo expertInfo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "style_id")
	private Style style;

	@Size(max = 50)
	private String styleName;

	@Builder
	public ExpertStyle(ExpertInfo expertInfo, Style style) {
		this.expertInfo = expertInfo;
		this.style = style;
		this.styleName = style.getName();
	}
}