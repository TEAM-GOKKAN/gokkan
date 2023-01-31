import React, { useEffect, useState } from 'react';
import ProductCategory from './ProductCategory';
import ProductName from './ProductName';
import ProductBrand from './ProductBrand';
import styled from 'styled-components';
import ProductDesigner from './ProductDesigner';
import PageControlButton from '../../common/PageControlButton';
import {
  firstDepthCategoryAtom,
  secondDepthCategoryAtom,
  productNameAtom,
} from '../../../store/registerAtom';
import { useAtom } from 'jotai';

const ProductMainInfoWrapper = styled.div`
  display: flex;
  flex-direction: column;
  padding: 0 16px;
`;

const PageTwo = () => {
  const [active, setActive] = useState(false);
  const [firstCategory] = useAtom(firstDepthCategoryAtom);
  const [secondCategory] = useAtom(secondDepthCategoryAtom);
  const [productName] = useAtom(productNameAtom);

  useEffect(() => {
    if (firstCategory !== '' && secondCategory !== '' && productName !== '') {
      setActive(true);
    } else {
      setActive(false);
    }
  }, [firstCategory, secondCategory, productName]);

  return (
    <ProductMainInfoWrapper>
      <ProductCategory />
      <ProductName />
      <ProductBrand />
      <ProductDesigner />
      <PageControlButton active={active} />
    </ProductMainInfoWrapper>
  );
};

export default PageTwo;
