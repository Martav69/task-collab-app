// src/App.js
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LandingPage from "./pages/LandingPage";
import BoardsPage from "./pages/BoardsPage";
import { AuthProvider } from "./context/AuthContext"; // <-- à créer

const App = () => (
  <AuthProvider>
    <Router>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/boards" element={<BoardsPage />} />
      </Routes>
    </Router>
  </AuthProvider>
);

export default App;
