import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { useAtom } from 'jotai';
import {
  clearAtom,
  userProfileImageFileAtom,
  userInfoAtom,
  userNickNameCheckAtom,
  userPhoneCheckAtom,
  userInfoFixAtom,
} from '../../store/signUpAtom';
import customAxios from '../../utils/customAxios';
import { useNavigate } from 'react-router-dom';

const ConfirmButtonWrapper = styled.button`
  position: fixed;
  bottom: 0px;
  left: 0px;
  width: 100%;
  height: 50px;
  background-color: var(--color-brown100);
  z-index: 10;
  &[data-submit='true'] {
    background-color: var(--color-brown500);
    color: var(--color-brown100);
  }
`;

const SingUpConfirmButton = () => {
  const [submitPossible, setSubmitPossible] = useState('');
  const [buttonText, setButtonText] = useState('회원가입');
  const [, setClear] = useAtom(clearAtom);
  const [profileImageFile] = useAtom(userProfileImageFileAtom);
  const [userInfo] = useAtom(userInfoAtom);
  const [nickNameCheck] = useAtom(userNickNameCheckAtom);
  const [phoneCheck] = useAtom(userPhoneCheckAtom);
  const [userInfoFix, setUserInfoFix] = useAtom(userInfoFixAtom);
  const navigate = useNavigate();

  useEffect(() => {
    if (
      nickNameCheck === 'true' &&
      phoneCheck === 'true' &&
      userInfo?.address !== '' &&
      userInfo?.name !== '' &&
      userInfo?.addressDetail !== ''
    ) {
      setSubmitPossible('true');
    } else {
      setSubmitPossible('false');
    }
  }, [
    nickNameCheck,
    phoneCheck,
    userInfo?.address,
    userInfo?.name,
    userInfo?.addressDetail,
  ]);

  useEffect(() => {
    if (userInfoFix === 'fix') {
      setButtonText('회원정보 수정');
    } else {
      setButtonText('회원가입');
    }
  }, [userInfoFix]);

  const handleUserInfoSubmit = () => {
    if (submitPossible !== 'true') return;
    // Atom에 저장된 값들을 전송해줌
    const transferData = new FormData();
    const requestUpdateDto = new Blob([JSON.stringify(userInfo)], {
      type: 'application/json',
    });
    transferData.append('requestUpdateDto', requestUpdateDto);

    // 파일이 비워져 있을 때, 빈 배열을 보내기 위한 데이터
    const nullData = new Blob([], {
      type: 'application/json',
    });

    if (profileImageFile === '') {
      transferData.append('profileImage', nullData);
    } else {
      transferData.append('profileImage', profileImageFile);
    }

    customAxios
      .patch('api/v1/users', transferData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      })
      .then((res) => {
        // Atom에 저장된 값들을 전부 비워줌
        setClear('');
        // 이전 페이지로 넘어감(이거 어떻게 할지 몰라서, 일단 메인 페이지로 넘어가게 함)
        navigate('/');
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <ConfirmButtonWrapper
      onClick={handleUserInfoSubmit}
      data-submit={submitPossible}
    >
      {buttonText}
    </ConfirmButtonWrapper>
  );
};

export default SingUpConfirmButton;
