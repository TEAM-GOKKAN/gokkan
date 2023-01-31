import React from 'react';
import { useNavigate } from 'react-router-dom';

const RegisterMyWritingLot = () => {
  const navigate = useNavigate();

  const handleWritingLotClick = () => {
    navigate('/myWritingProduct');
  };

  return <div onClick={handleWritingLotClick}>작성 중인 경매 불러오기</div>;
};

export default RegisterMyWritingLot;
