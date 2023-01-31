import React, { ChangeEvent } from 'react';
import styled from 'styled-components';

interface IProps {
  onChangeCheckbox: (e: ChangeEvent<HTMLInputElement>) => void;
}

export default function PaymentTerms({ onChangeCheckbox }: IProps) {
  return (
    <Container>
      <TermContainer>
        <CheckBox type="checkbox" id="terms" onChange={onChangeCheckbox} />
        <Label htmlFor="terms">
          <div>이용약관에 모두 동의합니다.</div>
          <div>
            주문 상품정보 및 결제대행 서비스 이용약관에 모두 동의합니다.
          </div>
        </Label>
      </TermContainer>
    </Container>
  );
}

const Container = styled.div`
  width: 100%;
  padding-bottom: 98px;
  font-size: var(--font-small);

  &::before {
    content: '';
    display: inline-block;
    margin-bottom: 21px;
    border-bottom: 1px solid var(--color-brown100);
    width: calc(100% + 32px);
    position: relative;
    right: 16px;
  }
`;

const CheckBox = styled.input`
  padding: 0;
  margin: 0;

  appearance: none;
  width: 1rem;
  height: 1rem;
  border: 1px solid var(--color-brown200);
  /* background: var(--color-brown100); */
  background: none;
  background-image: url("data:image/svg+xml,%3Csvg width='18' height='18' viewBox='0 0 18 18' fill='none' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M7.08745 13.3875L2.88745 9.18745L3.6937 8.3812L7.08745 11.775L14.2875 4.57495L15.0937 5.3812L7.08745 13.3875Z' fill='%23D9D6D4'/%3E%3C/svg%3E ");
  background-size: 100% 100%;
  background-position: 50%;
  background-repeat: no-repeat;

  &:checked {
    border-color: var(--color-brown500);
    background-image: url("data:image/svg+xml,%3Csvg width='18' height='18' viewBox='0 0 18 18' fill='none' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M7.08745 13.3875L2.88745 9.18745L3.6937 8.3812L7.08745 11.775L14.2875 4.57495L15.0937 5.3812L7.08745 13.3875Z' fill='%236C6158'/%3E%3C/svg%3E%0A");
    background-size: 100% 100%;
    background-position: 50%;
    background-repeat: no-repeat;
    /* background-color: var(--color-brown300); */
  }
`;

const Label = styled.label`
  & > div:last-child {
    margin-top: 6px;
    font-size: var(--font-micro);
    color: var(--color-brown300);
  }
`;

const TermContainer = styled.div`
  display: flex;
  align-items: flex-start;
  gap: 8px;
`;
