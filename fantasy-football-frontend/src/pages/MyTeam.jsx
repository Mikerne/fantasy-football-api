"use client"

import React, { useState, useEffect } from "react";
import TeamManager from "../components/team/team-manager";

export default function MyTeam() {
  const [players, setPlayers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const username = localStorage.getItem("username");
    if (!username) {
      setLoading(false);
      return;
    }

    fetch("http://localhost:3001/userTeams")
      .then((res) => res.json())
      .then((data) => {
        const userTeam = data.find((team) => team.username === username);
        console.log("data fundet", userTeam)
        setPlayers(userTeam ? userTeam.players : []);
      })
      .catch((error) => {
        console.error("Error fetching user teams:", error);
        setPlayers([]);
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div>Loading...</div>;

  return <TeamManager initialPlayers={players} />;
}
