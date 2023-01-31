import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import PageControlButton from '../../common/PageControlButton';
import ProductMaterial from './ProductMaterial';
import ProductAge from './ProductAge';
import ProductSize from './ProductSize';
import ProductCondition from './ProductCondition';
import ProductDescription from './ProductDescription';
import ProductStyle from './ProductStyle';
import {
  productStyleAtom,
  productMaterialAtom,
  productAgeAtom,
  productWidthAtom,
  productDepthAtom,
  productHeightAtom,
  productConditionAtom,
  productDetailConditionAtom,
  productDescriptionAtom,
} from '../../../store/registerAtom';
import { useAtom } from 'jotai';

const ProductDetailInfoWrapper = styled.div`
  display: flex;
  flex-direction: column;
  padding: 0 16px;
`;

const PageThree = () => {
  const [active, setActive] = useState(false);
  const [productStyleList] = useAtom(productStyleAtom);
  const [productMaterial] = useAtom(productMaterialAtom);
  const [productAge] = useAtom(productAgeAtom);
  const [productWidth] = useAtom(productWidthAtom);
  const [productDepth] = useAtom(productDepthAtom);
  const [productHeight] = useAtom(productHeightAtom);
  const [productCondition] = useAtom(productConditionAtom);
  const [productDetailCondition] = useAtom(productDetailConditionAtom);
  const [productDescription] = useAtom(productDescriptionAtom);

  useEffect(() => {
    if (
      productStyleList.length !== 0 &&
      productMaterial !== '' &&
      productAge !== '' &&
      productWidth !== '' &&
      productDepth !== '' &&
      productHeight !== '' &&
      productCondition !== '' &&
      productDetailCondition !== '' &&
      productDescription !== ''
    ) {
      setActive(true);
    } else {
      setActive(false);
    }
  }, [
    productStyleList.length,
    productMaterial,
    productAge,
    productWidth,
    productDepth,
    productHeight,
    productCondition,
    productDetailCondition,
    productDescription,
  ]);

  return (
    <ProductDetailInfoWrapper>
      <ProductStyle />
      <ProductMaterial />
      <ProductAge />
      <ProductSize />
      <ProductCondition />
      <ProductDescription />
      <PageControlButton active={active} />
    </ProductDetailInfoWrapper>
  );
};

export default PageThree;
