import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import {
  calculateTimeDifference,
  jsonToQueryString,
  queryStringToJson,
} from "../utils/utils";
import SearchBox from "../components/searchbox/SearchBox";
import FlightTakeoffIcon from "@mui/icons-material/FlightTakeoff";
import { errorResponse } from "../utils/errorResponse";
import { flightListAPI, flightStatisticsAPI } from "../api/search";
import PriceChart from "../components/searchbox/PriceChart";

export default function FlightListPage() {
  const location = useLocation();
  const queryString = location.search;
  const navigate = useNavigate();
  const [flightList, setFlightList] = useState([]);
  const [flightStatistics, setFlightStatistics] = useState({});
  const [isLoading, setIsLoading] = useState(true);
  const moveReservationPage = (data) => {
    navigate(
      `/reservation${jsonToQueryString({ ...data, ...queryStringToJson(queryString) })}`,
    );
  };

  useEffect(() => {
    const getData = async () => {
      try {
        const params = queryStringToJson(queryString);
        const [flightList, flightStatistics] = await Promise.all([
          flightListAPI(params),
          flightStatisticsAPI({
            startCode: params.startCode,
            endCode: params.endCode,
          }),
        ]);
        setFlightList(flightList);

        setFlightStatistics(flightStatistics);
      } catch (e) {
        errorResponse(e);
      } finally {
        setIsLoading(false);
      }
    };

    getData();
  }, []);

  if (isLoading) return "loading...";
  return (
    <div className={"bg-[#f5f7f8]"}>
      <div className={"container mx-auto max-w-[1200px] pt-10"}>
        <div className={"flex flex-col gap-5"}>
          <div>
            <SearchBox />
          </div>

          {flightStatistics?.statistics && (
            <div
              className={
                "text-black flex justify-center py-10 shadow-lg w-full bg-white"
              }
            >
              <PriceChart obj={flightStatistics} />
            </div>
          )}

          <div>
            {flightList.map((item, i) => (
              <React.Fragment key={i}>
                <ListItem
                  moveReservationPage={moveReservationPage}
                  flight={item}
                />
              </React.Fragment>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
// {"airline_name":"제주항공","price":80500,"start_route":"08:20","end_route":"10:00"},
const ListItem = ({ moveReservationPage, flight }) => {
  return (
    <div
      className={
        "flex justify-between items-center bg-white rounded-lg h-[130px] p-10 shadow-lg mb-2"
      }
    >
      <div className={"w-1/3 font-bold text-[18px]"}>{flight.airline_name}</div>
      <div className={"flex gap-20 w-1/3 justify-center"}>
        <div className={"flex gap-1 font-bold"}>
          <div>{flight.start_route}</div>
          <FlightTakeoffIcon className={"text-blue-400"} />
          <div>{flight.end_route}</div>
        </div>

        <div>
          직항, {calculateTimeDifference(flight.start_route, flight.end_route)}
        </div>
      </div>

      <div
        className={"w-1/3 text-right text-red-500 font-bold text-[18px]"}
        onClick={() => moveReservationPage({ price: flight.price })}
      >
        편도 {flight.price.toLocaleString()}원~
      </div>
    </div>
  );
};
