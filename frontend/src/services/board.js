import axios from "axios";
const API_URL = process.env.REACT_APP_API_URL || "http://localhost:8080";

export async function deleteBoard(boardId) {
  return axios.delete(`${API_URL}/api/boards/${boardId}`, {
    headers: {
      Authorization: "Bearer " + localStorage.getItem("token"),
    },
  });
}
