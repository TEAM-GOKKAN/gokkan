import { atom } from 'jotai';
import { filterInfoAtom } from './auctionQueryAtom';

const sortFilterAtom = atom('');
const styleFilterAtom = atom<string[]>([]);
const minPriceFilterAtom = atom('');
const maxPriceFilterAtom = atom('');
const resetFilterAtom = atom(null, (get, set, data) => {
  set(sortFilterAtom, '');
  set(styleFilterAtom, []);
  set(minPriceFilterAtom, '');
  set(maxPriceFilterAtom, '');
});
const initialFilterSettingAtom = atom(null, (get, set, data) => {
  const filterInfo = get(filterInfoAtom);
  set(sortFilterAtom, filterInfo.sort);
  set(styleFilterAtom, filterInfo.styles);
  set(minPriceFilterAtom, filterInfo.minPrice);
  set(maxPriceFilterAtom, filterInfo.maxPrice);
});

const getFilterDataAtom = atom((get) => {
  const sort = get(sortFilterAtom);
  const styles = get(styleFilterAtom);
  const minPrice = get(minPriceFilterAtom);
  const maxPrice = get(maxPriceFilterAtom);
  return {
    sort,
    styles,
    minPrice,
    maxPrice,
  };
});

export {
  sortFilterAtom,
  styleFilterAtom,
  minPriceFilterAtom,
  maxPriceFilterAtom,
  resetFilterAtom,
  initialFilterSettingAtom,
  getFilterDataAtom,
};
