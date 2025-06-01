import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import IndexPage from './pages/index';
import LoginPage from './pages/LoginPage';
import DashboardPage from './pages/Dashboard';
import ProfilePage from './pages/ProfilePage';
import GlobalStyle from './GlobalStyle';
import Market from './pages/market';

import MyTeam from './pages/MyTeam'
function App() {
  const [user, setUser] = useState(null);


  //Fiks af fejl hvor brugerens data ikke bliver loaded, når der bliver navigeret mellem sider
  useEffect(() => {
    const token = localStorage.getItem('token');
    const username = localStorage.getItem('username');
  
    if (token && username) {
      setUser({ username, token });
    }
  }, []);
  



  const handleLogin = (userData) => {
    
    console.log(userData)
    setUser(userData);
  };

  const handleLogout = () => {
    setUser(null);
    console.log("Bruger bliver logget ud..")
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
            path="/market"
            element={user ? <Market /> : <Navigate to="/login" />}
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
