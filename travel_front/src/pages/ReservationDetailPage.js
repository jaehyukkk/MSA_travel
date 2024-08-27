import { useParams } from "react-router-dom";
import { RESERVATION_STATUS } from "../enums/enums";
import { yyyyMMddHHmmss } from "../utils/utils";
import { useEffect, useState } from "react";
import { userReservationDetailAPI } from "../api/reservation";
import { errorResponse } from "../utils/errorResponse";

// const reservation = {
//   id: 1,
//   startRoute: "ICN",
//   endRoute: "LAX",
//   reservationNumber: "20220101000001",
//   reservationStatus: "RESERVED",
//   amount: 100000,
//   createdDate: "2022-01-01T00:00:00",
//   passengers: [
//     {
//       id: 1,
//       englishLastName: "Kim",
//       englishFirstName: "Dong",
//       nationality: "SOUTH_KOREA",
//       gender: "MAN",
//       isAdult: true,
//       dateOfBirth: "19900101",
//     },
//     {
//       id: 2,
//       englishLastName: "Kim",
//       englishFirstName: "Dong",
//       nationality: "SOUTH_KOREA",
//       gender: "MAN",
//       isAdult: true,
//       dateOfBirth: "19900101",
//     },
//   ],
// };
export default function ReservationDetailPage() {
  const { id } = useParams();
  const [isLoading, setIsLoading] = useState(true);
  const [reservation, setReservation] = useState({});
  useEffect(() => {
    const getData = async () => {
      try {
        const res = await userReservationDetailAPI(id);
        setReservation(res);
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
    <div className={"container mx-auto max-w-[600px] pt-20"}>
      <div className={"flex gap-10"}>
        <div className={"flex flex-col gap-5 flex-1"}>
          <div className={"font-bold text-[25px]"}>예약 상세</div>
          <div>
            <table
              className={"border-[1px] border-solid border-gray-300 w-full"}
            >
              <tbody>
                <tr>
                  <td
                    className={
                      "w-[100px] bg-gray-200 border-b-[1px] border-solid border-gray-300 py-1 px-2"
                    }
                  >
                    예약번호
                  </td>
                  <td className={"border-b-[1px] border-solid border-gray-300"}>
                    {reservation?.reservationNumber}
                  </td>
                </tr>
                <tr>
                  <td
                    className={
                      "w-[100px] bg-gray-200 border-b-[1px] border-solid border-gray-300 py-1 px-2"
                    }
                  >
                    출발지
                  </td>
                  <td className={"border-b-[1px] border-solid border-gray-300"}>
                    {reservation.startRoute}
                  </td>
                </tr>
                <tr>
                  <td
                    className={
                      "w-[100px] bg-gray-200 border-b-[1px] border-solid border-gray-300 py-1 px-2"
                    }
                  >
                    도착지
                  </td>
                  <td className={"border-b-[1px] border-solid border-gray-300"}>
                    {reservation.endRoute}
                  </td>
                </tr>

                <tr>
                  <td
                    className={
                      "w-[100px] bg-gray-200 border-b-[1px] border-solid border-gray-300 py-1 px-2"
                    }
                  >
                    예약상태
                  </td>
                  <td className={"border-b-[1px] border-solid border-gray-300"}>
                    {RESERVATION_STATUS[reservation.reservationStatus]}
                  </td>
                </tr>
                <tr>
                  <td
                    className={
                      "w-[100px] bg-gray-200 border-b-[1px] border-solid border-gray-300 py-1 px-2"
                    }
                  >
                    결제금
                  </td>
                  <td className={"border-b-[1px] border-solid border-gray-300"}>
                    {reservation.amount.toLocaleString()}
                  </td>
                </tr>
                <tr>
                  <td
                    className={
                      "w-[100px] bg-gray-200 border-b-[1px] border-solid border-gray-300 py-1 px-2"
                    }
                  >
                    예약일자
                  </td>
                  <td className={"border-b-[1px] border-solid border-gray-300"}>
                    {yyyyMMddHHmmss(reservation.createdDate)}
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <div>
            <div className={"font-bold text-[20px]"}>승객 정보</div>
            <div>
              {reservation.passengers.map((passenger) => (
                <div
                  key={passenger.id}
                  className={
                    "border-[1px] border-solid border-gray-300 rounded-lg mb-3 p-5"
                  }
                >
                  <div className={"font-bold"}>
                    {passenger.englishLastName} {passenger.englishFirstName}
                  </div>
                  <div>{passenger.isAdult ? "성인" : "미성년자"}</div>

                  <div>생년월일 {passenger.dateOfBirth}</div>
                  <div>성별 {passenger.gender === "MAN" ? "남자" : "여자"}</div>
                  <div>국적 {passenger.nationality}</div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
