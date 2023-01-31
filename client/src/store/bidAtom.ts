import { atomsWithQuery } from 'jotai-tanstack-query';
import { atom } from 'jotai';
import { Client } from '@stomp/stompjs';
import customAxios from '../utils/customAxios';

interface AuctionInfo {
  auctionEndDateTime: string;
  currentPrice: number;
}

interface BidInfo {
  memberId: string;
  price: number;
  bidTime: string;
}

const auctionIdAtom = atom(3);

const [, auctionInfoAtom] = atomsWithQuery((get) => ({
  queryKey: ['bidInfo', get(auctionIdAtom)],
  queryFn: async ({ queryKey: [, auctionId] }): Promise<AuctionInfo> => {
    const { data } = await customAxios.get('/api/v1/auction', {
      params: {
        auctionId,
      },
    });

    return data;
  },
}));

const auctionInfoDataAtom = atom((get) => get(auctionInfoAtom)?.data);

const [, bidHistoryAtom] = atomsWithQuery((get) => ({
  queryKey: ['bidHistory', get(auctionIdAtom)],
  queryFn: async ({ queryKey: [, auctionId] }): Promise<BidInfo[] | null[]> => {
    const { data } = await customAxios.get('/api/v1/auction/history', {
      params: {
        auctionId,
      },
    });

    return data;
  },
}));

const StompClientAtom = atom<React.MutableRefObject<Client | undefined> | null>(
  null
);

const bidErrMsgAtom = atom('');

const addedBidTimeAtom = atom('');
const isTimeAddedAtom = atom(false);

export {
  auctionIdAtom,
  auctionInfoAtom,
  bidHistoryAtom,
  StompClientAtom,
  bidErrMsgAtom,
  addedBidTimeAtom,
  isTimeAddedAtom,
  auctionInfoDataAtom,
};
