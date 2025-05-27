import axios from "axios";
const API_URL = process.env.REACT_APP_API_URL || "http://localhost:8080";

export async function deleteTask(taskId) {
  const res = await axios.delete(`${API_URL}/api/tasks/${taskId}`, {
    headers: {
      Authorization: "Bearer " + localStorage.getItem("token"),
    },
  });
  return res;
}
