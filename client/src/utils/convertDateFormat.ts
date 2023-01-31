const convertDateFormat = (time: string) => {
  const year = Number(time.slice(0, 4));
  const month = Number(time.slice(5, 7));
  const day = Number(time.slice(8, 10));
  const hour = Number(time.slice(8, 10));
  const min = Number(time.slice(14, 16));
  const sec = Number(time.slice(17, 19));

  return { year, month, day, hour, min, sec };
};

export { convertDateFormat };
