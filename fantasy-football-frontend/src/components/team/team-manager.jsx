"use client"

import React, { useState } from "react"
import "./team-manager.css"

const initialFormation = [
  { id: "gk", name: "Målmand", player: null, x: 50, y: 85 },
  { id: "lb", name: "Venstre back", player: null, x: 30, y: 65 },
  { id: "rb", name: "Højre back", player: null, x: 70, y: 65 },
  { id: "lm", name: "Venstre midtbane", player: null, x: 35, y: 45 },
  { id: "rm", name: "Højre midtbane", player: null, x: 65, y: 45 },
  { id: "lf", name: "Venstre angriber", player: null, x: 35, y: 25 },
  { id: "rf", name: "Højre angriber", player: null, x: 65, y: 25 },
  { id: "st", name: "Højre angriber", player: null, x: 65, y: 25 },
]

export default function TeamManager({ initialPlayers }) {
  // Modtager spillerens købte spillere som prop
  const [availablePlayers, setAvailablePlayers] = useState(initialPlayers || []);
  const [formation, setFormation] = useState(initialFormation)
  const [draggedPlayer, setDraggedPlayer] = useState(null)

  const handleDragStart = (player) => {
    setDraggedPlayer(player)
  }

  const handleDragOver = (e) => {
    e.preventDefault()
  }

  const handleDrop = (e, positionId) => {
    e.preventDefault()

    if (!draggedPlayer) return

    const position = formation.find((pos) => pos.id === positionId)
    if (!position) return

    // Hvis positionen allerede har en spiller, flyt den tilbage til listen
    if (position.player) {
      setAvailablePlayers((prev) => [...prev, position.player])
    }

    // Opdater formationen med den nye spiller
    setFormation((prev) =>
      prev.map((pos) => (pos.id === positionId ? { ...pos, player: draggedPlayer } : pos))
    )

    // Fjern spilleren fra listen af tilgængelige spillere
    setAvailablePlayers((prev) => prev.filter((p) => p.id !== draggedPlayer.id))

    setDraggedPlayer(null)
  }

  const removePlayerFromFormation = (positionId) => {
    const position = formation.find((pos) => pos.id === positionId)
    if (!position?.player) return

    // Tilføj spilleren tilbage til listen
    setAvailablePlayers((prev) => [...prev, position.player])

    // Fjern spilleren fra positionen
    setFormation((prev) =>
      prev.map((pos) => (pos.id === positionId ? { ...pos, player: null } : pos))
    )


    //Eksamen hvis vi brugte loop:
    /*
    setFormation((prev) => {
      const newFormation = [];
    
      for (let i = 0; i < prev.length; i++) {
        const pos = prev[i];
        if (pos.id === positionId) {
          // Lav en kopi af pos med player sat til null
          newFormation.push({ ...pos, player: null });
        } else {
          // Behold det originale element
          newFormation.push(pos);
        }
      }
    
      return newFormation;
    });
    */
    
  }

  const playersOnField = formation.filter((pos) => pos.player).length

  return (
    <div className="team-manager">
      <header className="header">
        <div className="header-content">
          <h1>Mit 7-Mands Hold</h1>
          <p>Træk spillere fra listen og byg dit perfekte hold</p>
          <div className="team-stats">
            <span className="stat">
              <strong>{playersOnField}/7</strong> spillere på banen
            </span>
          </div>
        </div>
      </header>

      <main className="main-content">
        <section className="field-section">
          <div className="field-container">
            <div className="football-field">
              <div className="field-lines">
                <div className="center-line"></div>
                <div className="center-circle"></div>
                <div className="penalty-area penalty-area--top"></div>
                <div className="penalty-area penalty-area--bottom"></div>
              </div>

              {formation.map((position) => (
                <div
                  key={position.id}
                  className="position-slot"
                  style={{ left: `${position.x}%`, top: `${position.y}%` }}
                  onDragOver={handleDragOver}
                  onDrop={(e) => handleDrop(e, position.id)}
                >
                  {position.player ? (
                    <div className="player-on-field">
                      <button
                        className="remove-btn"
                        onClick={() => removePlayerFromFormation(position.id)}
                        aria-label={`Fjern ${position.player.name}`}
                      >
                        ×
                      </button>
                      <div className="player-avatar">
                        <img src={position.player.imageUrl || "/placeholder.svg"} alt={position.player.name} />
                      </div>
                      <div className="player-details">
                        <span className="player-name">{position.player.name}</span>
                        <span className="player-rating">{position.player.rating}</span>
                      </div>
                    </div>
                  ) : (
                    <div className="empty-position">
                      <div className="position-icon">+</div>
                      <span className="position-name">{position.name}</span>
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
        </section>

        <aside className="players-sidebar">
          <div className="sidebar-header">
            <h2>Spillertruppen</h2>
            <span className="available-count">{availablePlayers.length} tilgængelige</span>
          </div>

          <div className="players-list">
            {availablePlayers.map((player) => (
              <div key={player.id} className="player-card" draggable onDragStart={() => handleDragStart(player)}>
                <div className="player-card-avatar">
                  <img src={player.imageUrl || "/placeholder.svg"} alt={player.name} />
                </div>
                <div className="player-card-info">
                  <h3 className="player-card-name">{player.name}</h3>
                  <p className="player-card-position">{player.position}</p>
                  <div className="player-card-rating">
                    <span>{player.rating}</span>
                  </div>
                </div>
                <div className="drag-indicator">⋮⋮</div>
              </div>
            ))}

            {availablePlayers.length === 0 && (
              <div className="empty-state">
                <p>Alle spillere er på banen! 🎉</p>
              </div>
            )}
          </div>
        </aside>
      </main>
    </div>
  )
}
