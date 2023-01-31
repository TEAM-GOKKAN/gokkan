import { atomsWithInfiniteQuery } from 'jotai-tanstack-query';
import { atom } from 'jotai';
import axios from 'axios';

const categoryAtom = atom('');
const stylesAtom = atom<string[]>([]);
const minPriceAtom = atom('');
const maxPriceAtom = atom('');
const sortAtom = atom('마감임박순');

// 초기 filter 조회용 atom
const filterInfoAtom = atom((get) => {
  const category = get(categoryAtom);
  const styles = get(stylesAtom);
  const minPrice = get(minPriceAtom);
  const maxPrice = get(maxPriceAtom);
  const sort = get(sortAtom);
  return {
    category,
    styles,
    minPrice,
    maxPrice,
    sort,
  };
});
// filter reset용 atom
const resetStoredFilterInfoAtom = atom(null, (get, set, data) => {
  set(stylesAtom, []);
  set(sortAtom, '마감임박순');
  set(minPriceAtom, '');
  set(maxPriceAtom, '');
});
// filter 설정용 atom
const setStoredFilterInfoAtom = atom(null, (get, set, data: FilterInfoType) => {
  set(sortAtom, data.sort);
  set(stylesAtom, data.styles);
  set(minPriceAtom, data.minPrice);
  set(maxPriceAtom, data.maxPrice);
});
// 마감임박 페이지용 쿼리
const [, endTimeAuctionItemListAtom] = atomsWithInfiniteQuery((get) => ({
  queryKey: ['auction', '마감임박순'],
  queryFn: async ({ queryKey: [name, sort], pageParam = 0 }) => {
    let url = `http://3.38.59.40:8080/api/v1/auction/filter-list?size=6&page=${pageParam}`;
    // url에 sort 추가해줌
    url += `&sort=${sort}`;
    // 최종적으로 해당 url로 요청을 보냄
    const page = await axios.get(url);
    return page;
  },
  // infinite queries can support paginated fetching
  getNextPageParam: (lastPage: any, pages) => {
    if (!lastPage?.data.last) {
      return lastPage.data.number + 1;
    }
  },
}));
// 신규등록 페이지용 쿼리
const [, newEnrollAuctionItemListAtom] = atomsWithInfiniteQuery((get) => ({
  queryKey: ['auction', '신규등록순'],
  queryFn: async ({ queryKey: [name, sort], pageParam = 0 }) => {
    let url = `http://3.38.59.40:8080/api/v1/auction/filter-list?size=6&page=${pageParam}`;
    // url에 sort 추가해줌
    url += `&sort=${sort}`;
    // 최종적으로 해당 url로 요청을 보냄
    const page = await axios.get(url);
    return page;
  },
  // infinite queries can support paginated fetching
  getNextPageParam: (lastPage: any, pages) => {
    if (!lastPage?.data.last) {
      return lastPage.data.number + 1;
    }
  },
}));

// 카테고리 및 필터 조회용 쿼리
const [, auctionItemListAtom] = atomsWithInfiniteQuery((get) => ({
  queryKey: [
    'auction',
    get(categoryAtom),
    get(stylesAtom),
    get(minPriceAtom),
    get(maxPriceAtom),
    get(sortAtom),
  ],
  queryFn: async ({
    queryKey: [name, category, styles, minPrice, maxPrice, sort],
    pageParam = 0,
  }) => {
    let url = `http://3.38.59.40:8080/api/v1/auction/filter-list?size=6&page=${pageParam}`;
    // url에 sort 추가해줌
    url += `&sort=${sort}`;
    // category 있을 때 카테고리 추가해줌
    if (category !== '') {
      // 홈데코 카테고리 예외처리해줌
      if (category === '홈데코') {
        const newCategory = '홈 데코';
        url += `&category=${newCategory}`;
      } else {
        url += `&category=${category}`;
      }
    }
    // minPrice있을 때 추가해줌
    if (minPrice !== '') {
      url += `&minPrice=${String(minPrice).replace(/,/g, '')}`;
    }
    // maxPrice있을 때 추가해줌
    if (maxPrice !== '') {
      url += `&maxPrice=${String(maxPrice).replace(/,/g, '')}`;
    }
    // styles있을 때 styles 추가해줌
    if ((styles as Array<string>).length !== 0) {
      let existStyle = (styles as Array<string>)[0];
      (styles as Array<string>).forEach((style, index) => {
        if (index !== 0) {
          existStyle += `, ${style}`;
        }
      });
      url += `&styles=${existStyle}`;
    }
    // 최종적으로 해당 url로 요청을 보냄
    const page = await axios.get(url);
    return page;
  },
  // infinite queries can support paginated fetching
  getNextPageParam: (lastPage: any, pages) => {
    if (!lastPage?.data.last) {
      return lastPage.data.number + 1;
    }
  },
}));

export {
  auctionItemListAtom,
  categoryAtom,
  stylesAtom,
  minPriceAtom,
  maxPriceAtom,
  sortAtom,
  endTimeAuctionItemListAtom,
  newEnrollAuctionItemListAtom,
  filterInfoAtom,
  resetStoredFilterInfoAtom,
  setStoredFilterInfoAtom,
};

interface FilterInfoType {
  sort: string;
  styles: string[];
  minPrice: string;
  maxPrice: string;
}
