import React from "react";
import { Route, Routes } from "react-router-dom";
import HomePage from "../pages/homePage/HomePage";
import LoginPage from "../pages/buyer/auth/LoginPage";
import Dashboard from "../pages/buyer/buyerHome/Dashboard";
import ProtectedRoute from "./ProtectedRoute";
import SignupPage from "../pages/buyer/auth/SignupPage";

function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/signup" element={<SignupPage />} />

      {/* Protected Route */}
      {/* <Route element={<ProtectedRoute />}> */}
      <Route path="/dashboard" element={<Dashboard />} />
      {/* </Route> */}
    </Routes>
  );
}

export default AppRoutes;
