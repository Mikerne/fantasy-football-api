import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import IndexPage from './pages/index';
import LoginPage from './pages/LoginPage';
import DashboardPage from './pages/Dashboard';
import ProfilePage from './pages/ProfilePage';
import GlobalStyle from './GlobalStyle';


import MyTeam from './pages/MyTeam'
function App() {
  const [user, setUser] = useState(null);

  const handleLogin = (userData) => {
    setUser(userData);
  };

  const handleLogout = () => {
    setUser(null);
    localStorage.removeItem('token');
    localStorage.removeItem('username');
  };

  return (
    <>
      <GlobalStyle />   {}
      <Router>
        <Routes>
          <Route path="/" element={<IndexPage user={user} />} />
          <Route
            path="/login"
            element={!user ? <LoginPage onLogin={handleLogin} /> : <Navigate to="/dashboard" />}
          />
          <Route
            path="/dashboard"
            element={user ? <DashboardPage username={user.username} onLogout={handleLogout} /> : <Navigate to="/login" />}
          />
          <Route
            path="/profile"
            element={user ? <ProfilePage user={user} onLogout={handleLogout} /> : <Navigate to="/login" />}
          />
          <Route
            path="/myteam"
            element={user ? <MyTeam /> : <Navigate to="/login" />}
          />
          <Route
            path="*"
            element={<Navigate to="/" />}
          />
        </Routes>
      </Router>
    </>
  );
}

export default App;
