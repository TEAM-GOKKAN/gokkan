import React, { ReactNode } from 'react';
import SpreadList from '../common/SpreadList';
import CategoryItem from './CategoryItem';
import { furniture } from '../../utils/category';

const FurnitureList = () => {
  const listItems: ReactNode[] = furniture.map((content) => {
    return <CategoryItem content={content} />;
  });

  return <SpreadList title="가구" listItems={listItems} />;
};

export default FurnitureList;
