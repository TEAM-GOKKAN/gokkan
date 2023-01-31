import React from 'react';
import styled from 'styled-components';
import {
  productBrandAtom,
  productBrandFirstAtom,
} from '../../../store/registerAtom';
import CustomProductUnknownInput from '../../common/CustomProductUnknownInput';

const ProductBrandWrapper = styled.div`
  margin-bottom: 42px;
`;

const ProductBrand = () => {
  return (
    <ProductBrandWrapper>
      <CustomProductUnknownInput
        title="브랜드"
        storeAtom={productBrandAtom}
        placeHolder="브랜드를 입력해주세요"
        firstStoreAtom={productBrandFirstAtom}
      />
    </ProductBrandWrapper>
  );
};

export default ProductBrand;
