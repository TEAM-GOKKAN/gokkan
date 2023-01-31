import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { insertCommas } from '../../utils/handleCommas';
import { getElapsedTime } from '../../utils/getDiffTime';
// 확인
const SuccessfulLotListElement = ({ lotInfo }: PropType) => {
  const navigate = useNavigate();

  const handleElementClick = () => {
    navigate(`/auction/${lotInfo.itemId}/${lotInfo.id}`);
  };

  const handlePaymentClick = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.stopPropagation();
    navigate(`/payment/${lotInfo.itemId}/${lotInfo.id}`);
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
      <button className="commit-price" onClick={handlePaymentClick}>
        결제하기
      </button>
    </Container>
  );
};

export default SuccessfulLotListElement;

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
  .commit-price {
    width: 100%;
    padding: 6px 0px;
    background-color: var(--color-orange);
    color: var(--color-brown100);
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 12px;
    font-weight: 400;
    line-height: 12px;
    letter-spacing: -4%;
    margin-top: 10px;
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
