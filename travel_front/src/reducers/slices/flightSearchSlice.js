import { yyyyMMdd } from "../../utils/utils";
import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  date: yyyyMMdd(new Date()),
  startCode: "GMP",
  endCode: "KIX",
  adult: 1,
  child: 0,
};

export const flightSearchSlice = createSlice({
  name: "flightSearch",
  initialState,
  reducers: {
    setFlightSearch: (state, action) => {
      state[action.payload.name] = action.payload.value;
    },
  },
});

export const { setFlightSearch } = flightSearchSlice.actions;
