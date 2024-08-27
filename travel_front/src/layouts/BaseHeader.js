import { Link } from "react-router-dom";
import { useSelector } from "react-redux";

export default function BaseHeader() {
  const { isLogin } = useSelector((state) => state.user);

  return (
    <header className={"w-full h-[70px] flex items-center bg-blue-400"}>
      <div className={"container mx-auto max-w-[1200px]"}>
        <div className={"flex justify-between items-center"}>
          <div className={"text-[25px] font-bold text-white"}>
            <div>traveldotcom</div>
          </div>
          <div>
            <div className={"flex gap-5 text-white font-bold"}>
              {isLogin && (
                <div>
                  <Link to={"/reservationList"}>나의예약</Link>
                </div>
              )}
              {!isLogin && (
                <>
                  <div>
                    <Link to={"/login"}>로그인</Link>
                  </div>
                  <div>회원가입</div>
                </>
              )}
            </div>
          </div>
        </div>
      </div>
    </header>
  );
}
