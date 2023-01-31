import React, { ReactNode } from 'react';
import SpreadList from '../common/SpreadList';
import MyRegisteredLot from './MyRegisteredLot';
import MyReturnedLot from './MyReturnedLot';
import MyBidLot from './MyBidLot';
import MySuccessfulBidLot from './MySuccessfulBidLot';
import MyFinishedBidLot from './MyFinishedBidLot';

const LotList = () => {
  const listItems: ReactNode[] = [];
  listItems.push(<MyBidLot />);
  listItems.push(<MySuccessfulBidLot />);
  listItems.push(<MyFinishedBidLot />);
  listItems.push(<MyRegisteredLot />);
  listItems.push(<MyReturnedLot />);

  return <SpreadList title="나의 경매 목록" listItems={listItems} />;
};

export default LotList;
