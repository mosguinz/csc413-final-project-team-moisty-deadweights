import './App.css';
import HomePage from './HomePage';
import Login from './Login';
import RequestsPage from './Requests'

import { BrowserRouter, Routes, Route } from "react-router-dom";

// https://reactrouter.com/en/main/start/examples
function App() {
  return (
    <BrowserRouter>
      <div>
        <Routes>
          <Route path="/home" element={<HomePage />} />
          <Route path="/" element={<Login />} />
          <Route path="/requests" element={<RequestsPage />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;
