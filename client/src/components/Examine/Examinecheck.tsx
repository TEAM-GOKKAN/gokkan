import React from 'react';
import styled from 'styled-components';
import { useAtom } from 'jotai';
import { examineCheckAtom } from '../../store/examineAtom';

const Examinecheck = () => {
  const [isApprove, setIsApprove] = useAtom(examineCheckAtom);

  const handleApproveClick = () => {
    setIsApprove('COMPLETE');
  };

  const handleDenyClick = () => {
    setIsApprove('RETURN');
  };

  return (
    <Container>
      <div className="title">승인 여부</div>
      <div className="button-holder">
        <button
          onClick={handleApproveClick}
          data-focused={isApprove === 'COMPLETE'}
        >
          승인
        </button>
        <button onClick={handleDenyClick} data-focused={isApprove === 'RETURN'}>
          반려
        </button>
      </div>
    </Container>
  );
};

export default Examinecheck;

const Container = styled.div`
  &::after {
    content: '';
    width: 100%;
    height: 1px;
    margin-top: 42px;
    display: block;
    border-bottom: 1px solid var(--color-brown200);
  }
  .title {
    font-weight: 700;
    font-size: 15px;
    margin-bottom: 24px;
  }
  .button-holder {
    width: 100%;
    height: 42px;
    margin-bottom: 48px;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    button {
      height: 100%;
      width: 100%;
      border: 1px solid var(--color-brown300);
      margin: 5px;
      &[data-focused='true'] {
        border: 1px solid var(--color-brown500);
        color: var(--color-brown500);
      }
    }
  }
`;
