import BaseInput from "../components/global/Input";
import { useEffect, useMemo, useState } from "react";
import { getUUID, queryStringToJson } from "../utils/utils";
import BaseSelect from "../components/global/Select";
import { useLocation, useNavigate } from "react-router-dom";
import { errorResponse } from "../utils/errorResponse";
import { reservationAPI } from "../api/reservation";
import toast from "react-hot-toast";

const GENDER = [
  {
    name: "남성",
    value: "MAN",
  },
  {
    name: "여성",
    value: "GIRL",
  },
];

const INIT_PASSENGER = {
  englishLastname: "",
  englishFirstname: "",
  gender: "",
  isAdult: true,
  nationality: "SOUTH_KOREA",
  uuid: "",
};

export default function ReservationPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const queryString = location.search;
  const [query, setQuery] = useState({});
  const [inputs, setInputs] = useState({
    passengers: [],
    payment: {
      cardNumber: "",
      cardCompany: "",
    },
  });

  useEffect(() => {
    const jsonQuery = queryStringToJson(queryString);
    setQuery(jsonQuery);
    setInputs((prev) => {
      return {
        ...prev,
        passengers: [
          ...Array(+jsonQuery.adult).fill({
            ...INIT_PASSENGER,
            uuid: getUUID(),
          }),
          ...Array(+jsonQuery.child).fill({
            ...INIT_PASSENGER,
            isAdult: false,
            uuid: getUUID(),
          }),
        ],
      };
    });
  }, [queryString]);

  const handlePassengerChange = (e) => {
    const { name, value } = e.target;
    const { id } = e.currentTarget.dataset;
    setInputs({
      ...inputs,
      passengers: inputs.passengers.map((item) =>
        item.uuid === id ? { ...item, [name]: value } : item,
      ),
    });
  };

  const handlePaymentChange = (e) => {
    const { name, value } = e.target;
    setInputs({
      ...inputs,
      payment: {
        ...inputs.payment,
        [name]: value,
      },
    });
  };

  const amountInfo = useMemo(() => {
    const price = Number(query?.price);
    const adult = Number(query?.adult);
    const child = Number(query?.child);
    const defaultAmount = price * adult;
    return {
      total: child
        ? defaultAmount + (price - price * 0.2) * child
        : defaultAmount,
      childAmount: child ? (price - price * 0.2) * child : 0,
      adultAmount: defaultAmount,
    };
  }, [query]);

  const handleReservation = async () => {
    try {
      const data = {
        ...inputs,
        startRoute: query.startCode,
        endRoute: query.endCode,
        payment: {
          ...inputs.payment,
          amount: amountInfo.total,
        },
      };

      await reservationAPI(data);
      navigate("/reservationList");
    } catch (e) {
      errorResponse(e);
    }
  };

  return (
    <div className={"bg-[#f5f7f8]"}>
      <div className={"container mx-auto max-w-[1200px] pt-20"}>
        <div className={"flex gap-10"}>
          <div className={"flex flex-col gap-5 flex-1"}>
            {inputs.passengers.map((item, index) => (
              <div key={index}>
                <div className={"flex gap-2 items-center pb-2"}>
                  <div className={"font-bold text-[18px]"}>여행자 정보</div>
                  <div
                    className={
                      "text-[15px] bg-blue-400 rounded-full px-2 py-1 text-white font-semibold"
                    }
                  >
                    {item.isAdult ? "성인" : "어린이"}
                  </div>
                </div>
                <PassengerInfo
                  item={item}
                  handlePassengerChange={handlePassengerChange}
                />
              </div>
            ))}

            <div>
              <div className={"font-bold text-[18px] pb-2"}>카드 정보</div>
              <div className={"bg-white rounded-lg shadow-lg p-5 w-full"}>
                <div className={"flex gap-5 w-full"}>
                  <div className={"w-1/2"}>
                    <BaseSelect
                      label={"카드사"}
                      options={[
                        { value: "", name: "카드사 선택" },
                        { value: "NH_CARD", name: "농협" },
                        { value: "KB_CARD", name: "KB" },
                      ]}
                      name={"cardCompany"}
                      onChange={handlePaymentChange}
                    />
                  </div>
                  <div className={"w-1/2"}>
                    <BaseInput
                      label={"카드번호"}
                      name={"cardNumber"}
                      onChange={handlePaymentChange}
                    />
                  </div>
                </div>
                <div className={"flex gap-5 w-full"}>
                  <div className={"w-1/2"}>
                    <BaseInput label={"카드비밀번호"} />
                  </div>
                  <div className={"w-1/2"}>
                    <BaseInput label={"cvv"} />
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div>
            <div
              className={
                "h-[300px] bg-white rounded-lg w-[300px] shadow-lg p-5"
              }
            >
              <div className={"flex flex-col h-full"}>
                <div className={"flex flex-col gap-5 flex-1"}>
                  <div className={"font-bold text-[18px]"}>결제 정보</div>
                  <div className={"flex flex-col gap-2"}>
                    <div>
                      <div className={"font-semibold"}>
                        성인 {query.adult}명
                      </div>
                      <div className={"text-[15px] text-gray-500"}>
                        {amountInfo.adultAmount.toLocaleString()}원
                      </div>
                    </div>
                    <div>
                      <div className={"font-semibold"}>
                        어린이 {query.child}명
                      </div>
                      <div className={"text-[15px] text-gray-500"}>
                        {Number(
                          amountInfo.childAmount.toFixed(0),
                        ).toLocaleString()}
                        원
                      </div>
                    </div>
                  </div>
                </div>

                <div
                  className={"h-[30px] flex items-center text-[18px] font-bold"}
                >
                  총 {Number(amountInfo.total.toFixed(0)).toLocaleString()}원
                </div>
              </div>
            </div>
            <div className={"pt-3"}>
              <button
                className={
                  "h-[50px] rounded-lg bg-blue-500 text-white text-[20px] w-full font-bold"
                }
                onClick={handleReservation}
              >
                결제하기
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

const PassengerInfo = ({ item, handlePassengerChange }) => {
  return (
    <div className={"bg-white rounded-lg shadow-lg p-5 w-full"}>
      <div className={"flex gap-5 w-full"}>
        <div className={"w-1/2"}>
          <BaseInput
            label={"성(영문)"}
            data-id={item.uuid}
            name={"englishFirstname"}
            onChange={handlePassengerChange}
          />
        </div>
        <div className={"w-1/2"}>
          <BaseInput
            label={"이름(영문)"}
            data-id={item.uuid}
            name={"englishLastname"}
            onChange={handlePassengerChange}
          />
        </div>
      </div>
      <div className={"flex gap-5 w-full"}>
        <div className={"w-1/2"}>
          <BaseSelect
            label={"성별"}
            options={[{ value: "", name: "성별 선택" }, ...GENDER]}
            data-id={item.uuid}
            name={"gender"}
            onChange={handlePassengerChange}
          />
        </div>
        <div className={"w-1/2"}>
          <BaseInput
            label={"생년월일"}
            data-id={item.uuid}
            name={"dateOfBirth"}
            onChange={handlePassengerChange}
          />
        </div>
      </div>
    </div>
  );
};
