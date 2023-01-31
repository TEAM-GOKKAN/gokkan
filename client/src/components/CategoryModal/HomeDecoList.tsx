import React, { ReactNode } from 'react';
import SpreadList from '../common/SpreadList';
import CategoryItem from './CategoryItem';
import { homeDeco } from '../../utils/category';

const HomeDecoList = () => {
  const listItems: ReactNode[] = homeDeco.map((content) => {
    return <CategoryItem content={content} />;
  });

  return <SpreadList title="홈데코" listItems={listItems} />;
};

export default HomeDecoList;
