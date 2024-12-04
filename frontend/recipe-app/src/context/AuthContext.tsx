// src/context/AuthContext.tsx
import React, { createContext, useState, ReactNode } from "react";
import axios from "axios";

interface User {
  id: string;
  login: string;
  role: string;
}

interface AuthContextProps {
  isAuthenticated: boolean;
  token: string | null;
  user: User | null;
  login: (email: string, password: string) => Promise<void>;
  register: (login: string, password: string) => Promise<boolean>;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextProps | null>(null);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [token, setToken] = useState<string | null>(localStorage.getItem("token"));
  const [user, setUser] = useState<User | null>(
    token ? JSON.parse(localStorage.getItem("user") || "null") : null
  );

  const login = async (login: string, password: string) => {
    try {
      const response = await axios.post("http://77.221.155.11:3000/user/login", {
        login,
        password,
      });

      const { token, user } = response.data;
      setToken(token);
      setUser(user);
      localStorage.setItem("token", token);
      localStorage.setItem("user", JSON.stringify(user));
    } catch (error) {
      console.error("Login failed:", error);
      throw error;
    }
  };

  const register = async (login: string, password: string): Promise<boolean> => {
    try {
      const response = await axios.post("http://77.221.155.11:3000/user/register", {
        login,
        password,
      });
      return response.data.success;
    } catch (error) {
      console.error("Registration failed:", error);
      return false;
    }
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    localStorage.removeItem("token");
    localStorage.removeItem("user");
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated: !!token, token, user, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
