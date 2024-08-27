import toast from "react-hot-toast";

export const errorResponse = (e) => {
  toast.error(errorMessage(e) || "알 수 없는 에러가 발생했습니다.");
};

const errorMessage = (e) => {
  if (e?.response?.data?.code) {
    return `${e?.response?.data?.code} ${e?.response?.data?.message}`;
  }
  return null;
};
