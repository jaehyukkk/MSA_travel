import { createBrowserRouter } from "react-router-dom";
import BaseLayout from "./layouts/BaseLayout";
import HomePage from "./pages/HomePage";
import FlightListPage from "./pages/FlightListPage";
import ReservationPage from "./pages/ReservationPage";
import LoginPage from "./pages/LoginPage";
import SignupPage from "./pages/SignupPage";
import ReservationListPage from "./pages/ReservationListPage";
import ReservationDetailPage from "./pages/ReservationDetailPage";

export const router = createBrowserRouter([
  {
    element: <BaseLayout />,
    children: [
      {
        path: "/",
        element: <HomePage />,
      },
      {
        path: "/flight/list",
        element: <FlightListPage />,
      },
      {
        path: "/reservation",
        element: <ReservationPage />,
      },
      {
        path: "/login",
        element: <LoginPage />,
      },
      {
        path: "/signup",
        element: <SignupPage />,
      },
      {
        path: "/reservationList",
        element: <ReservationListPage />,
      },
      {
        path: "/reservation/:id",
        element: <ReservationDetailPage />,
      },
    ],
  },
]);
