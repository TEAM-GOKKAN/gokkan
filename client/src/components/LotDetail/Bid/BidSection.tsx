import { useAtom } from 'jotai';
import React, {
  ChangeEvent,
  Dispatch,
  MouseEvent,
  SetStateAction,
  useEffect,
  useState,
} from 'react';
import styled from 'styled-components';
import useInput from '../../../lib/hooks/useInput';
import { bidErrMsgAtom } from '../../../store/bidAtom';
import { insertCommas, removeCommas } from '../../../utils/handleCommas';
import { MdOutlineErrorOutline } from 'react-icons/md';
import SignInPage from '../../../pages/SignInPage';

interface Iprops {
  currentPrice: number | string | undefined;
  hasBid: boolean;
  onConfirmOpen: () => void;
  onSetBidPrice: (price: number) => void;
  onSetAutoBid: Dispatch<SetStateAction<boolean>>;
}

export default function BidSection({
  currentPrice,
  hasBid,
  onConfirmOpen,
  onSetBidPrice,
  onSetAutoBid,
}: Iprops) {
  const [recommendedBidPrices, setRecommendedBidPrices] = useState<number[]>(
    []
  );
  const [bidErrMsg, setBidErrMsg] = useAtom(bidErrMsgAtom);
  const [value, , setValue] = useInput<string>('');
  const [loginModal, setLoginModal] = useState(false);

  const closeLoginModal = () => {
    setLoginModal(false);
  };

  const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    setBidErrMsg('');

    const inputPriceWithCommas = insertCommas(removeCommas(e.target.value));
    setValue(inputPriceWithCommas);
  };

  // 모달 띄우기
  const openConfirmModal = (price: string, isButton = false) => {
    setBidErrMsg('');

    if (!isButton) {
      // 토큰이 없을 경우 에러 메세지
      const accessToken = localStorage.getItem('accessToken');
      if (!accessToken) {
        setBidErrMsg(`로그인 후 응찰해주세요.`);
        setLoginModal(true);
        return;
      }

      // 응찰가가 현재가보다 낮을 경우 에러 메세지
      if (Number(removeCommas(price)) < recommendedBidPrices[0]) {
        const minBidPriceWithCommas = insertCommas(recommendedBidPrices[0]);
        setBidErrMsg(`${minBidPriceWithCommas}원 이상부터 응찰 가능합니다.`);
        return;
      }
    }

    const bidPriceWithoutCommas = Number(removeCommas(price));
    onSetBidPrice(bidPriceWithoutCommas);
    onConfirmOpen();
  };

  const handleClickBidButton = () => {
    onSetAutoBid(false);
    openConfirmModal(value);
  };

  const handleClickAutoBidButton = () => {
    onSetAutoBid(true);
    openConfirmModal(value);
  };

  // 추천 응찰가 클릭
  const handleClickRecommendedBidButton = (
    e: MouseEvent<HTMLButtonElement>
  ) => {
    if (e.target instanceof HTMLButtonElement && e.target.textContent) {
      setBidErrMsg('');
      onSetAutoBid(false);
      openConfirmModal(e.target.textContent, true);
    }
  };

  useEffect(() => {
    setBidErrMsg('');
  }, []);

  useEffect(() => {
    if (!currentPrice) return;

    const minBidPrice = hasBid
      ? Number(currentPrice) + 10000
      : Number(currentPrice);
    setRecommendedBidPrices([
      minBidPrice,
      minBidPrice + 10000,
      minBidPrice + 20000,
    ]);
  }, [currentPrice]);

  return (
    <>
      <Container>
        <PriceButtonsContainer>
          {recommendedBidPrices.map((price) => (
            <button
              type="button"
              key={price}
              onClick={handleClickRecommendedBidButton}
            >
              {insertCommas(price)}
            </button>
          ))}
        </PriceButtonsContainer>
        <PriceInputContainer>
          <input
            placeholder={`${insertCommas(recommendedBidPrices[0] || 0)}원 이상`}
            value={value}
            onChange={handleInputChange}
            className={bidErrMsg ? 'error' : ''}
          />
          {bidErrMsg && (
            <>
              <StyledWarningIcon />
              <div>{bidErrMsg}</div>
            </>
          )}
        </PriceInputContainer>
        <BidButtonsContainer>
          <BidOnceButton type="button" onClick={handleClickBidButton}>
            1회 응찰
          </BidOnceButton>
          <BidAutoButton type="button" onClick={handleClickAutoBidButton}>
            자동 응찰
          </BidAutoButton>
        </BidButtonsContainer>
      </Container>
      {loginModal && <SignInPage onClose={closeLoginModal} />}
    </>
  );
}

const Container = styled.form`
  margin-bottom: 72px;
  display: flex;
  flex-direction: column;
`;

const PriceButtonsContainer = styled.div`
  margin-bottom: 18px;

  & > button {
    width: calc((100% - 20px) / 3);
    margin-right: 10px;
    background: var(--color-brown100);
    height: 42px;
    font-size: var(--font-regular);
    line-height: var(--font-regular);
    color: var(--color-brown100);
    background: var(--color-brown400);
    font-weight: 400;
    letter-spacing: normal;
    font-family: 'Poppins', sans-serif;
  }
`;

const PriceInputContainer = styled.div`
  margin-bottom: 10px;
  position: relative;

  & > input {
    padding-left: 12px;
    font-size: var(--font-regular);
    line-height: var(--font-regular);
    color: var(--color-brown500);
    letter-spacing: normal;
    font-family: 'Poppins', sans-serif;

    &.error {
      border: 1px solid var(--color-orange);
    }
  }

  & > div {
    margin-top: 6px;
    font-size: var(--font-micro);
    color: var(--color-orange);
  }
`;

const StyledWarningIcon = styled(MdOutlineErrorOutline)`
  position: absolute;
  top: 21px;
  right: 10px;
  transform: translateY(-50%);
  font-size: 20px;
  color: var(--color-orange);
`;

const BidButtonsContainer = styled.div`
  & button {
    width: calc((100% - 10px) / 2);
    margin-right: 10px;
    background: var(--color-brown100);
    height: 42px;
    font-size: var(--font-regular);
    line-height: var(--font-regular);
    color: var(--color-brown100);
    font-weight: 500;
  }
`;

const BidOnceButton = styled.button`
  && {
    background: var(--color-brown500);
  }
`;

const BidAutoButton = styled.button`
  && {
    background: var(--color-purple);
  }
`;
