import api from "./api";

export const loginAPI = (data) => {
  return api.post(`/api/v1/user/login`, data);
};

export const refreshTokenAPI = (data) => {
  return api.post(`/api/v1/user/refresh`, data);
};
