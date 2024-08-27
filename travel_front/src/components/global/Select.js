import ExpandMoreIcon from "@mui/icons-material/ExpandMore";

export default function BaseSelect({ label, options, ...rest }) {
  return (
    <>
      <label>{label}</label>
      <div className={"relative"}>
        <select
          {...rest}
          className={
            "border-[1px] border-solid border-gray-300 rounded-lg h-[50px] w-full px-5"
          }
        >
          {options.map((option) => (
            <option key={option.value} value={option.value}>
              {option.name}
            </option>
          ))}
        </select>
        <div className={"absolute top-1/2 -translate-y-1/2 right-5"}>
          <ExpandMoreIcon className={"!text-[20px]"} />
        </div>
      </div>
    </>
  );
}
