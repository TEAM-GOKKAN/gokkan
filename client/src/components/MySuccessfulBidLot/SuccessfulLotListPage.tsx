import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import customAxios from '../../utils/customAxios';
import { useInfiniteQuery } from '@tanstack/react-query';
import { useInView } from 'react-intersection-observer';
import LoadingIndicator from '../common/LoadingIndicator';
import SuccessfullLotList from './SuccessfulLotList';

const SuccessfulLotListPage = ({ url, queryKey, title }: PropType) => {
  const [lotDisplayList, setLotDisplayList] = useState<LotInfoType[]>([]);
  const [lotDisplayCount, setLotDisplayCount] = useState(0);
  const [loadingRef, inView] = useInView();

  const getLotList = ({ pageParam = 0 }) => {
    return customAxios.get(`${url}size=6&page=${pageParam}`);
  };

  const {
    data,
    error,
    fetchNextPage,
    hasNextPage,
    isFetching,
    isFetchingNextPage,
    status,
    refetch,
  } = useInfiniteQuery({
    queryKey: [queryKey],
    queryFn: getLotList,
    getNextPageParam: (lastPage, pages) => {
      if (!lastPage.data.last) {
        return lastPage.data.number + 1;
      }
    },
  });

  useEffect(() => {
    if (!isFetching && status === 'success') {
      setLotDisplayCount(data.pages[0].data.totalElements);
      setLotDisplayList((pre) => {
        let targetList: LotInfoType[] = [];
        data.pages.forEach((page) => {
          targetList = [...targetList, ...page.data.content];
        });

        return targetList;
      });
    }
  }, [isFetching, status, hasNextPage]);

  useEffect(() => {
    if (inView) {
      fetchNextPage();
    }
  }, [inView]);

  // 페이지가 로드될 때마다 refetch해 줌
  useEffect(() => {
    // 초기화
    setLotDisplayList([]);
    refetch();
  }, []);

  return (
    <Container>
      <div className="title">{title}</div>
      <SuccessfullLotList
        lotList={lotDisplayList}
        lotListCount={lotDisplayCount}
      />
      {hasNextPage === true && (
        <LoadingWrapper ref={loadingRef}>
          <LoadingIndicator />
        </LoadingWrapper>
      )}
    </Container>
  );
};

export default SuccessfulLotListPage;

const Container = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  .title {
    font-weight: 700;
    font-size: 16px;
    margin-bottom: 48px;
  }
`;
const LoadingWrapper = styled.div`
  width: 100%;
  height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
`;
type LotInfoType = {
  id: number;
  itemId: number;
  name: string;
  thumbnail: string;
  currentPrice: number;
  writer: string;
  auctionState: string;
  auctionEndDateTime: string;
};

type PropType = {
  url: string;
  queryKey: string;
  title: string;
};
