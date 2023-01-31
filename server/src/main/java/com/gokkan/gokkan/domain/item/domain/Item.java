package com.gokkan.gokkan.domain.item.domain;

import com.gokkan.gokkan.domain.category.domain.Category;
import com.gokkan.gokkan.domain.image.domain.ImageCheck;
import com.gokkan.gokkan.domain.image.domain.ImageItem;
import com.gokkan.gokkan.domain.item.type.State;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.style.domain.StyleItem;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 100)
	private String name;

	private long startPrice;

	@Enumerated(EnumType.STRING)
	private State state;

	private long width;
	private long depth;
	private long height;
	@Size(max = 30)
	private String material;

	@Size(max = 30)
	private String conditionGrade;
	@Size(max = 2000)
	private String conditionDescription;
	@Size(max = 2000)
	private String text;

	@Size(max = 30)
	private String designer;
	@Size(max = 30)
	private String brand;
	private int productionYear;
	private String thumbnail;

	private LocalDateTime created;
	private LocalDateTime updated;


	@ManyToOne(fetch = FetchType.LAZY)
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	@BatchSize(size = 11)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.REMOVE)
	private List<ImageCheck> imageChecks;

	@BatchSize(size = 11)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.REMOVE)
	private List<ImageItem> imageItems;

	@BatchSize(size = 11)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.REMOVE)
	private List<StyleItem> styleItems;

	public void addImageItems(List<ImageItem> imageItems) {
		for (ImageItem imageItem : imageItems) {
			imageItem.setItem(this);
		}
		this.getImageItems().addAll(imageItems);
	}

	public void addImageChecks(List<ImageCheck> imageChecks) {
		for (ImageCheck imageCheck : imageChecks) {
			imageCheck.setItem(this);
		}
		this.getImageChecks().addAll(imageChecks);
	}

	public void addStyleItem(List<StyleItem> styleItems) {
		for (StyleItem styleItem : styleItems) {
			styleItem.setItem(this);
		}
		this.setStyleItems(styleItems);
	}
}
