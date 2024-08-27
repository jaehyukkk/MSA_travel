import { useEffect, useMemo, useState } from "react";
import dayjs from "dayjs";
import "dayjs/locale/ko";
dayjs.locale("ko");
// const obj = {
//   startCode: "GMP",
//   endCode: "KIX",
//   statistics: [
//     { date: "20240811", price: 90000 },
//     { date: "20240812", price: 25000 },
//     { date: "20240813", price: 30000 },
//     { date: "20240814", price: 50000 },
//     { date: "20240820", price: 100400 },
//   ],
// };
export default function PriceChart({ obj }) {
  const [data, setData] = useState([]);
  const [hoverDate, setHoverDate] = useState(null);
  useEffect(() => {
    let today = dayjs(new Date()).format("YYYYMMDD");
    const aa = obj.statistics.filter((item) => +item.date >= +today);
    //다음달 마지막 날짜
    const nextMonth = dayjs(today)
      .add(1, "month")
      .endOf("month")
      .format("YYYYMMDD");

    const bb = [];

    while (+today <= +nextMonth) {
      const find = aa.find((item) => +item.date === +today);
      if (find) {
        bb.push(find);
      } else {
        bb.push({ date: today, price: 0 });
      }
      today = dayjs(today).add(1, "day").format("YYYYMMDD");
    }

    setData(bb);
  }, []);

  const max = useMemo(
    () => Math.max(...data.map((item) => item.price)),
    [data],
  );
  const min = useMemo(
    () => Math.min(...data.map((item) => item.price)),
    [data],
  );

  const qorqnsdbf = (price) => ((price - min) / (max - min)) * 100;

  return (
    <div>
      <div className={"flex h-[200px] gap-5"}>
        <div>
          <div className={"flex flex-col justify-between h-full"}>
            <div>{Number(max.toFixed(0)).toLocaleString()}</div>
            <div>
              {max - min > 0
                ? Number(((max - min) / 2 + min).toFixed(0)).toLocaleString()
                : "No Data"}
            </div>
            <div>{Number(min.toFixed(0)).toLocaleString()}</div>
          </div>
        </div>
        <div
          className={
            "relative flex gap-1 h-full border-y-[1px] border-solid border-gray-300"
          }
        >
          <div
            className={`absolute top-1/2 -translate-y-1/2 w-full h-[1px] bg-gray-400 z-10 left-0`}
          />
          {data.map((item) => (
            <div key={item.date} className={"h-full w-[15px]"}>
              {item.price === 0 ? (
                <ChartItem
                  h={10}
                  color={"#dadada"}
                  item={item}
                  setHoverDate={setHoverDate}
                />
              ) : (
                <ChartItem
                  h={qorqnsdbf(item.price)}
                  color={"red"}
                  item={item}
                  setHoverDate={setHoverDate}
                />
              )}
            </div>
          ))}
        </div>
      </div>
      {hoverDate && (
        <div className={"text-center mt-5 font-bold text-[20px]"}>
          <div>{dayjs(hoverDate).format("YYYY-MM-DD")}</div>
          <div>{dayjs(hoverDate).format("dddd")}</div>
        </div>
      )}
    </div>
  );
}

const ChartItem = ({ h, color, item, setHoverDate }) => {
  const [isHovering, setIsHovering] = useState(false);

  const handleMouseOver = () => {
    setHoverDate(item.date);
    setIsHovering(true);
  };

  const handleMouseOut = () => {
    setHoverDate(null);
    setIsHovering(false);
  };

  return (
    <div
      className={"relative h-full w-full"}
      onMouseOver={handleMouseOver}
      onMouseOut={handleMouseOut}
    >
      <div
        style={{
          height: `${h}%`,
          width: "100%",
          position: "absolute",
          background: color,
          bottom: 0,
          left: 0,
        }}
      />
      {isHovering && (
        <>
          <div
            className={"absolute top-0 left-1/2 bg-black z-10 w-[1px] h-full"}
          />
          <div
            className={
              "absolute -top-[40px] border-[1px] border-solid border-gray-300 shadow-lg rounded-lg w-[200px] bg-white px-2 py-1 flex items-center font-bold text-blue-500"
            }
          >
            {item.price
              ? `평균 ${Number(item.price.toFixed(0)).toLocaleString()}원`
              : "No Data"}
          </div>
        </>
      )}
    </div>
  );
};
