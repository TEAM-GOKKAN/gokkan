import React from 'react';
import styled from 'styled-components';
import { IoMdRadioButtonOn } from 'react-icons/io';
import { useAtom } from 'jotai';
import { sortFilterAtom } from '../../store/filterAtom';

const SortFilter = () => {
  const [sortTarget, setSortTarget] = useAtom(sortFilterAtom);
  const sortList = ['마감임박순', '신규등록순'];

  const handleSortItemClick = (item: string) => {
    setSortTarget(item);
  };

  return (
    <Container>
      <div className="title">정렬</div>
      <ul>
        {sortList.map((item) => {
          const focused = item === sortTarget;
          return (
            <li
              onClick={() => {
                handleSortItemClick(item);
              }}
              key={item}
            >
              <div className="icon-holder" data-focused={focused}>
                <IoMdRadioButtonOn />
              </div>
              <p className="content">{item}</p>
            </li>
          );
        })}
      </ul>
    </Container>
  );
};

export default SortFilter;

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
