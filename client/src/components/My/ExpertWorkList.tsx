import React, { useState, useEffect } from 'react';
import SpreadList from '../common/SpreadList';
import customAxios from '../../utils/customAxios';
import { useNavigate } from 'react-router-dom';

const ExpertWorkList = () => {
  const [isExpert, setIsExpert] = useState(false);
  const navigate = useNavigate();

  const handleWorkListClick = () => {
    navigate('/expertWorkList');
  };

  // 해당 컴포넌트가 로드될 때, 전문가인지 확인함
  useEffect(() => {
    const checkUrl = 'api/v1/expert/info/is-expert';
    customAxios
      .get(checkUrl)
      .then(({ data }) => {
        if (data) {
          setIsExpert(true);
        }
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  if (!isExpert) {
    return <></>;
  }

  // 전문가 일때만 검수 목록을 보여주도록 함
  return <SpreadList title="검수 목록" clickFn={handleWorkListClick} />;
};

export default ExpertWorkList;
