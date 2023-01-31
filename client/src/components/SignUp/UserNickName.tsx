import React, { useState, useEffect } from 'react';
import {
  userNickNameAtom,
  userNickNameCheckAtom,
} from '../../store/signUpAtom';
import { useAtom } from 'jotai';
import axios from 'axios';
import styled from 'styled-components';

const NickNameWrapper = styled.div`
  display: flex;
  flex-direction: column;
  margin-bottom: 36px;
  font-family: Poppins, 'Noto Sans KR', sans-serif;
  .nickname-title {
    font-weight: 500;
    font-size: 14px;
    line-height: 20px;
    letter-spacing: -4%;
  }
  input {
    width: 100%;
    padding: 10px 12px;
    margin-top: 10px;
    border: 1px solid var(--color-brown100);
    font-size: 15px;
    line-height: 21px;
    letter-spacing: -4%;
  }
  .available {
    font-size: 12px;
    color: var(--color-purple);
    margin-top: 4px;
  }
  .unavailable {
    font-size: 12px;
    color: --color-orange;
  }
`;

const UserNickName = () => {
  const [nickName, setNickName] = useAtom(userNickNameAtom);
  const [nickNameCheck, setNickNameCheck] = useAtom(userNickNameCheckAtom);

  const inputNickName = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNickName(e.target.value);
  };

  const checkDuplicate = () => {
    if (nickName === '') return;
    const baseUrl = 'http://3.38.59.40:8080/api/v1/users/nickName/duplicate';
    axios
      .get(baseUrl, { params: { nickName } })
      .then(({ data }) => {
        if (!data) {
          setNickNameCheck('true');
        } else {
          setNickNameCheck('false');
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  useEffect(() => {
    if (nickName === '') {
      setNickNameCheck('');
    }
  }, [nickName]);

  return (
    <NickNameWrapper>
      <div className="nickname-title">닉네임</div>
      <input
        type="text"
        placeholder="닉네임을 지어주세요"
        value={nickName}
        onChange={inputNickName}
        onBlur={checkDuplicate}
        data-duplicate={nickNameCheck}
      />
      {nickNameCheck === 'true' && (
        <div className="available">사용 가능한 닉네임입니다.</div>
      )}
      {nickNameCheck === 'false' && (
        <div className="unavailable">중복된 닉네임입니다.</div>
      )}
    </NickNameWrapper>
  );
};

export default UserNickName;
