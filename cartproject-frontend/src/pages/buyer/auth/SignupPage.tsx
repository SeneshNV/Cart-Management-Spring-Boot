import axios from "axios";
import "./Auth.css";
import React, { FormEvent, useState } from "react";
import { useNavigate } from "react-router-dom";

interface FormData {
  email: string;
  username: string;
  password: string;
  userRoleCode: string;
}

function SignupPage() {
  const [formData, setFormData] = useState<FormData>({
    email: "",
    username: "",
    password: "",
    userRoleCode: "BUYER",
  });
  const [confirmPassword, setConfirmPassword] = useState<string>("");

  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleConfirmPasswordChange = (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    setConfirmPassword(e.target.value);
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    const { username, email, password } = formData;

    if (!username || !email || !password || !confirmPassword) {
      alert("All fields are required.");
      return;
    }

    if (password.length < 8) {
      alert("Password must be at least 8 characters long.");
      return;
    }

    if (password !== confirmPassword) {
      alert("Passwords do not match.");
      return;
    }

    try {
      const response = await axios.post(
        "http://localhost:8080/api/v1/user/signup",
        formData,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      console.log(formData);

      if (response.data.status === "success") {
        console.log("Signup successful. Redirecting to dashboard...");
        navigate("/dashboard");
      } else {
        alert(response.data.message);
      }
    } catch (error) {
      console.error("Signup failed:", error);
      alert("Signup failed. Please try again.");
    }
  };

  return (
    <div className="container my-4">
      <div className="row justify-content-center align-items-center">
        <div className="col-md-6 text-center mb-4 mb-md-0 d-none d-md-block">
          <img
            src="https://images.unsplash.com/photo-1556906781-9a412961c28c?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            alt="Shoe Image"
            className="img-fluid rounded shadow-lg full-height"
          />
        </div>

        <div className="col-md-6">
          <div className="card p-4 shadow-lg border-0 rounded">
            <h2 className="text-center mb-4 text-bs-dark-text-emphasis">
              Signup
            </h2>
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label htmlFor="username" className="form-label">
                  User Name
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="username"
                  name="username"
                  value={formData.username}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="mb-3">
                <label htmlFor="email" className="form-label">
                  Email address
                </label>
                <input
                  type="email"
                  className="form-control"
                  id="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="mb-3">
                <label htmlFor="password" className="form-label">
                  Password
                </label>
                <input
                  type="password"
                  className="form-control"
                  id="password"
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="mb-3">
                <label htmlFor="confirmPassword" className="form-label">
                  Confirm Password
                </label>
                <input
                  type="password"
                  className="form-control"
                  id="confirmPassword"
                  value={confirmPassword}
                  onChange={handleConfirmPasswordChange}
                  required
                />
              </div>
              <button
                type="submit"
                className="btn text-white bg-black w-100 mt-3 p-2"
              >
                Submit
              </button>
            </form>
            <div className="text-center mt-3">
              <p>
                Already have an account? <a href="/login">Login</a>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default SignupPage;
