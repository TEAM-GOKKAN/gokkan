import React, { useEffect, useState } from 'react';
import customAxios from '../../utils/customAxios';
import { productSetInfoAtom } from '../../store/registerAtom';
import { useAtom } from 'jotai';

const useProductTempInfo = (itemId: string, loadingFinish: () => void) => {
  const [, productSetInfo] = useAtom(productSetInfoAtom);

  useEffect(() => {
    const url = 'api/v1/items/details/temp';
    customAxios
      .get(url, {
        params: { itemId: Number(itemId) },
      })
      .then(({ data }) => {
        // 초기 데이터 설정
        productSetInfo(data);
        loadingFinish();
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);
};

export default useProductTempInfo;
