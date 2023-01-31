import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { insertCommas } from '../../utils/handleCommas';
import { getElapsedTime } from '../../utils/getDiffTime';

const LotListElement = ({ lotInfo }: PropType) => {
  const navigate = useNavigate();

  const handleElementClick = () => {
    navigate(`/auction/${lotInfo.itemId}/${lotInfo.id}`);
  };

  return (
    <Container onClick={handleElementClick}>
      <img src={lotInfo.thumbnail} />
      <div className="lot-title">{lotInfo.name}</div>
      <div className="price">
        <p className="price-title">현재가</p>
        <div className="price-content">
          {insertCommas(lotInfo.currentPrice)}
          <p className="unit">원</p>
        </div>
      </div>
      <div className="end-time">
        {getElapsedTime(lotInfo.auctionEndDateTime, '남은시간')}
      </div>
    </Container>
  );
};

export default LotListElement;

const Container = styled.div`
  display: flex;
  flex-direction: column;
  width: calc(50vw - 24px);
  margin-bottom: 48px;
  img {
    height: calc((50vw - 24px) * 1.5);
    width: 100%;
    margin-bottom: 12px;
    object-fit: cover;
  }
  .lot-title {
    margin-bottom: 16px;
    font-weight: 400;
    font-size: var(--font-regular);
    line-height: 17.5px;
    width: 100%;
    white-space: pre-line;
  }
  .price {
    display: flex;
    flex-direction: column;
    .price-title {
      font-weight: 500;
      font-size: 12px;
      line-height: 17px;
      letter-spacing: -4%;
      color: var(--color-brown300);
    }
    .price-content {
      display: flex;
      flex-direction: row;
      color: var(--color-brown500);
      font-weight: 600;
      line-height: 24px;
      font-size: 16px;
      .unit {
        margin-left: 4px;
        font-weight: 400;
        font-size: 14px;
        display: flex;
        justify-content: center;
        align-items: center;
      }
    }
  }
  .end-time {
    margin-top: 12px;
    color: var(--color-brown300);
    font-weight: 400;
    font-size: var(--font-small);
  }
`;

type PropType = {
  lotInfo: {
    id: number;
    itemId: number;
    name: string;
    thumbnail: string;
    currentPrice: number;
    writer: string;
    auctionState: string;
    auctionEndDateTime: string;
  };
};
