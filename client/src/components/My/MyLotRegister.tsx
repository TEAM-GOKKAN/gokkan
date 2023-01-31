import React, { ReactNode } from 'react';
import SpreadList from '../common/SpreadList';
import RegisterNewLot from './RegisterNewLot';
import RegisterMyWritingLot from './RegisterMyWritingLot';

const MyLotRegister = () => {
  const listItems: ReactNode[] = [];
  listItems.push(<RegisterNewLot />);
  listItems.push(<RegisterMyWritingLot />);

  return <SpreadList title="경매 등록" listItems={listItems} />;
};

export default MyLotRegister;
