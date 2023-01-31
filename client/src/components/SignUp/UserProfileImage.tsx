import styled from 'styled-components';

const ProfileImgWrapper = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
  margin-bottom: 100px;
  .profile-title {
    font-weight: 500;
    font-size: 14px;
    line-height: 20px;
    letter-spacing: -4%;
  }
  img {
    margin-top: 10px;
    width: 100%;
    height: calc(100vw - 24px);
    object-fit: cover;
  }
  button {
    margin-top: 2vh;
  }
  input {
    display: none;
  }
  p {
    color: red;
  }
`;

//props 타입 정리
type Info = {
  imgUrl: string;
  profileChangeButtonClick: () => void;
  changeImg: (e: React.ChangeEvent<HTMLInputElement>) => void;
  fileInputRef: null | React.RefObject<HTMLInputElement>;
  warn: string;
};
type InfoProps = {
  info: Info;
};

const UserProfileImage = ({ info }: InfoProps) => {
  const { imgUrl, profileChangeButtonClick, changeImg, fileInputRef, warn } =
    info;
  return (
    <ProfileImgWrapper>
      <div className="profile-title">프로필 사진</div>
      <img
        alt="유저 프로필 이미지"
        src={imgUrl}
        onClick={profileChangeButtonClick}
      />
      <input
        type="file"
        accept="image/jpg,impge/png,image/jpeg,image/gif"
        onChange={changeImg}
        ref={fileInputRef}
      />
      {warn !== '' && <p>*{warn}</p>}
    </ProfileImgWrapper>
  );
};

export default UserProfileImage;
