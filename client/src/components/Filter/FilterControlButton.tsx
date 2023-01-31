import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { useAtom } from 'jotai';
import { resetFilterAtom, getFilterDataAtom } from '../../store/filterAtom';
import {
  resetStoredFilterInfoAtom,
  setStoredFilterInfoAtom,
} from '../../store/auctionQueryAtom';
import { useNavigate } from 'react-router-dom';

const FilterControlButton = () => {
  const [, resetFilter] = useAtom(resetFilterAtom);
  const [, resetStoredFilter] = useAtom(resetStoredFilterInfoAtom);
  const [, setStoredFilter] = useAtom(setStoredFilterInfoAtom);
  const [filterData] = useAtom(getFilterDataAtom);
  const { sort, styles, minPrice, maxPrice } = filterData;
  const [active, setActive] = useState(false);
  const navigate = useNavigate();

  const handleResetButtonClick = () => {
    resetFilter();
    resetStoredFilter();
  };

  const handleCompleteButtonClick = () => {
    if (active) {
      // 제출을 클릭했을 때, StoredFilter data 갱신해주고, 이전 페이지로 돌아감
      setStoredFilter(filterData);
      navigate(-1);
    }
  };

  useEffect(() => {
    if (
      sort !== '' ||
      styles.length !== 0 ||
      minPrice !== '' ||
      maxPrice !== ''
    ) {
      setActive(true);
    } else {
      setActive(false);
    }
  }, [sort, styles.length, minPrice, maxPrice]);

  return (
    <Container>
      <button className="reset" onClick={handleResetButtonClick}>
        초기화
      </button>
      <button
        className="complete"
        onClick={handleCompleteButtonClick}
        data-active={active}
      >
        검색결과 보기
      </button>
    </Container>
  );
};

export default FilterControlButton;

const Container = styled.div`
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 50px;
  display: flex;
  flex-direction: row;
  button {
    height: 100%;
    width: 100%;
  }
  .reset {
    background-color: var(--color-brown200);
    color: var(--color-brown300);
  }
  .complete {
    color: var(--color-brown100);
    background-color: var(--color-brown300);
    &[data-active='true'] {
      background-color: var(--color-brown500);
    }
  }
`;
