import axios from "axios";

const api = axios.create({
  // baseURL: process.env.NEXT_PUBLIC_BASE_URL,
  baseURL: `${process.env.REACT_APP_API_URL}`,
  // timeout: 5000,
});

api.interceptors.request.use(
  (config) => config,
  (error) => Promise.reject(error),
);
api.interceptors.response.use(
  function (response) {
    const { data } = response;
    return data;
  },
  async function (error) {
    // toast.error(error?.response?.data?.message || '데이터 처리 중 오류가 발생하였습니다.');
    return Promise.reject(error);
  },
);

export default api;
