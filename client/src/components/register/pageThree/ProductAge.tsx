import React from 'react';
import styled from 'styled-components';
import CustomProductInput from '../../common/CustomProductInput';
import { productAgeAtom } from '../../../store/registerAtom';

const ProductAgeWrapper = styled.div`
  margin-bottom: 40px;
`;

const ProductAge = () => {
  return (
    <ProductAgeWrapper>
      <CustomProductInput
        title="생산 연도"
        storeAtom={productAgeAtom}
        placeHolder="ex) 1960"
        inputType="number"
      />
    </ProductAgeWrapper>
  );
};

export default ProductAge;
