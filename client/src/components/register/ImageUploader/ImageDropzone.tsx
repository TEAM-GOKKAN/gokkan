import { DropEvent, useDropzone, FileRejection } from 'react-dropzone';
import styled from 'styled-components';
import { BiImageAdd } from 'react-icons/bi';

const StyledInputFileArea = styled.div`
  width: 100%;
  height: 255px;
  display: flex;
  justify-content: center;
  align-items: center;
  svg {
    width: 72px;
    height: 72px;
    color: var(--color-brown200);
  }
`;

const ImageDropZone = ({ preTreatment }: ImageDropzonePropType) => {
  // 파일이 입력되었을 때, 실행해줄 함수 선언
  const onDrop = <T extends File>(
    acceptedFiles: T[],
    fileRejections: FileRejection[],
    event: DropEvent
  ) => {
    if (acceptedFiles.length > 0) {
      preTreatment(acceptedFiles);
    }
  };

  // 드랍 존에 함수 설정 및 이미지 파일들만 받도록 설정해줌
  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: {
      'image/*': [],
    },
  });

  return (
    <StyledInputFileArea {...getRootProps()}>
      <input {...getInputProps()} />
      <BiImageAdd />
    </StyledInputFileArea>
  );
};

type ImageDropzonePropType = {
  preTreatment: (rawImageFileList: File[]) => void;
};

export default ImageDropZone;
