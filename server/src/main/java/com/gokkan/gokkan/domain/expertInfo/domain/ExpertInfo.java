package com.gokkan.gokkan.domain.expertInfo.domain;

import com.gokkan.gokkan.domain.expertCareer.domain.ExpertCareer;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.style.domain.ExpertStyle;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

@Entity
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpertInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "expert_info_id")
	private Long id;

	@JoinColumn(name = "member_id")
	@OneToOne(fetch = FetchType.LAZY)
	private Member member;

	@BatchSize(size = 10)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "expertInfo", cascade = CascadeType.REMOVE)
	private List<ExpertStyle> expertStyles = new ArrayList<>();

	@BatchSize(size = 10)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "expertInfo", cascade = CascadeType.REMOVE)
	private List<ExpertCareer> expertCareers = new ArrayList<>();

	private String name;
	private String info;

	@Builder
	public ExpertInfo(Member member, String name, String info) {
		this.member = member;
		this.name = name;
		this.info = info;
	}

	public void updateInfo(String info) {
		this.info = info;
	}

	public void addExpertStyle(ExpertStyle expertStyle) {
		this.expertStyles.add(expertStyle);
	}

	public void setExpertStyles(List<ExpertStyle> expertStyles) {
		this.expertStyles = expertStyles;
	}

	public void addExpertCareer(ExpertCareer expertCareer) {
		this.expertCareers.add(expertCareer);
	}
}
