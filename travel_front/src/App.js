import "./App.css";
import { RouterProvider } from "react-router-dom";
import { router } from "./router";
import "react-calendar/dist/Calendar.css";
import { Toaster } from "react-hot-toast";
import { useCallback, useEffect } from "react";
import { Cookies } from "react-cookie";
import api from "./api/api";
import { useDispatch, useSelector } from "react-redux";
import { login, logout } from "./reducers/slices/userSlice";
import { refreshToken } from "./utils/loginUtils";
function App() {
  const dispatch = useDispatch();
  const cookie = new Cookies();
  const { isLoading } = useSelector((state) => state.user);
  const handleLogout = useCallback(() => {
    dispatch(logout());
    cookie.remove("accessToken", { path: "/" });
    cookie.remove("refreshToken", { path: "/" });
  }, []);

  const loginCallback = useCallback(async () => {
    try {
      dispatch(login());
    } catch (e) {
      // logout();
      handleLogout();
    }
  }, []);

  useEffect(() => {
    if (cookie.get("accessToken")) {
      refreshToken(loginCallback, handleLogout);
    } else {
      handleLogout();
    }
  }, []);

  if (isLoading) return "loading...";

  return (
    <>
      <RouterProvider router={router} />
      <Toaster position="top-center" />
    </>
  );
}

export default App;
