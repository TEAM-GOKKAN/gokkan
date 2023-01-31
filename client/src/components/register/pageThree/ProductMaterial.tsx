import React from 'react';
import styled from 'styled-components';
import CustomProductInput from '../../common/CustomProductInput';
import { productMaterialAtom } from '../../../store/registerAtom';

const ProductMaterialWrapper = styled.div`
  margin-bottom: 40px;
`;

const ProductMaterial = () => {
  return (
    <ProductMaterialWrapper>
      <CustomProductInput
        title="소재"
        storeAtom={productMaterialAtom}
        placeHolder="ex) 스테인리스"
      />
    </ProductMaterialWrapper>
  );
};

export default ProductMaterial;
