import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LandingPage from "./pages/LandingPage";
import BoardsPage from "./pages/BoardsPage"; 

const App = () => (
  <Router>
    <Routes>
      <Route path="/" element={<LandingPage />} />
      <Route path="/boards" element={<BoardsPage />} />
    </Routes>
  </Router>
);

export default App;
