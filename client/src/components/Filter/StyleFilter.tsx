import React from 'react';
import styled from 'styled-components';
import { AiOutlineCheckSquare } from 'react-icons/ai';
import { useAtom } from 'jotai';
import { styleFilterAtom } from '../../store/filterAtom';

const StyleFilter = () => {
  const [styleTargetList, setstyleTargetList] = useAtom(styleFilterAtom);

  const styleList = [
    'Art Deco',
    'Mid-Century Modern',
    'Space Age',
    'Memphis',
    'Industrial',
    'Contemporary',
  ];

  const handleStyleItemClick = (item: string) => {
    if (styleTargetList.includes(item)) {
      setstyleTargetList((pre) =>
        pre.filter((element) => {
          if (element === item) {
            return false;
          } else {
            return true;
          }
        })
      );
    } else {
      setstyleTargetList((pre) => [...pre, item]);
    }
  };

  return (
    <Container>
      <div className="title">스타일</div>
      <ul>
        {styleList.map((item) => {
          const focused = styleTargetList.includes(item);
          return (
            <li
              onClick={() => {
                handleStyleItemClick(item);
              }}
              key={item}
            >
              <div className="icon-holder" data-focused={focused}>
                <AiOutlineCheckSquare />
              </div>
              <p className="content">{item}</p>
            </li>
          );
        })}
      </ul>
    </Container>
  );
};

export default StyleFilter;

const Container = styled.div`
  margin-bottom: 48px;
  .title {
    font-weight: 700;
    font-size: 16px;
    margin-bottom: 16px;
    line-height: 23px;
    letter-spacing: -4%;
  }
  li {
    width: 100%;
    height: 30px;
    display: flex;
    flex-direction: row;
    align-items: center;
    margin-bottom: 8px;
    .content {
      margin-left: 10px;
    }
    .icon-holder {
      color: var(--color-brown200);
      &[data-focused='true'] {
        color: var(--color-brown500);
      }
    }
  }
`;
