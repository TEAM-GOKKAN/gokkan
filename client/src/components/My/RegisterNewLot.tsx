import React from 'react';
import customAxios from '../../utils/customAxios';
import { useNavigate } from 'react-router-dom';
import { productIdAtom } from '../../store/registerAtom';
import { useAtom } from 'jotai';

const RegisterNewLot = () => {
  const navigate = useNavigate();
  const [productId, setProductId] = useAtom(productIdAtom);

  const handleMakeNewLot = () => {
    customAxios
      .post('api/v1/items/temp')
      .then(({ data }) => {
        setProductId(String(data));
        navigate(`../register/1/${data}`);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return <div onClick={handleMakeNewLot}>새로운 경매 등록</div>;
};

export default RegisterNewLot;
