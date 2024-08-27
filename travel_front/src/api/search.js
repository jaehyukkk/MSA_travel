import api from "./api";

export const flightListAPI = (params) => {
  return api.get(`/api/v1/search/flight`, { params });
};

export const flightStatisticsAPI = (params) => {
  return api.get(`/api/v1/search/flight/statistics`, { params });
};

export const categoryListAPI = () => {
  return api.get(`/api/v1/search/category`);
};
