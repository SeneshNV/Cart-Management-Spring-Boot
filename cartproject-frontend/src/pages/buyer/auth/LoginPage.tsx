import React, { useState, FormEvent } from "react";
import "./Auth.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";

interface FormData {
  email: string;
  password: string;
}

function LoginPage() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState<FormData>({
    email: "",
    password: "",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    const { email, password } = formData;

    if (!email || !password) {
      alert("All field are required");
      return;
    }

    try {
      const response = await axios.post(
        "http://localhost:8080/api/v1/user/login",
        formData,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      console.log(formData);

      if (response.data.status === "success") {
        console.log("Login successful. Redirecting to dashboard...");
        navigate("/dashboard");
      } else {
        alert(response.data.message);
      }
    } catch (error) {
      console.error("Login failed:", error);
      alert("Login failed. Please try again.");
    }
  };

  return (
    <>
      <div className="container my-4">
        <div className="row justify-content-center align-items-center">
          <div className="col-md-6 text-center mb-4 mb-md-0 d-none d-md-block">
            <img
              src="https://images.unsplash.com/photo-1556906781-9a412961c28c?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
              alt="Shoe Image"
              className="img-fluid rounded shadow-lg full-height"
            />
          </div>

          {/* Form - login */}
          <div className="col-md-6">
            <div className="card p-4 shadow-lg border-0 rounded">
              <h2 className="text-center mb-4 text-bs-dark-text-emphasis">
                Login
              </h2>
              <form onSubmit={handleSubmit}>
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

                <button
                  type="submit"
                  className="btn text-white bg-black w-100 mt-3 p-2"
                >
                  Submit
                </button>
              </form>
              <div className="text-center mt-3">
                <p>
                  Don't have an account? <a href="/signup">Sign up</a>
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default LoginPage;
