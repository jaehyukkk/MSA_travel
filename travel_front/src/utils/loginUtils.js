import { Cookies } from "react-cookie";
import api from "../api/api";
import { refreshTokenAPI } from "../api/user";

export const refreshToken = (loginCallback, logout) => {
  const cookie = new Cookies();
  refreshTokenAPI({
    refreshToken: cookie.get("refreshToken"),
    accessToken: cookie.get("accessToken"),
  })
    .then((res) => {
      cookie.set("accessToken", res.accessToken, { path: "/" });
      // cookie.set('refreshToken', res.refreshToken, { path: '/' });

      api.defaults.headers.common["Authorization"] =
        "Bearer " + res.accessToken;

      loginCallback();
      setTimeout(
        function () {
          refreshToken(loginCallback, logout);
        },
        1000 * 60 * 60 * 2,
      );
    })
    .catch((ex) => {
      cookie.remove("accessToken", { path: "/" });
      cookie.remove("refreshToken", { path: "/" });
      logout();
    })
    .finally(() => {});
};
