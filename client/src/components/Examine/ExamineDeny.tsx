import React from 'react';
import styled from 'styled-components';
import { useAtom } from 'jotai';
import { examineDenyDetailAtom } from '../../store/examineAtom';

const ExamineDeny = () => {
  const [detailDescription, setDetailDescription] = useAtom(
    examineDenyDetailAtom
  );

  const handleDescriptionChange = (
    e: React.ChangeEvent<HTMLTextAreaElement>
  ) => {
    if (e.target.value.length <= 200) {
      setDetailDescription(e.target.value);
    }
  };

  return (
    <Container>
      <div className="comment-holder">
        <label htmlFor="detail-info">반려 사유</label>
        <textarea
          id="detail-info"
          name="detail-info"
          rows={5}
          cols={33}
          placeholder={'제품의 반려 사유에 대해 적어주세요'}
          onChange={handleDescriptionChange}
          value={detailDescription}
        />
        <div className="limit">
          {detailDescription.length} <p>/200</p>
        </div>
      </div>
    </Container>
  );
};

export default ExamineDeny;

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
      height: 200px;
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
`;
