import React, { useState } from "react";

export default function ProfilePage() {
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem("token"));
  const [username, setUsername] = useState(localStorage.getItem("username") || "");
  const [role, setRole] = useState(localStorage.getItem("role") || "");
  const [formType, setFormType] = useState("login"); // "login" or "register"
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
        alert("Авторизация успешна!");
      } else {
        alert(data.message || "Ошибка авторизации");
      }
    } catch (error) {
      console.error("Ошибка:", error);
      alert("Ошибка соединения с сервером");
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
          <h1>Добро пожаловать, {username}!</h1>
          <p>Ваша роль: {role}</p>
          <button onClick={handleLogout}>Выйти</button>
        </div>
      ) : (
        <div>
          <h1>{formType === "login" ? "Авторизация" : "Регистрация"}</h1>
          <input
            type="text"
            name="login"
            placeholder="Логин"
            value={formData.login}
            onChange={handleInputChange}
          />
          <input
            type="password"
            name="password"
            placeholder="Пароль"
            value={formData.password}
            onChange={handleInputChange}
          />
          <button onClick={handleAuth}>
            {formType === "login" ? "Войти" : "Зарегистрироваться"}
          </button>
          <button onClick={() => setFormType(formType === "login" ? "register" : "login")}>
            Переключиться на {formType === "login" ? "регистрацию" : "авторизацию"}
          </button>
        </div>
      )}
    </div>
  );
}
