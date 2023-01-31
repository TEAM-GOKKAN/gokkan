import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { furniture, light, homeDeco } from '../../utils/category';
import { useRef } from 'react';
import styled from 'styled-components';
import CategoryNavItem from './CategoryNavItem';
import { useAtom } from 'jotai';
import { categoryAtom } from '../../store/auctionQueryAtom';

const CategoryNav = () => {
  const { category } = useParams();
  const [, setStoredCategory] = useAtom(categoryAtom);
  const [firstDepthCategory, setFirstDepthCategory] = useState('');
  const [secondDepthCategory, setSecondDepthCategory] = useState<string[]>([]);
  const ContainerRef = useRef<HTMLElement>(null);

  useEffect(() => {
    if (category && ContainerRef) {
      // 선택된 카테고리를 atom에 저장
      setStoredCategory(String(category));
      // 카테고리별로 표시할 타이틀을 지정함
      if (furniture.includes(String(category))) {
        setFirstDepthCategory('가구');
        setSecondDepthCategory(furniture);
      }
      if (light.includes(String(category))) {
        setFirstDepthCategory('조명');
        setSecondDepthCategory(light);
      }
      if (homeDeco.includes(String(category))) {
        setFirstDepthCategory('홈데코');
        setSecondDepthCategory(homeDeco);
      }
    }
  }, [category, ContainerRef]);

  return (
    <Container ref={ContainerRef}>
      <div className="title">{firstDepthCategory}</div>
      <ul>
        {secondDepthCategory.map((secondDepthCategory) => {
          return (
            <CategoryNavItem
              secondDepthCategory={secondDepthCategory}
              key={secondDepthCategory}
            />
          );
        })}
      </ul>
    </Container>
  );
};

export default CategoryNav;

const Container = styled.nav`
  margin-bottom: 48px;
  .title {
    font-weight: 700;
    font-size: 15px;
    margin-bottom: 20px;
  }
  ul {
    display: flex;
    flex-direction: row;
    overflow-x: scroll;
    /* hide scrollbar */
    -ms-overflow-style: none; /* IE and Edge */
    scrollbar-width: none; /* Firefox */
  }
  /* Hide scrollbar for Chrome, Safari and Opera */
  ul::-webkit-scrollbar {
    display: none;
  }
`;
