import './App.css';
import HomePage from './HomePage';
import Login from './Login';
import RequestsPage from './Requests'
import RegistrationPage from './Registration';
import LandingPage from './Landing';

import { BrowserRouter, Routes, Route } from "react-router-dom";
import SearchUsers from './Search';

// https://reactrouter.com/en/main/start/examples
function App() {
    return (
        <BrowserRouter>
            <div>
                <Routes>
                    <Route path="/home" element={<HomePage />} />
                    <Route path="/" element={<LandingPage />} />
                    <Route path="/requests" element={<RequestsPage />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/registration" element={<RegistrationPage />} />
                    <Route path="/search" element={<SearchUsers/>} />
                </Routes>
            </div>
        </BrowserRouter>
    );
}

export default App;
