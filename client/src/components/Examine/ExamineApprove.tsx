import React from 'react';
import styled from 'styled-components';
import { useAtom } from 'jotai';
import { examineApproveDetailAtom } from '../../store/examineAtom';
import {
  examineMaximumPriceAtom,
  examineMinimumPriceAtom,
} from '../../store/examineAtom';
import ExaminePriceInput from './ExaminePriceInput';

const ExamineApprove = () => {
  const [detailDescription, setDetailDescription] = useAtom(
    examineApproveDetailAtom
  );

  const handleDescriptionChange = (
    e: React.ChangeEvent<HTMLTextAreaElement>
  ) => {
    if (e.target.value.length <= 50) {
      setDetailDescription(e.target.value);
    }
  };
  return (
    <Container>
      <div className="comment-holder">
        <label htmlFor="detail-info">코멘트</label>
        <textarea
          id="detail-info"
          name="detail-info"
          rows={5}
          cols={33}
          placeholder={'제품의 상세 승인 이유에 대해 적어주세요'}
          onChange={handleDescriptionChange}
          value={detailDescription}
        />
        <div className="limit">
          {detailDescription.length} <p>/50</p>
        </div>
      </div>
      <div className="expected-price">
        <div className="title">예상 낙찰가</div>
        <ExaminePriceInput
          title="최소금액"
          targetAtom={examineMinimumPriceAtom}
        />
        <ExaminePriceInput
          title="최대금액"
          targetAtom={examineMaximumPriceAtom}
        />
      </div>
    </Container>
  );
};

export default ExamineApprove;

const Container = styled.div`
  padding-bottom: 30vh;
  .comment-holder {
    display: flex;
    flex-direction: column;
    position: relative;
    margin-bottom: 48px;
    label {
      margin-top: 21px;
      margin-bottom: 24px;
      font-weight: 700;
      font-size: 15px;
    }
    textarea {
      border: 1px solid var(--color-brown200);
      background: none;
      padding: 10px;
      height: 120px;
      resize: none;
    }
    .limit {
      position: absolute;
      right: 10px;
      bottom: 10px;
      display: flex;
      flex-direction: row;
      p {
        margin-left: 3px;
        color: var(--color-brown200);
      }
    }
  }
  .expected-price {
    &::before {
      content: '';
      width: 100%;
      height: 1px;
      display: block;
      border-top: 1px solid var(--color-brown200);
    }
    .title {
      font-weight: 700;
      font-size: 15px;
      margin-top: 21px;
      margin-bottom: 24px;
    }
  }
`;
