import { atomsWithQuery } from 'jotai-tanstack-query';
import { atom } from 'jotai';
import customAxios from '../utils/customAxios';
import { auctionIdAtom } from './bidAtom';
import { lotIdAtom } from './lotDetailAtom';

interface IAddress {
  name: string;
  phoneNumber: string;
  address: string;
  addressDetail: string;
}

interface IProductInfo {
  id: number;
  itemId: number;
  itemName: string;
  thumbnail: string;
  price: number;
}

interface IPaymentAmount {
  hammerPrice: number;
  fee: number;
  paymentAmount: number;
}

const [addressAtom] = atomsWithQuery(() => ({
  queryKey: ['shippingAddress'],
  queryFn: async (): Promise<IAddress> => {
    const { data } = await customAxios.get('/api/v1/auction/order/address');

    return data;
  },
}));

const [productInfoAtom] = atomsWithQuery((get) => ({
  queryKey: ['productInfo', get(auctionIdAtom), get(lotIdAtom)],
  queryFn: async ({
    queryKey: [, auctionId, itemId],
  }): Promise<IProductInfo> => {
    const { data } = await customAxios.get('/api/v1/auction/order/item', {
      params: {
        auctionId,
        itemId,
      },
    });

    return data;
  },
}));

const [paymentAmountAtom] = atomsWithQuery((get) => ({
  queryKey: ['paymentAmount', get(auctionIdAtom)],
  queryFn: async ({ queryKey: [, auctionId] }): Promise<IPaymentAmount> => {
    const { data } = await customAxios.get('/api/v1/auction/order/pay', {
      params: {
        auctionId,
      },
    });

    return data;
  },
}));

const pgAtom = atom('');
const payMethodAtom = atom('card');
const mRedirectUrlAtom = atom('');

const iamportDataAtom = atom((get) => {
  const { name, phoneNumber, address, addressDetail } = get(addressAtom);
  const { paymentAmount } = get(paymentAmountAtom);
  const { itemName } = get(productInfoAtom);

  return {
    pg: get(pgAtom),
    pay_method: get(payMethodAtom),
    merchant_uid: `${new Date().getTime()}`,
    amount: paymentAmount,
    name: itemName,
    buyer_name: name,
    buyer_tel: phoneNumber,
    buyer_addr: `${address} ${addressDetail}`,
    m_redirect_url: get(mRedirectUrlAtom),
  };
});

const payErrMsgAtom = atom('');
const paymentResultErrMsgAtom = atom('');

export {
  addressAtom,
  productInfoAtom,
  paymentAmountAtom,
  iamportDataAtom,
  pgAtom,
  payMethodAtom,
  mRedirectUrlAtom,
  payErrMsgAtom,
  paymentResultErrMsgAtom,
};
