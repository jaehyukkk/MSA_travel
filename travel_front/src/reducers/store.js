import { configureStore } from "@reduxjs/toolkit";
import { logger } from "redux-logger/src";
import { flightSearchSlice } from "./slices/flightSearchSlice";
import { userSlice } from "./slices/userSlice";

const reducer = {
  flightSearch: flightSearchSlice.reducer,
  user: userSlice.reducer,
};

export const store = configureStore({
  reducer,
  middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(logger),
  devTools: process.env.NODE_ENV !== "production",
});
