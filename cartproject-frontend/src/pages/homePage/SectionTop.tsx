import React from "react";
import { Link } from "react-router-dom";

function SectionTop() {
  return (
    <div className="position-absolute top-50 start-50 translate-middle text-center ">
      <div className="content">
        <h1 className="display-1 fw-bold text-white mb-1">SHOES FORT</h1>
        <p className="fs-3 text-white mb-4">Shoes Fort Online Store</p>
        <div className="d-flex gap-3 justify-content-center">
          <Link to="/login" className="btn btn-primary btn-lg px-4">
            Login
          </Link>

          <Link to="/signup" className="btn btn-secondary btn-lg px-4">
            Create New Account
          </Link>
        </div>
      </div>
    </div>
  );
}

export default SectionTop;
