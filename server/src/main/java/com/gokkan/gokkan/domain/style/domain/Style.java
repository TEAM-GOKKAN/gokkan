package com.gokkan.gokkan.domain.style.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
public class Style {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "style_id")
	private Long id;

	@Size(max = 50)
	private String name;
}
