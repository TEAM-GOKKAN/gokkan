import ImageDropZone from './ImageDropzone';
import { BsTrash } from 'react-icons/bs';
import ImageSlider from '../../common/ImageSlider';
import styled from 'styled-components';

const SliderElement = styled.div`
  width: 100%;
  height: 100%;
  position: relative;
  img {
    width: 100%;
    height: 255px;
    object-fit: cover;
  }
  .delete {
    background-color: var(--color-orange);
    position: absolute;
    right: 0;
    top: 0;
    width: 30px;
    height: 30px;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    svg {
      width: 18px;
      height: 18px;
      color: var(--color-brown100);
    }
  }
`;

const ImageSwiper = ({
  imageUrlList,
  handleDeleteButton,
  preTreatment,
}: ImageSwiperProp) => {
  return (
    <ImageSlider>
      {imageUrlList.map((url) => {
        return (
          <SliderElement className="img-swiper-wrapper" key={url}>
            <img src={url} alt="입력된 이미지" />
            <button
              className="delete"
              onClick={() => {
                handleDeleteButton(url);
              }}
            >
              <BsTrash />
            </button>
          </SliderElement>
        );
      })}
      <ImageDropZone preTreatment={preTreatment} />
    </ImageSlider>
  );
};

type ImageSwiperProp = {
  imageUrlList: string[];
  handleDeleteButton: (targetUrl: string) => void;
  preTreatment: (rawImageFileList: File[]) => void;
};

export default ImageSwiper;
