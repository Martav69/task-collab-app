import axios from "axios";

const API_URL = process.env.REACT_APP_API_URL || "http://localhost:8080";

// Connexion utilisateur
export async function login(email, password) {
  const response = await axios.post(`${API_URL}/api/auth/login`, {
    email,
    password,
  });
  return response.data.token; 
}

// Inscription utilisateur
export async function register({ username, email, password }) {
  const response = await axios.post(`${API_URL}/api/auth/register`, {
    username,
    email,
    password,
  });
  return response.data; 
}

// plus tard) Déconnexion
export function logout() {
  localStorage.removeItem("token");
}

// Vérifier si connecté
export function isAuthenticated() {
  return !!localStorage.getItem("token");
}

// Récupérer le token actuel
export function getToken() {
  return localStorage.getItem("token");
}
