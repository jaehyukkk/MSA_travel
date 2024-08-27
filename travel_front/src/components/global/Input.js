export default function BaseInput({ label, ...rest }) {
  return (
    <>
      <label>{label}</label>
      <div>
        <input
          {...rest}
          className={
            "border-[1px] border-solid border-gray-300 rounded-lg h-[50px] w-full px-5"
          }
          placeholder={`${label} 입력`}
        />
      </div>
    </>
  );
}
