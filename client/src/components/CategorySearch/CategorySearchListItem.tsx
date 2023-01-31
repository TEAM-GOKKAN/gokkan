import styled from 'styled-components';
import { insertCommas } from '../../utils/handleCommas';
import { getElapsedTime } from '../../utils/getDiffTime';
import { useNavigate } from 'react-router-dom';

const CategorySearchListItem = ({
  lotInfo,
}: CategorySearchListItemPropType) => {
  const navigate = useNavigate();

  const handleAuctionItemClick = () => {
    navigate(`/auction/${lotInfo.itemId}/${lotInfo.id}`);
  };

  return (
    <Container onClick={handleAuctionItemClick}>
      <img src={lotInfo.thumbnail} />
      <div className="lot-name">{lotInfo.name}</div>
      <div className="price">
        <p className="price-title">현재가</p>
        <div className="price-content">
          <span className="price-value">
            {insertCommas(lotInfo.currentPrice)}
          </span>
          <span className="unit">원</span>
        </div>
      </div>
      <div className="end-time">
        {getElapsedTime(lotInfo.auctionEndDateTime, '남은시간')}
      </div>
    </Container>
  );
};

export default CategorySearchListItem;

interface CategorySearchListItemPropType {
  lotInfo: {
    id: string;
    itemId: string;
    name: string;
    thumbnail: string;
    currentPrice: number;
    writer: string;
    auctionState: string;
    auctionEndDateTime: string;
  };
}

const Container = styled.li`
  width: calc(50vw - 24px);
  display: flex;
  flex-direction: column;
  img {
    height: calc((50vw - 24px) * 1.5);
    width: 100%;
    margin-bottom: 12px;
    object-fit: cover;
  }
  .lot-name {
    margin-bottom: 20px;
    font-weight: 400;
    font-size: 14px;
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
      color: var(--color-brown300);
    }
    .price-content {
      display: flex;
      flex-direction: row;
      .unit {
        font-weight: 400;
        font-size: 14px;
        line-height: 24px;
      }
      .price-value {
        font-weight: 600;
        font-size: 16px;
        line-height: 24px;
        font-family: 'Poppins';
        letter-spacing: normal;
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
