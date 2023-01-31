import { useEffect, useState } from 'react';
import styled from 'styled-components';
import ProductPrice from './ProductPrice';
import PageControlButton from '../../common/PageControlButton';
import { productStartPriceAtom } from '../../../store/registerAtom';
import { useAtom } from 'jotai';

const PageFourWrapper = styled.div`
  display: flex;
  flex-direction: column;
  padding: 0 16px;
`;

const PageFour = () => {
  const [startPrice, setStartPrice] = useAtom(productStartPriceAtom);
  const [buttonState, setButtonState] = useState(false);

  useEffect(() => {
    const priceNumber = Number(startPrice.replace(/,/g, ''));
    if (priceNumber >= 10000) {
      setButtonState(true);
    } else {
      setButtonState(false);
    }
  }, [startPrice]);

  return (
    <PageFourWrapper>
      <ProductPrice />
      <PageControlButton active={buttonState} />
    </PageFourWrapper>
  );
};

export default PageFour;
