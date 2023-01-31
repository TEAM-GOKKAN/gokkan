import { useEffect } from 'react';
import customAxios from '../../utils/customAxios';
import { useAtom } from 'jotai';
import {
  setInitialUserInfo,
  userNickNameCheckAtom,
  userPhoneCheckAtom,
} from '../../store/signUpAtom';

const useGetUserInfo = () => {
  const [, setUserInfo] = useAtom(setInitialUserInfo);
  const [, setNickNameCheck] = useAtom(userNickNameCheckAtom);
  const [, setPhoneCheck] = useAtom(userPhoneCheckAtom);

  useEffect(() => {
    customAxios
      .get('api/v1/users')
      .then(({ data }) => {
        // 서버에 저장된 초기 사용자 정보를 입력해줌
        setUserInfo(data);
        if (data.phoneNumber) {
          setNickNameCheck('true');
          setPhoneCheck('true');
        }
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);
};

export default useGetUserInfo;
