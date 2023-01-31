import React from 'react';
import { userNameAtom } from '../../store/signUpAtom';
import { useAtom } from 'jotai';
import styled from 'styled-components';

const NameWrapper = styled.div`
  display: flex;
  flex-direction: column;
  margin-bottom: 36px;
  font-family: Poppins, 'Noto Sans KR', sans-serif;
  label {
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
`;

const UserName = () => {
  const [name, setName] = useAtom(userNameAtom);

  const inputName = (e: React.ChangeEvent<HTMLInputElement>) => {
    setName(e.target.value);
  };

  return (
    <NameWrapper>
      <label htmlFor="name">이름</label>
      <input
        type="text"
        id="name"
        placeholder="이름을 입력해주세요"
        value={name}
        onChange={inputName}
      />
    </NameWrapper>
  );
};

export default UserName;
