import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { useAtom } from 'jotai';
import {
  examineApproveDetailAtom,
  examineDenyDetailAtom,
  examineCheckAtom,
  examineMaximumPriceAtom,
  examineMinimumPriceAtom,
  resetExamineData,
} from '../../store/examineAtom';
import customAxios from '../../utils/customAxios';
import { useParams, useNavigate } from 'react-router-dom';

const ExamineSubmitButton = () => {
  const [isActive, setIsActive] = useState(false);
  const [examineCheck] = useAtom(examineCheckAtom);
  const [examineApproveDetail] = useAtom(examineApproveDetailAtom);
  const [examineDenyDetail] = useAtom(examineDenyDetailAtom);
  const [examineMinimumPrice] = useAtom(examineMinimumPriceAtom);
  const [examineMaximumPrice] = useAtom(examineMaximumPriceAtom);
  const [, resetExamine] = useAtom(resetExamineData);
  const params = useParams();
  const navigate = useNavigate();

  const handleSubmitButtonClick = () => {
    if (isActive) {
      // 전송할 데이터 만들어줌
      const { itemId } = params;
      const comment =
        examineCheck === 'COMPLETE' ? examineApproveDetail : examineDenyDetail;
      const expertComment = {
        itemId: Number(itemId),
        comment,
        minPrice: Number(examineMinimumPrice.replace(/,/g, '')),
        maxPrice: Number(examineMaximumPrice.replace(/,/g, '')),
        status: examineCheck,
      };
      // 데이터를 서버에 전송해줌(application json 형태로 전송함)
      customAxios
        .post('api/v1/expert/comment', expertComment)
        .then(() => {
          // 성공했을 때, 데이터 초기화 및 목록으로 돌아감
          resetExamine();
          navigate('/expertWorkList');
        })
        .catch((err) => {
          console.log(err);
        });
    }
  };

  useEffect(() => {
    if (examineCheck === 'RETURN') {
      if (examineDenyDetail !== '') {
        setIsActive(true);
      } else {
        setIsActive(false);
      }
    }
    if (examineCheck === 'COMPLETE') {
      const minimumPrice = Number(examineMinimumPrice.replace(/,/g, ''));
      const maximumPrice = Number(examineMaximumPrice.replace(/,/g, ''));
      if (
        examineApproveDetail !== '' &&
        minimumPrice >= 10000 &&
        maximumPrice >= minimumPrice
      ) {
        setIsActive(true);
      } else {
        setIsActive(false);
      }
    }
  }, [
    examineCheck,
    examineDenyDetail,
    examineApproveDetail,
    examineMinimumPrice,
    examineMaximumPrice,
  ]);

  return (
    <Container data-active={isActive} onClick={handleSubmitButtonClick}>
      제출
    </Container>
  );
};

export default ExamineSubmitButton;

const Container = styled.button`
  width: 100%;
  height: 50px;
  position: fixed;
  bottom: 0;
  left: 0;
  background-color: var(--color-brown500);
  color: var(--color-brown100);
  display: flex;
  justify-content: center;
  align-items: center;
  &[data-active='false'] {
    background-color: var(--color-brown300);
  }
`;
