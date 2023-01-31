import React from 'react';
import styled from 'styled-components';
import { MdChevronRight } from 'react-icons/md';

interface Category {
  name: string;
  children: Category[];
}

interface Iprops {
  category: Category;
}

export default function CategoryInfo({ category }: Iprops) {
  return (
    <Conatiner>
      <span>{category.name}</span>
      <MdChevronRight />
      <span>{category.children?.at(-1)?.name}</span>
    </Conatiner>
  );
}

const Conatiner = styled.div`
  margin-bottom: 20px;
  display: flex;
  align-items: center;

  & * {
    font-size: var(--font-micro);
    color: var(--color-brown300);
  }
`;
