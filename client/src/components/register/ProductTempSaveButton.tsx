import React, { Dispatch } from 'react';
import {
  productGetInfoAtom,
  uploadImageFileAtom,
  examineImageFileAtom,
} from '../../store/registerAtom';
import { useAtom } from 'jotai';
import customAxios from '../../utils/customAxios';

const ProductTempSaveButton = ({ setLoading }: ProductTempSaveButtonProps) => {
  const [productInfo] = useAtom(productGetInfoAtom);
  const [uploadImgFile] = useAtom(uploadImageFileAtom);
  const [examineImgFile] = useAtom(examineImageFileAtom);

  const handleTempSaveButtonClick = () => {
    setLoading(true);
    const transferData = new FormData();
    const requestData = new Blob([JSON.stringify(productInfo)], {
      type: 'application/json',
    });
    transferData.append('request', requestData);

    // 파일이 비워져 있을 때, 빈 배열을 보내기 위한 데이터
    const nullData = new Blob([], {
      type: 'application/json',
    });

    if (uploadImgFile.length !== 0) {
      uploadImgFile.forEach((uploadImg) => {
        transferData.append('imageItemFiles', uploadImg);
      });
    } else {
      transferData.append('imageItemFiles', nullData);
    }

    if (examineImgFile.length !== 0) {
      examineImgFile.forEach((examineImg) => {
        transferData.append('imageCheckFiles', examineImg);
      });
    } else {
      transferData.append('imageCheckFiles', nullData);
    }

    customAxios
      .put('api/v1/items', transferData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      })
      .then((res) => {
        setLoading(false);
      })
      .catch((err) => {
        setLoading(false);
        console.log(err);
      });
  };

  return (
    <button className="save" onClick={handleTempSaveButtonClick}>
      저장
    </button>
  );
};

type ProductTempSaveButtonProps = {
  setLoading: Dispatch<React.SetStateAction<boolean>>;
};

export default ProductTempSaveButton;
