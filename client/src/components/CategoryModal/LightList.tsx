import React, { ReactNode } from 'react';
import SpreadList from '../common/SpreadList';
import CategoryItem from './CategoryItem';
import { light } from '../../utils/category';

const LightList = () => {
  const listItems: ReactNode[] = light.map((content) => {
    return <CategoryItem content={content} />;
  });

  return <SpreadList title="조명" listItems={listItems} />;
};

export default LightList;
