import React from 'react';
import ProductListPage from '../components/common/ProductListPage';

const MyReturnedLotPage = () => {
  const url = 'api/v1/items/my-items?states=RETURN&';

  return (
    <ProductListPage
      url={url}
      queryKey="myReturnedLot"
      title="반려된 경매 목록"
      targetElementUrl="/register/1"
    />
  );
};

export default MyReturnedLotPage;
