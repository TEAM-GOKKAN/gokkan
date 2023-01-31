import { atom } from 'jotai';
import { atomWithStorage } from 'jotai/utils';

const userNameAtom = atomWithStorage('name', '');
const userNickNameAtom = atomWithStorage('nickname', '');
const userNickNameCheckAtom = atomWithStorage('nicknamecheck', '');
const userAddressAtom = atomWithStorage('address', '');
const userAddressDetailAtom = atomWithStorage('addressdetail', '');
const userPhoneAtom = atomWithStorage('phone', '');
const userPhoneCheckAtom = atomWithStorage('phoneCheck', '');
const userProfileImageUrlAtom = atomWithStorage<string>(
  'imgurl',
  '/src/asset/userDefaultImage.png'
);
const userProfileImageFileAtom = atomWithStorage<string | File>('imgfile', '');
const userCardNumberAtom = atomWithStorage('cardNumber', '');
const clearAtom = atom(null, (get, set, update) => {
  set(userNameAtom, '');
  set(userNickNameAtom, '');
  set(userNickNameCheckAtom, '');
  set(userAddressAtom, '');
  set(userAddressDetailAtom, '');
  set(userPhoneAtom, '');
  set(userPhoneCheckAtom, '');
  set(userProfileImageUrlAtom, '/src/asset/userDefaultImage.png');
  set(userProfileImageFileAtom, '');
});
const setInitialUserInfo = atom(null, (get, set, update: InitialUserInfo) => {
  set(userNameAtom, update.name);
  set(userProfileImageUrlAtom, update.profileImageUrl);
  if (update.address) {
    set(userAddressAtom, update.address);
  }
  if (update.addressDetail) {
    set(userAddressDetailAtom, update.addressDetail);
  }
  if (update.nickName) {
    set(userNickNameAtom, update.nickName);
  }
  if (update.phoneNumber) {
    set(userPhoneAtom, update.phoneNumber);
  }
  if (update.cardNumber) {
    set(userCardNumberAtom, update.cardNumber);
  }
});
const userInfoAtom = atom((get) => {
  const name = get(userNameAtom);
  const nickName = get(userNickNameAtom);
  const address = get(userAddressAtom);
  const addressDetail = get(userAddressDetailAtom);
  const phoneNumber = get(userPhoneAtom);
  const cardNumber = get(userCardNumberAtom);
  return {
    name,
    nickName,
    phoneNumber,
    profileImageUrl: '',
    address,
    addressDetail,
    cardNumber,
  };
});
const userInfoFixAtom = atom('');

type InitialUserInfo = {
  name: string;
  profileImageUrl: string;
  nickName?: string;
  address?: string;
  addressDetail?: string;
  phoneNumber?: string;
  cardNumber?: string;
};

export {
  userNameAtom,
  userNickNameAtom,
  userNickNameCheckAtom,
  userAddressAtom,
  userAddressDetailAtom,
  userPhoneAtom,
  userPhoneCheckAtom,
  userProfileImageUrlAtom,
  userProfileImageFileAtom,
  clearAtom,
  setInitialUserInfo,
  userInfoAtom,
  userInfoFixAtom,
};
