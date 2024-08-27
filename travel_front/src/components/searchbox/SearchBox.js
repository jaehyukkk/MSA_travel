import FlightTakeoffIcon from "@mui/icons-material/FlightTakeoff";
import LoopIcon from "@mui/icons-material/Loop";
import Dropdown from "../global/Dropdown";
import CalendarMonthIcon from "@mui/icons-material/CalendarMonth";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import Calendar from "react-calendar";
import { setFlightSearch } from "../../reducers/slices/flightSearchSlice";
import { useEffect, useMemo, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { yyyyMMdd } from "../../utils/utils";
import { errorResponse } from "../../utils/errorResponse";
import { categoryListAPI } from "../../api/search";

export default function SearchBox({ handleSearch }) {
  const [startDropdownOpen, setStartDropdownOpen] = useState(false);
  const [endDropdownOpen, setEndDropdownOpen] = useState(false);
  const [dateDropdownOpen, setDateDropdownOpen] = useState(false);
  const [passengerDropdownOpen, setPassengerDropdownOpen] = useState(false);
  const [selectionCategory, setSelectionCategory] = useState(null);
  const [categories, setCategories] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const dispatch = useDispatch();
  const { date, startCode, endCode, adult, child } = useSelector(
    (state) => state.flightSearch,
  );

  const handleDate = (value) => {
    dispatch(setFlightSearch({ name: "date", value: yyyyMMdd(value) }));
  };
  const handleCategoryClick = (e) => {
    const { id } = e.currentTarget.dataset;
    if (+id === selectionCategory) {
      setSelectionCategory(null);
    } else {
      setSelectionCategory(+id);
    }
  };

  const startSelection = useMemo(() => {
    return categories
      .flatMap((item) => item.airports)
      .find((airport) => airport.code === startCode);
  }, [categories, startCode]);

  const endSelection = useMemo(() => {
    return categories
      .flatMap((item) => item.airports)
      .find((airport) => airport.code === endCode);
  }, [categories, endCode]);

  const handleAirportClick = (e) => {
    const { name, code } = e.currentTarget.dataset;
    dispatch(setFlightSearch({ name, value: code }));
  };

  useEffect(() => {
    if (!startDropdownOpen || !endDropdownOpen) setSelectionCategory(null);
  }, [startDropdownOpen, endDropdownOpen]);

  useEffect(() => {
    setDateDropdownOpen(false);
  }, [date]);

  useEffect(() => {
    const getData = async () => {
      try {
        const res = await categoryListAPI();
        setCategories(res);
        console.log(res);
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
    <div
      className={
        "flex justify-between items-center shadow-lg rounded-lg p-10 bg-white"
      }
    >
      <div className={"flex items-center gap-20"}>
        <AirportDropdown
          open={startDropdownOpen}
          setOpen={setStartDropdownOpen}
          handleCategoryClick={handleCategoryClick}
          selectionCategory={selectionCategory}
          name={"startCode"}
          onChange={handleAirportClick}
          selection={startSelection}
          categories={categories}
        />
        <div className={"flex flex-col items-center justify-center gap-1"}>
          <div>
            <FlightTakeoffIcon className={"text-blue-400"} />
          </div>
          <div>
            <LoopIcon className={"!text-[30px]"} />
          </div>
        </div>
        <AirportDropdown
          open={endDropdownOpen}
          setOpen={setEndDropdownOpen}
          handleCategoryClick={handleCategoryClick}
          selectionCategory={selectionCategory}
          name={"endCode"}
          onChange={handleAirportClick}
          selection={endSelection}
          categories={categories}
        />
      </div>

      <div className={"flex gap-3"}>
        <Dropdown
          top={55}
          open={dateDropdownOpen}
          setOpen={setDateDropdownOpen}
          button={
            <div
              className={
                "border-[1px] border-solid border-gray-200 h-[50px] flex items-center px-3 rounded-lg"
              }
            >
              <div className={"flex items-center justify-between w-[200px]"}>
                <div className={"flex gap-1 items-center"}>
                  <CalendarMonthIcon />
                  <div>{date}</div>
                </div>
                <div>
                  <ExpandMoreIcon className={"!text-[20px]"} />
                </div>
              </div>
            </div>
          }
        >
          <Calendar onChange={handleDate} value={date} locale={"ko-KR"} />
        </Dropdown>

        <Dropdown
          open={passengerDropdownOpen}
          setOpen={setPassengerDropdownOpen}
          top={55}
          button={
            <div
              className={
                "border-[1px] border-solid border-gray-200 h-[50px] flex items-center px-3 rounded-lg"
              }
            >
              <div className={"flex items-center justify-between w-[200px]"}>
                <div className={"flex gap-1 items-center"}>
                  <div>성인 {adult}명</div>
                  <div>어린이 {child}명</div>
                </div>
                <div>
                  <ExpandMoreIcon className={"!text-[20px]"} />
                </div>
              </div>
            </div>
          }
        >
          <div className={"p-5 w-[300px]"}>
            <div className={"flex items-center"}>
              <div className={"font-semibold w-[20%]"}>성인</div>
              <div>
                <input
                  className={"border-solid border-[1px] border-gray-300 p-1"}
                  type={"number"}
                  value={adult}
                  onChange={(e) => {
                    if (+e.target.value >= 1)
                      // setSearch({ ...search, adult: +e.target.value });
                      dispatch(
                        setFlightSearch({
                          name: "adult",
                          value: +e.target.value,
                        }),
                      );
                  }}
                />
              </div>
            </div>
            <div className={"flex items-center"}>
              <div className={"font-semibold w-[20%]"}>어린이</div>
              <div>
                <input
                  className={"border-solid border-[1px] border-gray-300 p-1"}
                  type={"number"}
                  value={child}
                  onChange={(e) => {
                    if (+e.target.value >= 0) {
                      dispatch(
                        setFlightSearch({
                          name: "child",
                          value: +e.target.value,
                        }),
                      );
                    }
                  }}
                />
              </div>
            </div>
          </div>
        </Dropdown>
      </div>

      <div>
        <button
          className={
            "bg-blue-400 text-white font-bold w-[150px] h-[50px] rounded-lg"
          }
          onClick={handleSearch}
        >
          항공권 검색
        </button>
      </div>
    </div>
  );
}

const AirportDropdown = ({
  open,
  setOpen,
  handleCategoryClick,
  selectionCategory,
  name,
  onChange,
  selection,
  categories,
}) => {
  const handleClick = (e) => {
    setOpen(!open);
    onChange(e);
  };
  return (
    <div className={"flex flex-col items-center"}>
      <div className={"font-semibold text-[40px]"}>{selection.code}</div>
      <Dropdown
        open={open}
        setOpen={setOpen}
        button={
          <div className={"flex items-center justify-center w-[100px]"}>
            <div>{selection.location}</div>
            <div>
              <ExpandMoreIcon className={"!text-[20px]"} />
            </div>
          </div>
        }
      >
        <div className={"w-[250px]"}>
          {categories.map((item, key) => (
            <div>
              <div
                className={`cursor-pointer select-none py-2 px-3 font-semibold bg-gray-100 ${
                  key !== categories.length - 1 && "border-b border-gray-200"
                }`}
                data-id={item.id}
                onClick={handleCategoryClick}
              >
                <div className={"flex gap-1"}>
                  {item.name}
                  <div>
                    <ExpandMoreIcon className={"!text-[20px]"} />
                  </div>
                </div>
              </div>
              {item.id === selectionCategory && (
                <div>
                  {item.airports.map((airport, key) => (
                    <div
                      className={`flex items-center cursor-pointer select-none py-2 px-3 ${
                        key !== categories.length - 1 &&
                        "border-b border-gray-200"
                      }`}
                      key={key}
                      data-name={name}
                      data-code={airport?.code}
                      onClick={handleClick}
                    >
                      <div className={"flex gap-2"}>
                        <div>{airport.location}</div>
                        <div>{airport?.code}</div>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          ))}
        </div>
      </Dropdown>
    </div>
  );
};
