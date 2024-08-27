import { useEffect, useRef, useState } from "react";

export default function Dropdown({
  open,
  setOpen,
  button,
  top = 30,
  children,
}) {
  const [offsetTop, setOffsetTop] = useState(0);
  const [offsetLeft, setOffsetLeft] = useState(0);
  const dropdownRef = useRef(null);

  useEffect(() => {
    if (!dropdownRef?.current) return;

    const handleScroll = () => {
      setOffsetTop(dropdownRef.current.getBoundingClientRect().top);
      setOffsetLeft(dropdownRef.current.getBoundingClientRect().left);
    };
    handleScroll();
    window.addEventListener("scroll", handleScroll);
    window.addEventListener("resize", handleScroll);
    return () => {
      window.removeEventListener("scroll", handleScroll);
      window.removeEventListener("resize", handleScroll);
    };
  }, [dropdownRef]);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setOpen(false);
      }
    };

    if (open) {
      document.addEventListener("mousedown", handleClickOutside);
      // document.body.style.overflow = "hidden";
    } else {
      document.removeEventListener("mousedown", handleClickOutside);
      // document.body.style.overflow = "";
    }

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
      // document.body.style.overflow = "";
    };
  }, [open]);

  const handleOpen = () => {
    setOpen(!open);
  };

  return (
    <div ref={dropdownRef}>
      <div onClick={handleOpen} className={"cursor-pointer"}>
        {button}
      </div>
      {open && (
        <div
          className={
            "fixed shadow-lg rounded-lg border-[1px] border-solid border-gray-200"
          }
          style={{
            top: offsetTop + top,
            left: offsetLeft,
            zIndex: 10,
            backgroundColor: "white",
          }}
        >
          {children}
        </div>
      )}
    </div>
  );
}
