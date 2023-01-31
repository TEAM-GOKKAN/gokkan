import React from 'react';
import ModalFull from '../components/common/ModalFull';
import FurnitureList from '../components/CategoryModal/FurnitureList';
import LightList from '../components/CategoryModal/LightList';
import HomeDecoList from '../components/CategoryModal/HomeDecoList';

const CategoryModalPage = () => {
  return (
    <ModalFull title="카테고리">
      <FurnitureList />
      <LightList />
      <HomeDecoList />
    </ModalFull>
  );
};

export default CategoryModalPage;
