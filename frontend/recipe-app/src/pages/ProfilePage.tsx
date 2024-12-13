import { useState } from "react";
import "./ProfilePage.css"

export default function ProfilePage() {
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem("token"));
  const [username, setUsername] = useState(localStorage.getItem("username") || "");
  const [role, setRole] = useState(localStorage.getItem("role") || "");
  const [formType, setFormType] = useState("login"); 
  const [formData, setFormData] = useState({ login: "", password: "" });

  const handleInputChange = (e: { target: { name: any; value: any; }; }) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleAuth = async () => {
    const endpoint = formType === "login" ? "/user/login" : "/user/register";
    try {
      const response = await fetch(`http://77.221.155.11:3000${endpoint}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });
      const data = await response.json();

      if (response.ok) {
        localStorage.setItem("token", data.token);
        localStorage.setItem("username", data.user.login);
        localStorage.setItem("role", data.user.role);
        setUsername(data.user.login);
        setRole(data.user.role);
        setIsLoggedIn(true);
        alert("Login success!");
      } else {
        alert(data.message || "Login error");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Server error");
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    localStorage.removeItem("role");
    setUsername("");
    setRole("");
    setIsLoggedIn(false);
  };

  return (
    <div className="profile-page">
      {isLoggedIn ? (
        <div>
          <h1 className="login">Welcome, {username}!</h1>
          <p className="login">Your role: {role}</p>
          <button className="submit" onClick={handleLogout}>Logout</button>
        </div>
      ) : (
        <div>
          <h1 className="login">{formType === "login" ? "Login" : "Register"}</h1>
          <input
            type="text"
            name="login"
            className="log"
            placeholder="Login"
            value={formData.login}
            onChange={handleInputChange}
          />
          <input
            type="password"
            name="password"
            className="log"
            placeholder="Password"
            value={formData.password}
            onChange={handleInputChange}
          />
          <button className="submit" onClick={handleAuth}>
            {formType === "login" ? "Login" : "Register"}
          </button>
          <button className="submit" onClick={() => setFormType(formType === "login" ? "register" : "login")}>
            Change to {formType === "login" ? "register" : "login"}
          </button>
        </div>
      )}
    </div>
  );
}
