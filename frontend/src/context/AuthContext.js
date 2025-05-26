import React, { createContext, useContext, useEffect, useState } from "react";
import { getToken, isAuthenticated, logout } from "../services/auth";

export const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loadingUser, setLoadingUser] = useState(true);

  useEffect(() => {
    async function fetchMe() {
      if (isAuthenticated()) {
        try {
          const res = await fetch(`${process.env.REACT_APP_API_URL || "http://localhost:8080"}/api/users/me`, {
            headers: { Authorization: "Bearer " + getToken() }
          });
          if (res.ok) {
            setUser(await res.json());
          } else {
            setUser(null);
            logout();
          }
        } catch (e) {
          setUser(null);
          logout();
        }
      } else {
        setUser(null);
      }
      setLoadingUser(false);
    }
    fetchMe();
  }, []);

  return (
    <AuthContext.Provider value={{ user, setUser, loadingUser }}>
      {children}
    </AuthContext.Provider>
  );
}


export function useAuth() {
  return useContext(AuthContext);
}
