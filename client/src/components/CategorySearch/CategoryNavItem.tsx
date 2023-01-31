import React, { useState, useEffect, useRef } from 'react';
import styled from 'styled-components';
import { useParams } from 'react-router-dom';
import { firstDepthCategoryList } from '../../utils/category';
import { useNavigate } from 'react-router-dom';

const CategoryNavItem = ({ secondDepthCategory }: CategoryNavItemProptype) => {
  const [focused, setFocused] = useState(false);
  const [buttonText, setButtonText] = useState('');
  const { category } = useParams();
  const navigate = useNavigate();
  const containerRef = useRef<HTMLLIElement>(null);

  // 클릭하면 해당 second category로 이동하도록 만들어줌
  const handleSecondCateogorySelectButtonClick = () => {
    if (category === secondDepthCategory) return;
    navigate(`/categorySearch/${secondDepthCategory}`);
  };

  useEffect(() => {
    // 선택되었는지 표시함
    if (secondDepthCategory === category) {
      setFocused(true);
    } else {
      setFocused(false);
    }

    // 버튼 텍스트가 전체인 경우를 탐색해줌
    if (firstDepthCategoryList.includes(secondDepthCategory)) {
      setButtonText('전체');
    } else {
      setButtonText(secondDepthCategory);
    }
  }, [category]);

  // 해당 element가 view안에 들어오도록 조정함
  useEffect(() => {
    if (focused && containerRef) {
      containerRef.current?.scrollIntoView({ block: 'end', inline: 'nearest' });
    }
  }, [focused, containerRef]);

  return (
    <Container
      data-focused={focused}
      onClick={handleSecondCateogorySelectButtonClick}
      ref={containerRef}
    >
      {buttonText}
    </Container>
  );
};

export default CategoryNavItem;

interface CategoryNavItemProptype {
  secondDepthCategory: string;
}

const Container = styled.li`
  padding: 4px 16px;
  font-weight: 500;
  font-size: 14px;
  color: var(--color-brown300);
  line-height: 20px;
  &[data-focused='true'] {
    background-color: var(--color-brown300);
    color: var(--color-brown100);
  }
`;
