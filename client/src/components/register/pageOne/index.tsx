import { useEffect, useState } from 'react';
import styled from 'styled-components';
import ImageUploader from '../ImageUploader';
import {
  examineImageFileAtom,
  examineImageUrlAtom,
  examineDbImageUrlAtom,
  uploadImageFileAtom,
  uploadImageUrlAtom,
  uploadDbImageUrlAtom,
} from '../../../store/registerAtom';
import { useNavigate, useParams } from 'react-router-dom';
import { useAtom } from 'jotai';

const RegisterPageOneWrapper = styled.div`
  padding: 0px 12px;
  height: calc(100vh - 92px);
  position: relative;
  .section-title {
    margin-bottom: 10px;
    font-weight: 500;
    font-size: 14px;
    line-height: 20px;
    letter-spacing: -4%;
  }
  .product-picture {
    margin-bottom: 42px;
    height: 300px;
  }
  .next {
    width: calc(100vw + 1px);
    height: 50px;
    background-color: var(--color-brown300);
    color: var(--color-brown100);
    margin: 0 -12px;
    position: absolute;
    bottom: 0;
    &[data-active='true'] {
      background-color: var(--color-brown500);
    }
  }
`;

const PageOne = () => {
  const [buttonActive, setButtonActive] = useState(false);
  const [imageUrl] = useAtom(uploadImageUrlAtom);
  const [imageExamineUrl] = useAtom(examineImageUrlAtom);
  const navigate = useNavigate();
  const { pageNumber, productId } = useParams();
  const handleNextButtonClick = () => {
    if (buttonActive) {
      navigate(`/register/2/${productId}`);
    } else {
      window.alert('사진을 넣어주세요');
    }
  };

  useEffect(() => {
    if (imageUrl.length > 0 && imageExamineUrl.length > 0) {
      setButtonActive(true);
    } else {
      setButtonActive(false);
    }
  }, [imageUrl.length, imageExamineUrl.length]);

  return (
    <RegisterPageOneWrapper>
      <div className="product-picture">
        <div className="section-title">제품 사진</div>
        <ImageUploader
          fileAtom={uploadImageFileAtom}
          urlAtom={uploadImageUrlAtom}
          dbUrlAtom={uploadDbImageUrlAtom}
        />
      </div>
      <div className="product-picture">
        <div className="section-title">검수용 사진</div>
        <ImageUploader
          fileAtom={examineImageFileAtom}
          urlAtom={examineImageUrlAtom}
          dbUrlAtom={examineDbImageUrlAtom}
        />
      </div>
      <button
        className="next"
        onClick={handleNextButtonClick}
        data-active={buttonActive}
      >
        다음
      </button>
    </RegisterPageOneWrapper>
  );
};

export default PageOne;
