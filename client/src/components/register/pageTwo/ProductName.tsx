import React from 'react';
import CustomProductInput from '../../common/CustomProductInput';
import { productNameAtom } from '../../../store/registerAtom';
import styled from 'styled-components';

const ProductNameWrapper = styled.div`
  margin-bottom: 42px;
`;

const ProductName = () => {
  return (
    <ProductNameWrapper>
      <CustomProductInput
        title="제품명"
        storeAtom={productNameAtom}
        placeHolder="제품 이름을 적어주세요"
      />
    </ProductNameWrapper>
  );
};

export default ProductName;
