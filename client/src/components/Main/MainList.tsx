import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { useAtom } from 'jotai';
import { auctionItemListAtom, sortAtom } from '../../store/auctionQueryAtom';
import { useInView } from 'react-intersection-observer';
import LoadingIndicator from '../common/LoadingIndicator';
import MainListElement from './MainListElement';

const MainList = ({ title, queryAtom }: MainListPropType) => {
  const [lotList, setLotList] = useState<LotInfoType[]>([]);
  const [loadingRef, inView] = useInView();
  const [queryResult] = useAtom(queryAtom);
  const {
    data,
    error,
    fetchNextPage,
    hasNextPage,
    isFetching,
    isFetchingNextPage,
    status,
    refetch,
  } = queryResult as any;

  useEffect(() => {
    if (!isFetching && status === 'success') {
      setLotList((pre) => {
        let targetList: LotInfoType[] = [];
        data.pages.forEach((page: any) => {
          targetList = [...targetList, ...page.data.content];
        });

        return targetList;
      });
    }
  }, [isFetching, status, hasNextPage]);

  // loading Indicator가 보일 때마다 refetch해줌
  useEffect(() => {
    if (inView) {
      fetchNextPage();
    }
  }, [inView]);

  // 페이지가 로드될 때마다 refetch해 줌
  useEffect(() => {
    // 초기화
    setLotList([]);
    refetch();
  }, []);

  return (
    <Container>
      <div className="title">{title}</div>
      <div className="scroll-container">
        {lotList.map((lotInfo) => {
          return <MainListElement key={lotInfo.id} lotInfo={lotInfo} />;
        })}
        {hasNextPage === true && (
          <LoadingWrapper ref={loadingRef}>
            <LoadingIndicator />
          </LoadingWrapper>
        )}
      </div>
    </Container>
  );
};

export default MainList;

interface MainListPropType {
  title: string;
  queryAtom: any;
  sort?: string;
}

interface LotInfoType {
  id: number;
  itemId: number;
  name: string;
  thumbnail: string;
  currentPrice: number;
  writer: string;
  auctionState: string;
  auctionEndDateTime: string;
}

const Container = styled.div`
  margin-bottom: 42px;
  .title {
    font-weight: 500;
    font-size: 16px;
    line-height: 23px;
    letter-spacing: -4%;
    margin-bottom: 16px;
  }
  .scroll-container {
    width: 100%;
    display: flex;
    flex-direction: row;
    overflow-x: scroll;
    overflow-y: hidden;
    height: 100%;
    /* hide scrollbar */
    -ms-overflow-style: none; /* IE and Edge */
    scrollbar-width: none; /* Firefox */
  }
  /* Hide scrollbar for Chrome, Safari and Opera */
  .scroll-container::-webkit-scrollbar {
    display: none;
  }
`;

const LoadingWrapper = styled.div`
  width: 100%;
  height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
`;
