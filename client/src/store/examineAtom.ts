import customAxios from '../utils/customAxios';
import { atomsWithQuery } from 'jotai-tanstack-query';
import { atom } from 'jotai';

const itemIdAtom = atom(24);
const [itemDetailAtom] = atomsWithQuery((get) => ({
  queryKey: ['itemDetail', get(itemIdAtom)],
  queryFn: async ({ queryKey: [, itemId] }): Promise<ItemDetail> => {
    const res = await customAxios({
      method: 'get',
      url: `api/v1/items/details`,
      params: {
        itemId: itemId,
      },
    });
    return res?.data;
  },
}));
const examineCheckAtom = atom('');
const examineApproveDetailAtom = atom('');
const examineDenyDetailAtom = atom('');
const examineMinimumPriceAtom = atom('');
const examineMaximumPriceAtom = atom('');
const resetExamineData = atom(null, (get, set, data) => {
  set(examineCheckAtom, '');
  set(examineApproveDetailAtom, '');
  set(examineDenyDetailAtom, '');
  set(examineMinimumPriceAtom, '');
  set(examineMaximumPriceAtom, '');
});

export {
  itemDetailAtom,
  itemIdAtom,
  examineCheckAtom,
  examineApproveDetailAtom,
  examineDenyDetailAtom,
  examineMinimumPriceAtom,
  examineMaximumPriceAtom,
  resetExamineData,
};

interface ItemDetail {
  id: number;
  itemNumber: string;
  name: string;
  thumbnail: string;
  startPrice: number;
  width: number;
  depth: number;
  height: number;
  material: string;
  conditionGrade: string;
  conditionDescription: string;
  text: string;
  designer: string;
  brand: string;
  productionYear: number;
  writer: null | string;
  category: Category;
  imageItemUrls: ImageUrl[];
  imageCheckUrls: ImageUrl[];
  styles: string[];
  created: string;
  updated: string;
}

interface ImageUrl {
  id: number;
  url: string;
}

interface Category {
  name: string;
  children: Category[];
}
