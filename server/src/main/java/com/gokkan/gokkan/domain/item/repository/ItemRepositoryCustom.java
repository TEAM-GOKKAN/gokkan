package com.gokkan.gokkan.domain.item.repository;

import com.gokkan.gokkan.domain.item.dto.ItemDto.ListResponse;
import com.gokkan.gokkan.domain.item.type.State;
import com.gokkan.gokkan.domain.member.domain.Member;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepositoryCustom {

	Page<ListResponse> searchAllItemForExport(Member member, Pageable pageable);

	Page<ListResponse> searchAllMyItem(List<State> state, Member member, Pageable pageable);
}
