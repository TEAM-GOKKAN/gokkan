import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import styled from 'styled-components';
import { AiOutlineClose } from 'react-icons/ai';
import PageOne from '../components/register/pageOne';
import PageTwo from '../components/register/pageTwo';
import PageThree from '../components/register/pageThree';
import PageFour from '../components/register/pageFour';
import ProductTempSaveButton from '../components/register/ProductTempSaveButton';
import { useAtom } from 'jotai';
import { productIdAtom, registerPageLoadingAtom } from '../store/registerAtom';
import useProductTempInfo from '../components/register/useProductTempInfo';
import LoadingIndicator from '../components/common/LoadingIndicator';
import { useNavigate } from 'react-router-dom';

const RegisterWrapper = styled.div`
  header {
    width: 100%;
    height: 60px;
    display: flex;
    flex-direction: row;
    margin-bottom: 32px;
    align-items: center;
    justify-content: space-between;
    padding: 16px;
    button {
      width: 18px;
      height: 18px;
    }
    .title {
      width: calc(100% - 120px);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 16px;
      font-weight: 500;
      line-height: 23px;
    }
    .save {
      color: var(--color-purple);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: var(--font-small);
    }
  }
`;

const LoadingIndicatorWrapper = styled.div`
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const AuctionRegister = () => {
  const { pageNumber, productId } = useParams();
  const [itemId, setItemId] = useAtom(productIdAtom);
  const [pageName, setPageName] = useState('');
  const [loading, setLoading] = useAtom(registerPageLoadingAtom);
  const navigate = useNavigate();

  const loadingFinish = () => {
    setLoading(false);
  };

  useProductTempInfo(String(productId), loadingFinish);

  useEffect(() => {
    switch (pageNumber) {
      case '1':
        setPageName('사진 업로드');
        break;
      case '2':
        setPageName('주요 정보 입력');
        break;
      case '3':
        setPageName('상세 정보 입력');
        break;
      default:
        setPageName('시작가 입력');
    }
  }, [pageNumber]);

  // product Id를 저장해줌
  useEffect(() => {
    setItemId(String(productId));
  }, []);

  const handleCancleButtonClick = () => {
    navigate('/');
  };

  if (loading) {
    return (
      <LoadingIndicatorWrapper>
        <LoadingIndicator />
      </LoadingIndicatorWrapper>
    );
  }

  return (
    <RegisterWrapper>
      <header>
        <button onClick={handleCancleButtonClick}>
          <AiOutlineClose size="18px" />
        </button>
        <div className="title">{pageName}</div>
        <ProductTempSaveButton setLoading={setLoading} />
      </header>
      {pageNumber === '1' && <PageOne />}
      {pageNumber === '2' && <PageTwo />}
      {pageNumber === '3' && <PageThree />}
      {pageNumber === '4' && <PageFour />}
    </RegisterWrapper>
  );
};

export default AuctionRegister;
