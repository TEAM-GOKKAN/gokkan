import React, { useEffect, useState } from 'react';
import ProductListPage from '../components/common/ProductListPage';

const MyWritingProductPage = () => {
  const url = 'api/v1/items/my-items?states=TEMPORARY&';

  return (
    <ProductListPage
      url={url}
      queryKey="myWritingProduct"
      title="작성 중인 경매 목록"
      targetElementUrl="/register/1"
    />
  );
};

export default MyWritingProductPage;
