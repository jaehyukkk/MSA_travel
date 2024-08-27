import BaseInput from "../components/global/Input";
import { useState } from "react";
import { loginAPI } from "../api/user";
import api from "../api/api";
import { Cookies } from "react-cookie";
import { errorResponse } from "../utils/errorResponse";

export default function LoginPage() {
  const cookie = new Cookies();
  const [inputs, setInputs] = useState({
    username: "",
    password: "",
  });

  const handleInput = (e) => {
    const { name, value } = e.target;
    setInputs({
      ...inputs,
      [name]: value,
    });
  };

  const handleLogin = async () => {
    try {
      const { accessToken, refreshToken } = await loginAPI(inputs);
      cookie.set("accessToken", accessToken, { path: "/" });
      cookie.set("refreshToken", refreshToken, { path: "/" });
      api.defaults.headers.common["Authorization"] = "Bearer " + accessToken;
      window.location.href = "/";
    } catch (e) {
      errorResponse(e);
    }
  };
  return (
    <div className={"container mx-auto max-w-[400px]"}>
      <div className={"pt-20"}>
        <div className={"flex justify-center font-bold text-[30px] pb-10"}>
          <h1>로그인</h1>
        </div>
        <div>
          <div className={"flex flex-col gap-3 pb-5"}>
            <div>
              <BaseInput
                label={"아이디"}
                name={"username"}
                onChange={handleInput}
              />
            </div>
            <div>
              <BaseInput
                label={"비밀번호"}
                type={"password"}
                name={"password"}
                onChange={handleInput}
              />
            </div>
          </div>
          <div>
            <button
              className={
                "w-full h-[50px] font-bold bg-blue-400 text-white text-[20px] rounded-lg"
              }
              onClick={handleLogin}
            >
              로그인
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
