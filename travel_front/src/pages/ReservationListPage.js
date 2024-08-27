import BaseSelect from "../components/global/Select";
import BaseInput from "../components/global/Input";
import { useEffect, useRef, useState } from "react";
import { errorResponse } from "../utils/errorResponse";
import { userReservationListAPI } from "../api/reservation";
import { RESERVATION_STATUS } from "../enums/enums";
import { yyyyMMddHHmmss } from "../utils/utils";
import Pagination from "../components/global/Pagination";
import { useNavigate } from "react-router-dom";

const SEARCH_OPTIONS = [
  {
    name: "검색옵션",
    value: "",
  },
  {
    name: "예약번호",
    value: "RESERVATION_NUMBER",
  },
  {
    name: "출발지",
    value: "START_ROUTE",
  },
  {
    name: "도착지",
    value: "END_ROUTE",
  },
];
export default function ReservationListPage() {
  const [reservations, setReservations] = useState({});
  const [isLoading, setIsLoading] = useState(true);
  const mounted = useRef(false);
  const navigate = useNavigate();
  const [search, setSearch] = useState({
    searchOption: "",
    searchKeyword: "",
  });
  const [pageData, setPageData] = useState({
    page: 1,
    size: 5,
  });
  const handleSearchChange = (e) => {
    const { name, value } = e.target;
    setSearch({
      ...search,
      [name]: value,
    });
  };

  useEffect(() => {
    if (!mounted.current) {
      mounted.current = true;
    } else {
      getData(pageData);
    }
  }, [pageData]);

  const getData = async (pageData = { page: 1, size: 5 }) => {
    try {
      const params = {
        ...pageData,
        ...search,
      };
      const res = await userReservationListAPI(params);
      setReservations(res);
    } catch (e) {
      errorResponse(e);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    getData();
  }, []);

  if (isLoading) return "loading...";

  return (
    <div className={"container mx-auto max-w-[1200px] pt-20"}>
      <div>
        <div className={"flex justify-between items-center"}>
          <div className={"text-[25px] font-bold pb-10"}>
            <div>나의 예약</div>
          </div>
        </div>

        <div className={"flex gap-3 pb-10"}>
          <div className={"w-[200px]"}>
            <BaseSelect
              label={"검색옵션"}
              options={SEARCH_OPTIONS}
              onChange={handleSearchChange}
              name={"searchOption"}
            />
          </div>
          <div className={"flex-1"}>
            <BaseInput
              label={"검색어"}
              name={"searchKeyword"}
              onChange={handleSearchChange}
              onKeyPress={(e) => {
                if (e.key === "Enter") {
                  getData();
                }
              }}
            />
          </div>
        </div>
      </div>
      <table className={"w-full"}>
        <thead>
          <tr
            className={
              "[&>th]:bg-gray-100 border-y-[1px] border-solid border-gray-300 [&>th]:py-2"
            }
          >
            <th>예약번호</th>
            <th>예약일</th>
            <th>출발지</th>
            <th>도착지</th>
            <th>탑승인원</th>
            <th>금액</th>
            <th>예약상태</th>
          </tr>
        </thead>
        <tbody>
          {reservations?.content?.map((item, key) => (
            <tr className={"[&>td]:py-2 [&>td]:text-center"} key={key}>
              <td
                className={"text-blue-500 underline"}
                onClick={() => {
                  navigate(`/reservation/${item.id}`);
                }}
              >
                {item.reservationNumber}
              </td>
              <td>{yyyyMMddHHmmss(item.createdDate)}</td>
              <td>{item.startRoute}</td>
              <td>{item.endRoute}</td>
              <td>{item.passengerCount}</td>
              <td>{item.amount.toLocaleString()}</td>
              <td>{RESERVATION_STATUS[item.reservationStatus]}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className={"flex w-full justify-center pt-3"}>
        <Pagination
          pageNumber={reservations.pageable.pageNumber + 1}
          totalPages={reservations.totalPages}
          onChange={(value) => {
            console.log(value);
            setPageData((prev) => {
              return {
                ...prev,
                page: value,
              };
            });
          }}
        />
      </div>
    </div>
  );
}
