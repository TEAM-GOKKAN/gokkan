import React, { ReactNode } from 'react';
import SpreadList from '../common/SpreadList';
import SettingMyInfo from './SettingMyInfo';

const MySetting = () => {
  const listItems: ReactNode[] = [];
  listItems.push(<SettingMyInfo />);
  listItems.push(<div>카드 정보</div>);

  return <SpreadList title="설정" listItems={listItems} />;
};

export default MySetting;
