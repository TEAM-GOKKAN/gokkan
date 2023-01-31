import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { userPhoneAtom, userPhoneCheckAtom } from '../../store/signUpAtom';
import { useAtom } from 'jotai';
import queryString from 'query-string';
import axios from 'axios';

const PhoneWrapper = styled.div`
  display: flex;
  flex-direction: column;
  margin-bottom: 36px;
  font-family: Poppins, 'Noto Sans KR', sans-serif;
  .phone-title {
    font-weight: 500;
    font-size: 14px;
    line-height: 20px;
    letter-spacing: -4%;
    margin-bottom: 10px;
  }
  .button-wrapper {
    display: flex;
    align-items: center;
    input {
      width: calc(100% - 86px);
      padding: 10px 12px;
      border: 1px solid var(--color-brown100);
      font-size: 15px;
      line-height: 21px;
      letter-spacing: normal;
    }
    button {
      width: 76px;
      margin-left: 10px;
      background-color: var(--color-brown200);
      height: 42px;
      &[data-submit='true'][data-check=''] {
        background-color: var(--color-brown300);
        color: var(--color-brown100);
      }
      &[data-check='false'] {
        background-color: var(--color-orange);
        color: var(--color-brown100);
      }
    }
  }
  .available {
    font-size: 12px;
    color: var(--color-purple);
    margin-top: 4px;
  }
  .unavailable {
    font-size: 12px;
    color: var(--color-orange);
    margin-top: 4px;
  }
`;

const UserPhone = () => {
  const [phoneNumber, setPhoneNumber] = useAtom(userPhoneAtom);
  const [phoneCheck, setPhoneCheck] = useAtom(userPhoneCheckAtom);
  const [submitPossible, setSubmitPossible] = useState(false);

  const phoneInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    const regex = /^[0-9\b -]{0,13}$/;
    if (regex.test(e.target.value)) {
      setPhoneNumber(e.target.value);
    }
  };
  const phoneCertificate = () => {
    if (phoneNumber.length !== 13) {
      return;
    }
    //@ts-ignore
    const { IMP } = window;
    IMP.init('imp36780150');
    IMP.certification(
      {
        merchant_uid: 'ORD20180131-0000011', // 주문 번호
        m_redirect_url:
          'http://gokkan.s3-website.ap-northeast-2.amazonaws.com/signup', //redirect
      },
      () => {}
    );
  };

  useEffect(() => {
    if (phoneNumber.length === 10) {
      setPhoneNumber(phoneNumber.replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3'));
    }
    if (phoneNumber.length === 13) {
      setPhoneNumber(
        phoneNumber
          .replace(/-/g, '')
          .replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3')
      );
      setSubmitPossible(true);
    } else {
      setSubmitPossible(false);
    }
  }, [phoneNumber]);

  useEffect(() => {
    setPhoneCheck('');
    const queryParam = queryString.parse(window.location.search);
    const uid = queryParam?.imp_uid;
    const success = queryParam?.success;
    if (success) {
      axios
        .get('http://3.38.59.40:8080/api/v1/certification', {
          params: { imp_uid: uid },
        })
        .then(({ data }) => {
          const targetPhoneNumber = phoneNumber.replace(/-/g, '');
          const checkData = data.replace(/-/g, '');
          console.log(checkData);
          console.log(targetPhoneNumber);
          if (checkData === targetPhoneNumber) {
            setPhoneCheck('true');
          } else {
            setPhoneCheck('false');
          }
        })
        .catch((err) => {
          console.log(err);
        });
    }
  }, []);

  return (
    <PhoneWrapper>
      <div className="phone-title">휴대전화</div>
      <div className="button-wrapper">
        <input
          type="text"
          onChange={phoneInput}
          value={phoneNumber}
          placeholder="010-xxxx-xxxx"
          data-check={phoneCheck}
        />
        <button
          onClick={phoneCertificate}
          data-submit={submitPossible}
          data-check={phoneCheck}
        >
          {phoneCheck === 'false' && '재시도'}
          {phoneCheck !== 'false' && '인증'}
        </button>
      </div>
      {phoneCheck === 'true' && (
        <div className="available">본인인증을 완료하였습니다.</div>
      )}
      {phoneCheck === 'false' && (
        <div className="unavailable">본인인증에 실패하셨습니다.</div>
      )}
    </PhoneWrapper>
  );
};

export default UserPhone;
