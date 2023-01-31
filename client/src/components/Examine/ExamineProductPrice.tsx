import React from 'react';
import styled from 'styled-components';
import { insertCommas } from '../../utils/handleCommas';

interface Iprops {
  itemStartPrice: number;
}

const ExamineProductPrice = (props: Iprops) => {
  const { itemStartPrice } = props;
  const stringStartPrice = insertCommas(itemStartPrice);

  return (
    <Container>
      <div className="title">시작가</div>
      <div className="price-holder">{stringStartPrice} 원</div>
    </Container>
  );
};

export default ExamineProductPrice;

const Container = styled.div`
  display: flex;
  width: 100%;
  flex-direction: row;
  justify-content: space-between;
  font-weight: 500;
  font-size: 15px;
  margin-bottom: 90px;
`;
