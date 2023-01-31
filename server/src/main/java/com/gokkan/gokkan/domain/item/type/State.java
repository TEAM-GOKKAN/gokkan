package com.gokkan.gokkan.domain.item.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum State {
	RETURN("반려"),
	ASSESSING("감정 대기"),
	COMPLETE("감정 완료"),
	TEMPORARY("임시 저장"),
	;

	private final String description;
}
