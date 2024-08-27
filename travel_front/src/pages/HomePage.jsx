import SearchBox from "../components/searchbox/SearchBox";
import { useSelector } from "react-redux";
import { jsonToQueryString } from "../utils/utils";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import dayjs from "dayjs";
import PriceChart from "../components/searchbox/PriceChart";

export default function HomePage() {
  const { flightSearch } = useSelector((state) => state);
  const navigate = useNavigate();
  const handleSearch = () => {
    const query = jsonToQueryString({
      ...flightSearch,
      date: flightSearch.date.replaceAll("-", ""),
    });
    navigate(`/flight/list/${query}`);
  };

  return (
    <div>
      <div className={"bg-black relative"}>
        <div className={"flex justify-center h-[400px]"}>
          <div className={"text-[40px] text-white font-bold pt-[100px]"}>
            TRAVEL.COM
          </div>
        </div>

        <div className={"flex justify-center"}>
          <div
            className={
              "absolute -bottom-[100px] container mx-auto max-w-[1200px]"
            }
          >
            <SearchBox handleSearch={handleSearch} />
          </div>
        </div>
      </div>
    </div>
  );
}
