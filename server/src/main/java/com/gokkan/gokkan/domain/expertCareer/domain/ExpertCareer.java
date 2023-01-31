package com.gokkan.gokkan.domain.expertCareer.domain;

import com.gokkan.gokkan.domain.expertInfo.domain.ExpertInfo;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpertCareer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "expert_career_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "expert_info_id")
	private ExpertInfo expertInfo;

	private LocalDate startDate;
	private LocalDate endDate;
	private String companyName;
	private String position;

	@Builder
	public ExpertCareer(ExpertInfo expertInfo, LocalDate startDate, LocalDate endDate,
		String companyName,
		String position) {
		this.expertInfo = expertInfo;
		this.startDate = startDate;
		this.endDate = endDate;
		this.companyName = companyName;
		this.position = position;
	}

	public void update(LocalDate startDate, LocalDate endDate, String companyName,
		String position) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.companyName = companyName;
		this.position = position;
	}
}
