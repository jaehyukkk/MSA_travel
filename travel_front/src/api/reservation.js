import api from "./api";

export const reservationAPI = (data) => {
  return api.post(`/api/v1/reservation`, data);
};

export const userReservationListAPI = (params) => {
  return api.get(`/api/v1/reservation`, { params });
};

export const userReservationDetailAPI = (id) => {
  return api.get(`/api/v1/reservation/${id}`);
};
