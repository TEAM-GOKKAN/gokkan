import React from 'react';
import styled from 'styled-components';
import Slider from 'react-slick';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';

export default function ImageSlider({
  children,
}: {
  children: React.ReactNode;
}) {
  const settings = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
  };

  return <StyledSlider {...settings}>{children}</StyledSlider>;
}

const StyledSlider = styled(Slider)`
  width: 100%;
  height: 255px;

  .slick-list {
    width: 100%;
    height: 100%;
    margin: 0 auto;
    overflow-x: hidden;
    background: var(--color-brown100);
  }

  .slick-slide div {
    cursor: pointer;
  }

  .slick-dots {
    bottom: 6px;

    & > li {
      width: 8px;
      height: 8px;
      padding: 5px;
      margin: 0 4px;
      position: static;

      &.slick-active > button {
        background: var(--color-brown300);
      }

      & > button {
        background: var(--color-brown200);
        width: 100%;
        height: 100%;
        padding: 4px;
      }

      & > button::before {
        content: '';
        width: 0px;
        height: 0px;
        position: static;
      }
    }
  }

  .slic-active {
    /*  */
  }

  .slick-track {
    width: 100%;
  }
`;
