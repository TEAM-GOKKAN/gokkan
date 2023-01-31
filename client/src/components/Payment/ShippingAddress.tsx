import React from 'react';
import styled from 'styled-components';
import Section from './Section';

interface IProps {
  data: {
    name: string;
    phoneNumber: string;
    address: string;
    addressDetail?: string;
  };
  isPaid?: boolean;
}

export default function ShippingAddress({ data }: IProps) {
  const { name, phoneNumber, address, addressDetail } = data;

  return (
    <Section
      title="배송지"
      preview={addressDetail ? `${address} ${addressDetail}` : address}
    >
      <div>
        <span>{name}</span>
        <span> / </span>
        <PhoneNumber>{phoneNumber}</PhoneNumber>
      </div>
      <Address>
        {addressDetail ? `${address} ${addressDetail}` : address}
      </Address>
    </Section>
  );
}

const Address = styled.address`
  margin-top: 10px;
  color: var(--color-brown300);
`;

const PhoneNumber = styled.span`
  letter-spacing: normal;
`;
