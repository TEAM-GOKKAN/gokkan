package com.gokkan.gokkan.domain.image.domain;

import com.gokkan.gokkan.domain.item.domain.Item;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class ImageCheck {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Image_check")
	private Long id;

	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	@ToString.Exclude
	private Item item;
}
