package com.gokkan.gokkan.domain.item.service;

import static com.gokkan.gokkan.domain.item.dto.ItemDto.Response;

import com.gokkan.gokkan.domain.category.domain.Category;
import com.gokkan.gokkan.domain.category.service.CategoryService;
import com.gokkan.gokkan.domain.image.domain.ImageCheck;
import com.gokkan.gokkan.domain.image.domain.ImageItem;
import com.gokkan.gokkan.domain.image.repository.ImageCheckRepository;
import com.gokkan.gokkan.domain.image.repository.ImageItemRepository;
import com.gokkan.gokkan.domain.image.service.AwsS3Service;
import com.gokkan.gokkan.domain.image.service.ImageCheckService;
import com.gokkan.gokkan.domain.image.service.ImageItemService;
import com.gokkan.gokkan.domain.item.domain.Item;
import com.gokkan.gokkan.domain.item.dto.ItemDto.ListResponse;
import com.gokkan.gokkan.domain.item.dto.ItemDto.ResponseForAuction;
import com.gokkan.gokkan.domain.item.dto.ItemDto.UpdateRequest;
import com.gokkan.gokkan.domain.item.exception.ItemErrorCode;
import com.gokkan.gokkan.domain.item.repository.ItemRepository;
import com.gokkan.gokkan.domain.item.type.State;
import com.gokkan.gokkan.domain.member.domain.Member;
import com.gokkan.gokkan.domain.member.exception.MemberErrorCode;
import com.gokkan.gokkan.domain.style.domain.StyleItem;
import com.gokkan.gokkan.domain.style.repository.StyleItemRepository;
import com.gokkan.gokkan.domain.style.service.StyleItemService;
import com.gokkan.gokkan.global.exception.exception.RestApiException;
import com.gokkan.gokkan.global.security.oauth.entity.Role;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;
	private final StyleItemRepository styleItemRepository;
	private final ImageItemRepository imageItemRepository;
	private final ImageCheckRepository imageCheckRepository;

	private final CategoryService categoryService;
	private final StyleItemService styleItemService;
	private final ImageItemService imageItemService;
	private final ImageCheckService imageCheckService;
	private final AwsS3Service awsS3Service;

	@Transactional
	public Response create(
		UpdateRequest request,
		List<MultipartFile> imageItemFiles,
		List<MultipartFile> imageCheckFiles,
		Member member) {
		log.info("create item id : " + request.getItemId());
		Item item = updateItem(request, imageItemFiles, imageCheckFiles, member, true);
		item.setState(State.ASSESSING);
		return Response.toResponse(itemRepository.save(item));
	}

	@Transactional(readOnly = true)
	public Response readDetail(Long itemId) {
		log.info("readDetail item id : " + itemId);
		Item item = getItem(itemId);
		itemStateCheckForRead(item.getState(),
			new ArrayList<>(List.of(State.COMPLETE, State.ASSESSING)));
		return Response.toResponse(item);
	}

	@Transactional(readOnly = true)
	public Response readDetailTemp(Long itemId, Member member) {
		log.info("readTempDetail item id : " + itemId);
		Item item = getItem(itemId);
		memberLoginCheck(member);
		memberMatchCheck(member.getUserId(), item.getMember().getUserId());
		itemStateCheckForRead(item.getState(),
			new ArrayList<>(List.of(State.TEMPORARY, State.RETURN)));
		return Response.toResponse(item);
	}

	@Transactional(readOnly = true)
	public ResponseForAuction readDetailAuction(Long itemId) {
		log.info("readAuctionDetail item id : " + itemId);
		Item item = getItem(itemId);
		itemStateCheckForRead(item.getState(),
			new ArrayList<>(List.of(State.COMPLETE)));
		return ResponseForAuction.toResponseForAuction(item);
	}

	@Transactional
	public boolean delete(Long itemId, Member member) {
		log.info("delete item id : " + itemId);
		memberLoginCheck(member);
		Item item = getItem(itemId);
		memberMatchCheck(member.getUserId(), item.getMember().getUserId());
		itemStateCheckForUpdateAndDelete(item.getState());
		imageItemService.deleteAllImageItems(item.getImageItems());
		imageCheckService.deleteAllImageItems(item.getImageChecks());
		itemRepository.delete(item);
		return true;
	}

	@Transactional
	public Response update(
		UpdateRequest request,
		List<MultipartFile> imageItemFiles,
		List<MultipartFile> imageCheckFiles,
		Member member) {
		log.info("update item id : " + request.getItemId());
		return Response.toResponse(
			itemRepository.save(
				updateItem(request, imageItemFiles, imageCheckFiles, member, false)));
	}

	public Long createTemporary(Member member) {
		memberLoginCheck(member);
		log.info("createTemporary member id : " + member.getId());
		return itemRepository.save(
				Item.builder()
					.name("")
					.member(member)
					.material("")
					.conditionGrade("")
					.conditionDescription("")
					.text("")
					.designer("")
					.brand("")
					.state(State.TEMPORARY)
					.created(LocalDateTime.now())
					.updated(LocalDateTime.now())
					.imageItems(new ArrayList<>())
					.imageChecks(new ArrayList<>())
					.styleItems(new ArrayList<>())
					.build())
			.getId();
	}

	@Transactional(readOnly = true)
	public Page<ListResponse> myItems(Member member, List<State> states, Pageable pageable) {
		log.info("myItems member id : " + member.getUserId());

		Page<ListResponse> result = itemRepository.searchAllMyItem(states, member, pageable);
		log.info("myItems : getTotalElements : " + result.getTotalElements());
		log.info("myItems : getTotalPages : " + result.getTotalPages());
		log.info("myItems : getContent.size : " + result.getContent().size());
		return result;
	}

	@Transactional(readOnly = true)
	public Page<ListResponse> itemsForExport(Member member, Pageable pageable) {
		log.info("itemsForExport member id : " + member.getUserId());
		if (!member.getRole().equals(Role.ADMIN)) {
			throw new RestApiException(MemberErrorCode.MEMBER_FORBIDDEN);
		}
		Page<ListResponse> result = itemRepository.searchAllItemForExport(member, pageable);
		log.info("itemsForExport : getTotalElements : " + result.getTotalElements());
		log.info("itemsForExport : getTotalPages : " + result.getTotalPages());
		log.info("itemsForExport : getContent.size : " + result.getContent().size());
		return result;
	}

	private void memberMatchCheck(String memberId, String itemMemberId) {
		log.info("memberMatchCheck member id : " + memberId);
		if (!memberId.equals(itemMemberId)) {
			log.error("memberMatchCheck member id : " + memberId);
			throw new RestApiException(MemberErrorCode.MEMBER_MISMATCH);
		}
	}

	private void memberLoginCheck(Member member) {
		if (member == null) {
			log.error("memberLoginCheck");
			throw new RestApiException(MemberErrorCode.MEMBER_NOT_LOGIN);
		} else {
			log.info("member id : " + member.getId());
		}
	}

	private void itemSaveCompleteCategoryCheck(String category) {
		log.info("saveItemRelations category name : " + category);
		if (category.equals("")) {
			throw new RestApiException(ItemErrorCode.CATEGORY_NOT_NUL);
		}
	}

	private void itemSaveCompleteStyleCheck(int styleItems) {
		if (styleItems == 0) {
			log.error("itemSaveCompleteStyleCheck styleItems count : " + styleItems);
			throw new RestApiException(ItemErrorCode.STYLE_NOT_NULL);
		}
	}

	private void itemStateCheckForUpdateAndDelete(State state) {
		if (state == State.COMPLETE || state == State.ASSESSING) {
			log.error("itemStateCheckForUpdateAndDelete state : " + state.getDescription());
			throw new RestApiException(ItemErrorCode.CAN_NOT_FIX_STATE);
		}
	}

	private void itemStateCheckForRead(State itemState, List<State> states) {
		for (State state : states) {
			if (state == itemState) {
				return;
			}
		}
		log.error("itemStateCheckForRead state : " + itemState.getDescription());
		throw new RestApiException(ItemErrorCode.CAN_NOT_READ_STATE);
	}

	private Item updateItem(
		UpdateRequest request,
		List<MultipartFile> imageItemFiles,
		List<MultipartFile> imageCheckFiles,
		Member member,
		boolean create
	) {
		memberLoginCheck(member);
		log.info("login member id : " + member.getId());
		Item item = getItem(request.getItemId());
		memberMatchCheck(member.getUserId(), item.getMember().getUserId());
		itemStateCheckForUpdateAndDelete(item.getState());
		if (create) {
			itemSaveCompleteCategoryCheck(request.getCategory());
		}
		checkImageFiles(imageItemFiles, imageCheckFiles,
			request.getImageItemUrls().size(), request.getImageCheckUrls().size());

		Category category = categoryService.getCategory(request.getCategory());
		List<StyleItem> styleItems = styleItemService.createNotDuplicate(request.getStyles(),
			item.getStyleItems());
		if (create) {
			itemSaveCompleteStyleCheck(styleItems.size());
		}
		List<ImageItem> imageItems = imageItemService.create(awsS3Service.save(imageItemFiles));
		List<ImageCheck> imageChecks = imageCheckService.create(awsS3Service.save(imageCheckFiles));

		item = request.toItem(item, category);
		item.setImageItems(
			imageItemService.checkImageItemDeleted(
				request.getImageItemUrls(),
				item.getImageItems()));
		item.setImageChecks(
			imageCheckService.checkImageCheckDeleted(
				request.getImageCheckUrls(),
				item.getImageChecks()));

		saveItemRelations(imageItems, imageChecks, styleItems, item);

		item.setThumbnail(
			item.getImageItems().size() == 0 ? "" : item.getImageItems().get(0).getUrl());
		item.setUpdated(LocalDateTime.now());
		return item;
	}

	private void checkImageFiles(
		List<MultipartFile> imageItemFiles,
		List<MultipartFile> imageCheckFiles,
		int imageItemsUrlsCount,
		int imageCheckUrlsCount) {
		log.info("checkImageFiles");
		awsS3Service.checkImageCount(imageItemFiles, imageItemsUrlsCount);
		awsS3Service.checkImageCount(imageCheckFiles, imageCheckUrlsCount);
		awsS3Service.check(imageItemFiles);
		awsS3Service.check(imageCheckFiles);
	}

	private Item getItem(Long itemId) {
		log.info("getItem item id : " + itemId);
		return itemRepository.findById(itemId)
			.orElseThrow((() -> {
				log.error("getItem item id : " + itemId);
				return new RestApiException(ItemErrorCode.NOT_FOUND_ITEM);
			}));
	}

	private void saveItemRelations(
		List<ImageItem> imageItems,
		List<ImageCheck> imageChecks,
		List<StyleItem> styleItems,
		Item item) {
		log.info("saveItemRelations");
		item.addStyleItem(styleItems);
		item.addImageItems(imageItems);
		item.addImageChecks(imageChecks);

		styleItemRepository.saveAll(styleItems);
		imageItemRepository.saveAll(imageItems);
		imageCheckRepository.saveAll(imageChecks);
	}
}
