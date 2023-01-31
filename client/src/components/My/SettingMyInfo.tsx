import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAtom } from 'jotai';
import { userInfoFixAtom } from '../../store/signUpAtom';

const SettingMyInfo = () => {
  const [, setUserInfoFix] = useAtom(userInfoFixAtom);
  const navigate = useNavigate();

  const handleMyInfoClick = () => {
    setUserInfoFix('fix');
    navigate('/signup');
  };
  return <div onClick={handleMyInfoClick}>회원 정보</div>;
};

export default SettingMyInfo;
