import React from 'react';
import KakaoButton from '../components/SignIn/KakaoButton';
import styled from 'styled-components';
import Modal from '../components/common/Modal';

type SiginInType = {
  setSignIn: React.Dispatch<React.SetStateAction<boolean>>;
};

interface IProps {
  onClose?: () => void;
}

const SignInPage = ({ onClose }: IProps) => {
  return (
    <Modal onClose={onClose}>
      <SignInWrapper>
        <div className="content-holder">
          <div className="login-title">
            <p className="highlight">로그인</p> 또는
            <p className="highlight">회원가입</p>
          </div>
          <div className="content">
            큐레이팅된 빈티지 가구들로 곳간을 채워보세요
          </div>
          <KakaoButton />
        </div>
      </SignInWrapper>
    </Modal>
  );
};

const SignInWrapper = styled.div`
  .content-holder {
    display: flex;
    justify-content: space-evenly;
    align-items: center;
    flex-direction: column;
    .login-title {
      display: flex;
      flex-direction: row;
      align-items: center;
      font-size: var(--font-micro);
      line-height: 12px;
      margin-bottom: 12px;
      .highlight {
        margin: 0 5px;
        font-size: var(--font-regular);
        font-weight: var(--weight-semi-bold);
      }
    }
    .content {
      font-style: normal;
      font-weight: 400;
      font-size: 14px;
      line-height: 21px;
      color: #9d9792;
      margin-bottom: 24px;
    }
  }
`;

export default SignInPage;
