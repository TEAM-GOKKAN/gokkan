import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import customAxios from '../../utils/customAxios';
import { useInfiniteQuery } from '@tanstack/react-query';
import { useInView } from 'react-intersection-observer';
import LoadingIndicator from '../common/LoadingIndicator';
import LotList from '../common/LotList';

const LotButtonListPage = () => {
  const [lotDisplayList, setLotDisplayList] = useState<LotInfoType[]>([]);
  const [lotDisplayCount, setLotDisplayCount] = useState(0);
  const [loadingRef, inView] = useInView();
  const [target, setTarget] = useState('경매중');
  let url = `api/v1/auction/list/my?auctionStatus=${target}&`;
  const auctionState = ['경매중', '결제대기', '마감'];
  const handleTargetClick = (state: string) => {
    if (state !== target) {
      setTarget(state);
    }
  };

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
    queryKey: [target],
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

  // target이 바뀔 때마다 refetch해 줌
  useEffect(() => {
    // 초기화
    setLotDisplayList([]);
    refetch();
  }, [target]);

  return (
    <Container>
      <div className="title">내가 등록한 경매</div>
      <ul>
        {auctionState.map((state) => {
          const focused = state === target;
          return (
            <li
              key={state}
              data-focused={focused}
              onClick={() => {
                handleTargetClick(state);
              }}
            >
              {state}
            </li>
          );
        })}
      </ul>
      <LotList lotList={lotDisplayList} lotListCount={lotDisplayCount} />
      {hasNextPage === true && (
        <LoadingWrapper ref={loadingRef}>
          <LoadingIndicator />
        </LoadingWrapper>
      )}
    </Container>
  );
};

export default LotButtonListPage;

const Container = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  .title {
    font-weight: 700;
    font-size: 16px;
    margin-bottom: 20px;
  }
  ul {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 48px;
    width: 200px;
    li {
      padding: 6px 10px;
      border: 1px solid var(--color-brown200);
      color: var(--color-brown200);
      &[data-focused='true'] {
        border: 1px solid var(--color-brown500);
        color: var(--color-brown500);
      }
    }
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
