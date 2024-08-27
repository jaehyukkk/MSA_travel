import { Outlet } from "react-router-dom";
import BaseHeader from "./BaseHeader";

export default function BaseLayout() {
  return (
    <div className={"min-h-[1000px]"}>
      <BaseHeader />
      <Outlet />
    </div>
  );
}
