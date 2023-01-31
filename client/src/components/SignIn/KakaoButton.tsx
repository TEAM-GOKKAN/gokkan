import { useRef } from 'react';
import styled from 'styled-components';
import customAxios from '../../utils/customAxios';
import { useNavigate } from 'react-router-dom';
import kakaoButtonImg from '../../assets/kakao_login_medium_wide.png';

const KakaoButtonWrapper = styled.section`
  img {
    width: 321px;
    height: 47.48px;
    border-radius: 12px;
    cursor: pointer;
  }
  a {
    display: hidden;
  }
`;

export default function KakaoButton() {
  const accessToken = localStorage.getItem('accessToken');
  const navigate = useNavigate();
  const anchorRef = useRef<HTMLAnchorElement>(null);
  const url =
    'http://3.38.59.40:8080/oauth2/authorization/kakao?redirect_uri=http://gokkan.s3-website.ap-northeast-2.amazonaws.com/signInCheck';

  const handleKakaoButtonClick = (e: React.MouseEvent<HTMLImageElement>) => {
    // 토큰이 null이 아닐 때(회원 정보를 받아옴)
    // 이렇게해서 휴대번호가 있는지 없는지 확인해야 함
    if (accessToken !== '') {
      customAxios
        .get('api/v1/users')
        .then(({ data }) => {
          const phoneNumber = data?.phoneNumber;
          if (!phoneNumber) {
            navigate('signup');
          } else {
            navigate(-1);
          }
        })
        .catch((err) => {
          console.error(err);
        });
    } else {
      const anchor = anchorRef.current;
      if (!anchor) return;
      anchor.click();
    }
  };

  return (
    <KakaoButtonWrapper>
      <img
        onClick={handleKakaoButtonClick}
        src={kakaoButtonImg}
        alt="로그인 버튼"
      />
      <a href={url} ref={anchorRef} />
    </KakaoButtonWrapper>
  );
}

type SiginInType = {
  setSignIn: React.Dispatch<React.SetStateAction<boolean>>;
};
