import React from 'react';
import ImageSwiperContainer from './ImageSwiperContainer';
import styled from 'styled-components';
import { PrimitiveAtom } from 'jotai';

const ImgInputWrapper = styled.div`
  width: 100%;
  height: 255px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--color-brown100);
`;

const ImageUploader = ({
  fileAtom,
  urlAtom,
  dbUrlAtom,
}: ImageUploaderPropType) => {
  return (
    <ImgInputWrapper>
      <ImageSwiperContainer
        fileAtom={fileAtom}
        urlAtom={urlAtom}
        dbUrlAtom={dbUrlAtom}
      />
    </ImgInputWrapper>
  );
};

type ImageUploaderPropType = {
  fileAtom: PrimitiveAtom<File[]>;
  urlAtom: PrimitiveAtom<string[]>;
  dbUrlAtom: PrimitiveAtom<ImageUrl[]>;
};

type ImageUrl = {
  id: number;
  url: string;
};
export default ImageUploader;
