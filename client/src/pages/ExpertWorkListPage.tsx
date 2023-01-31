import React from 'react';
import ProductListPage from '../components/common/ProductListPage';

const ExpertWorkListPage = () => {
  const url = 'api/v1/items/expert-items?';
  return (
    <ProductListPage
      url={url}
      queryKey="expertWorkList"
      title="감정 대기 목록"
      targetElementUrl="/expertWorkDetail"
    />
  );
};

export default ExpertWorkListPage;
