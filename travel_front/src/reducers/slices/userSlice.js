import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  isLogin: false,
  isLoading: true,
};

export const userSlice = createSlice({
  name: "USER",
  initialState,
  reducers: {
    login: (state, action) => {
      // state.userInfo = action.payload;
      state.isLogin = true;
      state.isLoading = false;
    },
    logout: (state) => {
      // state.userInfo = {};
      state.isLogin = false;
      state.isLoading = false;
    },
  },
});

// Action creators are generated for each case reducer function
export const { login, logout } = userSlice.actions;
