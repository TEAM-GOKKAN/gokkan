import React from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { firstDepthCategoryList } from '../../utils/category';

const CategoryItem = ({ content }: CategoryItemPropType) => {
  const navigate = useNavigate();

  const displayContent = firstDepthCategoryList.includes(content)
    ? '전체'
    : content;

  const handleCategoryButtonClick = () => {
    navigate(`/categorySearch/${content}`);
  };

  return (
    <Container onClick={handleCategoryButtonClick}>{displayContent}</Container>
  );
};

export default CategoryItem;

interface CategoryItemPropType {
  content: string;
}

const Container = styled.div`
  width: 100%;
`;
