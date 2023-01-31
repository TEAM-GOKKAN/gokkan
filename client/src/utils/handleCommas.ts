// 콤마 삽입
const insertCommas = (price: number | string) => {
  return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

// 콤마 제거
const removeCommas = (price: string) => {
  return price.replace(/,/g, '');
};

export { insertCommas, removeCommas };
