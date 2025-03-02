import { useState } from "react";
import { NavLink } from "react-router-dom";

function NavBar() {
  const [isNavOpen, setIsNavOpen] = useState(false);

  return (
    <nav className="navbar navbar-expand-lg custom-navbar shadow-sm ">
      <div className="container d-flex justify-content-between align-items-center px-3">
        <NavLink className="navbar-brand fw-bold text-white" to="/">
          SHOES FORT
        </NavLink>

        <button
          className="navbar-toggler"
          type="button"
          onClick={() => setIsNavOpen(!isNavOpen)}
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        <div
          className={`collapse navbar-collapse ${isNavOpen ? "show" : ""}`}
          id="navbarNav"
        >
          <ul className="navbar-nav ms-auto d-flex gap-4">
            <li className="nav-item">
              <NavLink
                to="/"
                end
                className={({ isActive }) =>
                  `nav-link ${
                    isActive ? "active fw-bold text-primary" : "text-white"
                  }`
                }
              >
                Home
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink
                to="/login"
                end
                className={({ isActive }) =>
                  `nav-link ${
                    isActive ? "active fw-bold text-primary" : "text-white"
                  }`
                }
              >
                Login
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink
                to="/signup"
                end
                className={({ isActive }) =>
                  `nav-link ${
                    isActive ? "active fw-bold text-primary" : "text-white"
                  }`
                }
              >
                Signup
              </NavLink>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
}

export default NavBar;
