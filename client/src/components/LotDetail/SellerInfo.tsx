import React from 'react';
import styled from 'styled-components';
import { BsStarFill } from 'react-icons/bs';
import { convertDateFormat } from '../../utils/convertDateFormat';

interface Iprops {
  data: {
    name: string;
    profileImageUrl: string;
    createdAt: string;
  };
}

export default function SellerInfo({ data }: Iprops) {
  const { year, month } = convertDateFormat(data.createdAt);

  return (
    <Container>
      <Title>판매자</Title>
      <Info>
        <Profile>
          <ProfileImageContainer>
            <img src={data.profileImageUrl || ''} alt="" />
          </ProfileImageContainer>
          <span>{data.name}</span>
        </Profile>
        <SignedUpDate>
          <span>since</span>
          <span>{`${year}. ${month}`}</span>
        </SignedUpDate>
      </Info>
    </Container>
  );
}

const Container = styled.div`
  margin-bottom: 142px;

  &::after {
    content: '';
    width: 100%;
    height: 1px;
    margin-top: 42px;
    display: block;
    border-bottom: 1px solid var(--color-brown100);
  }
`;

const Title = styled.h4`
  font-size: var(--font-regular);
  font-weight: 500;
  margin-bottom: 28px;
`;

const Info = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;

  & * {
    font-size: var(--font-small);
  }
`;

const Profile = styled.div`
  display: flex;
  align-items: center;
  gap: 10px;

  & > span {
    font-weight: 500;
  }
`;

const ProfileImageContainer = styled.div`
  width: 36px;
  height: 36px;
  border-radius: 18px;
  overflow: hidden;
  background: var(--color-brown100);

  & > img {
    width: 100%;
    object-fit: cover;
  }
`;

const SignedUpDate = styled.div`
  display: flex;
  gap: 10px;

  & > span {
    letter-spacing: normal;
  }
`;

const StarRating = styled.div`
  display: flex;
  gap: 1px;
`;
