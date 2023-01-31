// 남은 시간 구하는 함수
const getRemainingTime = (targetTime: string) => {
  // 마감 시간
  const bidCloseTime = new Date(targetTime);
  // 현재 시간
  const currTime = new Date();
  // 남은 시간 (ms)
  const diff = Number(bidCloseTime) - Number(currTime);

  if (diff < 0) return '마감';

  const diffDay = Math.floor(diff / (1000 * 60 * 60 * 24));
  const diffHour = Math.floor((diff / (1000 * 60 * 60)) % 24);
  const diffMin = Math.floor((diff / (1000 * 60)) % 60);
  const diffSec = Math.floor((diff / 1000) % 60);

  return `${diffDay}일 ${diffHour}시간 ${diffMin}분 ${diffSec}초`;
};

// 경과 시간 구하는 함수
const getElapsedTime = (targetTime: string, type?: string) => {
  // 지난 시간
  const bidCloseTime = new Date(targetTime);
  // 현재 시간
  const currTime = new Date();

  // 경과 시간 (ms)
  const diff =
    type === '남은시간'
      ? Number(bidCloseTime) - Number(currTime)
      : Number(currTime) - Number(bidCloseTime);

  if (diff < 0) {
    return type === '남은시간' ? '마감' : '0초 전';
  } else if (diff < 1000 * 60) {
    const diffSec = Math.floor((diff / 1000) % 60);
    return type === '남은시간' ? `${diffSec}초 후 마감` : `${diffSec}초 전`;
  } else if (diff < 1000 * 60 * 60) {
    const diffMin = Math.floor((diff / (1000 * 60)) % 60);
    return type === '남은시간' ? `${diffMin}분 후 마감` : `${diffMin}분 전`;
  } else if (diff < 1000 * 60 * 60 * 24) {
    const diffHour = Math.floor((diff / (1000 * 60 * 60)) % 24);
    return type === '남은시간'
      ? `${diffHour}시간 후 마감`
      : `${diffHour}시간 전`;
  } else {
    const diffDay = Math.floor(diff / (1000 * 60 * 60 * 24));
    return type === '남은시간' ? `${diffDay}일 후 마감` : `${diffDay}일 전`;
  }
};

export { getRemainingTime, getElapsedTime };
