import { useCallback, useMemo } from "react";
import { ReactComponent as ArrowLeft } from "../../assets/images/arrow_left.svg";
import { ReactComponent as ArrowRight } from "../../assets/images/arrow-right.svg";
import { ReactComponent as ArrowLeftDouble } from "../../assets/images/arrow-double-left.svg";
import { ReactComponent as ArrowRightDouble } from "../../assets/images/arrow-double-right.svg";

export default function Pagination({
  pageNumber,
  totalPages,
  onChange,
  size = 10,
  blockSize = 5,
}) {
  const pageItems = useMemo(() => {
    const total = Math.ceil(totalPages / size);
    const currentBlock = Math.ceil(pageNumber / blockSize);
    const startPage = (currentBlock - 1) * blockSize + 1;
    let endPage = currentBlock * blockSize;

    return { total, currentBlock, startPage, endPage };
  }, [totalPages, blockSize, pageNumber]);

  const pages = useMemo(() => {
    const pages = [];
    for (let i = pageItems.startPage; i <= pageItems.endPage; i++) {
      if (i > totalPages) {
        break;
      }
      pages.push(i);
    }
    return pages;
  }, [pageItems]);

  const handleNext = useCallback(() => {
    if (pageNumber < totalPages) {
      onChange(pageNumber + 1);
    }
  }, [pageNumber, totalPages]);

  const handlePrev = useCallback(() => {
    if (pageNumber > 1) {
      onChange(pageNumber - 1);
    }
  }, [pageNumber]);

  const handlePageChange = useCallback((e) => {
    onChange(Number(e.currentTarget.dataset.page));
  }, []);

  return (
    <div className={"w-full flex justify-center"}>
      <div className={"flex items-center select-none"}>
        <div
          data-page={1}
          onClick={handlePageChange}
          className={
            "w-[32px] h-[32px] flex items-center justify-center cursor-pointer"
          }
        >
          <ArrowLeftDouble className={"w-[13px] h-[13px] fill-gray-500"} />
        </div>
        <div onClick={handlePrev}>
          <div
            className={
              "w-[32px] h-[32px] flex items-center justify-center cursor-pointer"
            }
          >
            <ArrowLeft className={"w-[13px] h-[13px] fill-gray-500"} />
          </div>
        </div>
        <div className={"flex items-center"}>
          {pages?.map((page, key) => (
            <div
              key={key}
              data-page={page}
              onClick={handlePageChange}
              className={`
                w-[32px] h-[32px] flex items-center justify-center cursor-pointer
            ${pageNumber === page ? "border-solid border-[1px] border-blue-500 text-blue-500" : ""}`}
            >
              {page}
            </div>
          ))}
        </div>
        <div
          onClick={handleNext}
          className={
            "w-[32px] h-[32px] flex items-center justify-center cursor-pointer"
          }
        >
          <ArrowRight className={"w-[13px] h-[13px] fill-gray-500"} />
        </div>
        <div
          data-page={totalPages}
          onClick={handlePageChange}
          className={
            "w-[32px] h-[32px] flex items-center justify-center cursor-pointer"
          }
        >
          <ArrowRightDouble className={"w-[13px] h-[13px] fill-gray-500"} />
        </div>
      </div>
    </div>
  );
}
