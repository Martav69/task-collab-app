import axios from "axios";
const API_URL = process.env.REACT_APP_API_URL || "http://localhost:8080";

export async function uploadTaskImage(taskId, file) {
  const formData = new FormData();
  formData.append("file", file);
  formData.append("taskId", taskId);

  const res = await axios.post(`${API_URL}/api/task-images/upload`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
      Authorization: "Bearer " + localStorage.getItem("token"),
    },
  });
  return res.data; 
}
