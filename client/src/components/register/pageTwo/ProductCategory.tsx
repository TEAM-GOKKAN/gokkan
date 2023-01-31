import React, { useEffect } from 'react';
import styled from 'styled-components';
import { useAtom } from 'jotai';
import {
  firstDepthCategoryAtom,
  secondDepthCategoryAtom,
} from '../../../store/registerAtom';
import CategorySelector from '../../../components/common/CategorySelector';

const ProductCategoryWrapper = styled.div`
  display: flex;
  flex-direction: column;
  margin-bottom: 42px;
  .title {
    margin-bottom: 10px;
    font-weight: 500;
    font-size: 14px;
    line-height: 20px;
    letter-spacing: -4%;
  }
`;

const firstCategoryOptions = [
  { value: '가구', label: '가구' },
  { value: '조명', label: '조명' },
  { value: '홈 데코', label: '홈 데코' },
];

const secondCategoryOptions = {
  가구: [
    { value: '의자', label: '의자' },
    { value: '소파', label: '소파' },
    { value: '테이블', label: '테이블' },
    { value: '수납가구', label: '수납가구' },
  ],
  조명: [
    { value: '플로어 램프', label: '플로어 램프' },
    { value: '데스크 램프', label: '데스크 램프' },
    { value: '테이블 램프', label: '테이블 램프' },
    { value: '펜던트 램프', label: '펜던트 램프' },
    { value: '월 램프', label: '월 램프' },
  ],
  '홈 데코': [
    { value: '거울', label: '거울' },
    { value: '식기', label: '식기' },
    { value: '러그', label: '러그' },
    { value: '화병', label: '화병' },
    { value: '수납박스', label: '수납박스' },
  ],
  '': [],
};

const ProductCategory = () => {
  const [firstCategory, setFirstCategory] = useAtom(firstDepthCategoryAtom);
  const [secondCategory, setSecondCategory] = useAtom(secondDepthCategoryAtom);

  const handleFirstCategoryChange = (newValue: unknown) => {
    setFirstCategory(Object(newValue)?.value);
  };

  const handleSecondCategoryChange = (newValue: unknown) => {
    setSecondCategory(Object(newValue)?.value);
  };

  return (
    <ProductCategoryWrapper>
      <div className="title">카테고리</div>
      <CategorySelector
        options={firstCategoryOptions}
        onChange={handleFirstCategoryChange}
        targetValue={{ value: firstCategory, label: firstCategory }}
      />
      <CategorySelector
        options={secondCategoryOptions[firstCategory as keyof object]}
        onChange={handleSecondCategoryChange}
        targetValue={{ value: secondCategory, label: secondCategory }}
      />
    </ProductCategoryWrapper>
  );
};

export default ProductCategory;
