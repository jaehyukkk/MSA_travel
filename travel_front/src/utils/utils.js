import dayjs from "dayjs";

export const yyyyMMdd = (date) => {
  if (!date) return null;
  return dayjs(date).format("YYYY-MM-DD");
};

export const jsonToQueryString = (json) => {
  return (
    "?" +
    Object.keys(json)
      .map((key) => key + "=" + json[key])
      .join("&")
  );
};

export const queryStringToJson = (queryString) => {
  return queryString
    .replace("?", "")
    .split("&")
    .reduce((acc, cur) => {
      const [key, value] = cur.split("=");
      acc[key] = value;
      return acc;
    }, {});
};

export const getUUID = () => {
  return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (c) {
    const r = (Math.random() * 16) | 0,
      v = c === "x" ? r : (r & 0x3) | 0x8;
    return v.toString(16);
  });
};

//yyyy-MM-dd HH:mm:ss
export const yyyyMMddHHmmss = (date) => {
  if (!date) return null;
  return dayjs(date).format("YYYY-MM-DD HH:mm:ss");
};

export const calculateTimeDifference = (start, end) => {
  const startParts = start.split(":");
  const startHour = parseInt(startParts[0], 10);
  const startMinute = parseInt(startParts[1], 10);

  const endParts = end.split(":");
  const endHour = parseInt(endParts[0], 10);
  const endMinute = parseInt(endParts[1], 10);

  const startTimeInMinutes = startHour * 60 + startMinute;
  let endTimeInMinutes = endHour * 60 + endMinute;

  if (endTimeInMinutes < startTimeInMinutes) {
    endTimeInMinutes += 24 * 60;
  }

  const differenceInMinutes = endTimeInMinutes - startTimeInMinutes;

  const hours = Math.floor(differenceInMinutes / 60);
  const minutes = differenceInMinutes % 60;

  return `${hours}시간 ${minutes}분`;
};
