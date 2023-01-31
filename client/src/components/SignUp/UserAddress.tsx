import React, { useState } from 'react';
import { userAddressAtom, userAddressDetailAtom } from '../../store/signUpAtom';
import { useAtom } from 'jotai';
import styled from 'styled-components';
import DaumPostcode from 'react-daum-postcode';

const AddressWrapper = styled.div`
  display: flex;
  flex-direction: column;
  margin-bottom: 36px;
  font-family: Poppins, 'Noto Sans KR', sans-serif;
  .address-title {
    font-weight: 500;
    font-size: 14px;
    line-height: 20px;
    letter-spacing: -4%;
  }
  .address-find {
    width: 100%;
    margin: 10px 0px;
    display: flex;
    flex-direction: row;
    .address {
      width: calc(100% - 84px);
      height: 42px;
      padding: 10px 12px;
      font-weight: 400;
      font-size: 15px;
      line-height: 22px;
      letter-spacing: -4%;
      overflow-x: scroll;
      overflow-y: hidden;
      background-color: var(--color-brown100);
    }
    button {
      width: 80px;
      height: 42px;
      background-color: var(--color-purple);
      color: var(--color-brown100);
      margin-left: 10px;
    }
  }
  input {
    width: 100%;
    padding: 10px 12px;
    border: 1px solid var(--color-brown100);
    font-size: 15px;
    line-height: 21px;
    letter-spacing: -4%;
    font-weight: 400;
  }
`;

const AddressDetailWrapper = styled.div`
  input {
    margin-left: calc(54px + 2vw);
    width: 72vw;
    font-size: 18px;
    margin-top: 1vh;
    border: 1px solid black;
    padding-left: 2vw;
  }
`;

const AddressFindWrapper = styled.div`
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  position: absolute;
  z-index: 10;
  background-color: white;
  display: flex;
  justify-content: center;
  .daum-address {
    margin-top: 5vh;
    width: 90%;
  }
`;

const UserAddress = () => {
  const [address, setAddress] = useAtom(userAddressAtom);
  const [addressDetail, setAddressDetail] = useAtom(userAddressDetailAtom);
  const [showAddress, setShowAddress] = useState(false);

  const handleAddressButtonClick = () => {
    setShowAddress(true);
  };

  const onCompletePost = (data: any) => {
    console.log(data);
    setAddress(String(data.address));
    setShowAddress(false);
  };

  const inputAddressDetail = (e: React.ChangeEvent<HTMLInputElement>) => {
    setAddressDetail(e.target.value);
  };

  return (
    <AddressWrapper>
      <div className="address-title">주소</div>
      <div className="address-find">
        <div className="address">{address}</div>
        <button onClick={handleAddressButtonClick}>주소 검색</button>
      </div>
      <input
        type="text"
        placeholder="상세 주소를 입력해주세요"
        value={addressDetail}
        onChange={inputAddressDetail}
      />
      {showAddress && (
        <AddressFindWrapper>
          <div className="daum-address">
            <DaumPostcode onComplete={onCompletePost} />
          </div>
        </AddressFindWrapper>
      )}
    </AddressWrapper>
  );
};

export default UserAddress;
