import React, { useEffect } from 'react';
import { useAtom } from 'jotai';
import { initialFilterSettingAtom } from '../../store/filterAtom';

const useFilterInitialSetting = () => {
  const [, initialFilterSetting] = useAtom(initialFilterSettingAtom);

  // 처음 페이지가 로드될 때, atom에 저장된 값으로 초기 값을 setting 해줌
  useEffect(() => {
    initialFilterSetting();
  }, []);
  
};

export default useFilterInitialSetting;
