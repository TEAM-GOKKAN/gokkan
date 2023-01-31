import React from 'react';
import { productDescriptionAtom } from '../../../store/registerAtom';
import { useAtom } from 'jotai';
import styled from 'styled-components';

const DetailInfoWrapper = styled.div`
  display: flex;
  flex-direction: column;
  position: relative;
  margin-bottom: 90px;
  label {
    font-weight: 500;
    font-size: 14px;
    line-height: 20px;
    letter-spacing: -4%;
    margin-bottom: 10px;
  }
  textarea {
    border: 1px solid var(--color-brown100);
    background: none;
    padding: 10px;
    height: 140px;
    resize: none;
  }
  .limit {
    display: flex;
    flex-direction: row;
    align-items: center;
    position: absolute;
    right: 3px;
    bottom: -20px;
    font-family: 'Poppins';
    letter-spacing: normal;
    .limit-unit {
      color: var(--color-brown200);
      margin-left: 3px;
    }
  }
`;

const ProductDescription = () => {
  const [description, setDescription] = useAtom(productDescriptionAtom);

  const handleDescriptionChange = (
    e: React.ChangeEvent<HTMLTextAreaElement>
  ) => {
    if (e.target.value.length <= 1000) {
      setDescription(e.target.value);
    }
  };

  return (
    <DetailInfoWrapper>
      <label htmlFor="detail-info">설명</label>
      <textarea
        id="detail-info"
        name="detail-info"
        rows={5}
        cols={33}
        placeholder={'제품에 대한 상세 정보를 입력해주세요'}
        onChange={handleDescriptionChange}
        value={description}
      />
      <div className="limit">
        {description.length} <p className="limit-unit">/ 1000</p>{' '}
      </div>
    </DetailInfoWrapper>
  );
};

export default ProductDescription;
