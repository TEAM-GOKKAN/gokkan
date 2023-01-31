import React from 'react';
import { productStyleAtom } from '../../../store/registerAtom';
import { useAtom } from 'jotai';
import styled from 'styled-components';

const ProductStyleWrapper = styled.div`
  margin-bottom: 40px;
  .title {
    margin-bottom: 10px;
    font-weight: 500;
    font-size: 14px;
    line-height: 20px;
    letter-spacing: -4%;
  }
  ul {
    width: 100%;
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
  }
  li {
    padding-top: 9px;
    padding-left: 12px;
    padding-right: 12px;
    padding-bottom: 10px;
    font-family: 'Poppins';
    letter-spacing: normal;
    font-weight: 400;
    font-size: 15px;
    line-height: 22.5px;
    color: var(--color-brown200);
    border: 1px solid var(--color-brown200);
    margin: 5px;
    &[data-active='true'] {
      color: var(--color-brown500);
      border: 1px solid var(--color-brown500);
    }
  }
`;

const ProductStyle = () => {
  const [productStyle, setProductStyle] = useAtom(productStyleAtom);

  const styleList = [
    'Art Deco',
    'Mid-Century Modern',
    'Space Age',
    'Memphis',
    'Industrial',
    'Contemporary',
  ];

  const handleStyleButtonClick = (element: string) => {
    if (productStyle.includes(element)) {
      const newStyledList = productStyle.filter((listElement) => {
        if (listElement === element) {
          return false;
        } else {
          return true;
        }
      });
      setProductStyle(newStyledList);
    } else {
      setProductStyle((pre) => {
        return [...pre, element];
      });
    }
  };

  return (
    <ProductStyleWrapper>
      <div className="title">스타일</div>
      <ul>
        {styleList.map((element) => {
          let buttonActive = false;
          if (productStyle.includes(element)) {
            buttonActive = true;
          }
          return (
            <li
              data-active={buttonActive}
              key={element}
              onClick={() => {
                handleStyleButtonClick(element);
              }}
            >
              {element}
            </li>
          );
        })}
      </ul>
    </ProductStyleWrapper>
  );
};

export default ProductStyle;
