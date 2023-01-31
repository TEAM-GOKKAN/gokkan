import React from 'react';
import styled from 'styled-components';
import { insertCommas } from '../../utils/handleCommas';

interface Iprops {
  data: {
    name: string;
    profileImageUrl: string;
    comment: string;
    minPrice: number;
    maxPrice: number;
    styles: string;
  };
}

export default function ExpertValuation({ data }: Iprops) {
  const { name, profileImageUrl, comment, minPrice, maxPrice, styles } = data;
  const minPriceWithCommas = insertCommas(minPrice);
  const maxPriceWithCommas = insertCommas(maxPrice);

  return (
    <Container>
      <Profile>
        <ProfileImageContainer>
          <img src={profileImageUrl || ''} alt="expert profile image" />
        </ProfileImageContainer>
        <ExpertInfo>
          <div>{name}</div>
          <ExpertField>
            {styles.split(', ').map((style) => (
              <span key={style}>{style}</span>
            ))}
          </ExpertField>
        </ExpertInfo>
      </Profile>
      <Comment>{comment}</Comment>
      <EstimatedPrice>
        <span>예상 낙찰가</span>
        <div>
          <span>{`${minPriceWithCommas} - ${maxPriceWithCommas}`}</span>
          <span>원</span>
        </div>
      </EstimatedPrice>
    </Container>
  );
}

const Container = styled.div`
  margin-bottom: 42px;

  &::after {
    content: '';
    width: 100%;
    height: 1px;
    margin-top: 42px;
    display: block;
    border-bottom: 1px solid var(--color-brown100);
  }
`;

const Profile = styled.div`
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 16px;
  width: 100%;
`;

const ProfileImageContainer = styled.div`
  width: 48px;
  height: 48px;
  border-radius: 50%;
  overflow: hidden;
  background: var(--color-brown200);

  & > img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
`;

const ExpertInfo = styled.div`
  font-weight: 500;
  width: calc(100% - 48px - 10px);
`;

const ExpertField = styled.div`
  margin-top: 6px;
  width: 100%;
  display: flex;
  flex-wrap: wrap;

  & span {
    color: var(--color-brown300);
    font-size: var(--font-micro);
    font-weight: 400;
    letter-spacing: normal;

    &::after {
      content: '';
      display: inline-block;
      position: relative;
      width: 1px;
      height: 11px;
      top: 2px;
      border-right: 1px solid var(--color-brown300);
      margin: 0 6px;
    }

    &:last-child {
      &::after {
        display: none;
      }
    }
  }
`;

const Comment = styled.div`
  padding: 10px 12px;
  background: var(--color-brown100);
  margin-bottom: 14px;
  font-size: var(--font-small);
  line-height: calc(var(--font-small) * 1.3);
  width: 100%;
  white-space: pre-line;

  & > span {
    font-size: var(--font-small);
  }
`;

const EstimatedPrice = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;

  & > span {
    font-size: var(--font-micro);
  }
`;
