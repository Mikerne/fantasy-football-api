"use client"

import React from "react"

import { useState } from "react"
import "./team-manager.css"

interface Player {
  id: number
  name: string
  position: string
  rating: number
  image: string
}

interface TeamPosition {
  id: string
  name: string
  player: Player | null
  x: number
  y: number
}

const mockPlayers: Player[] = [
  { id: 1, name: "Mads Hansen", position: "Målmand", rating: 85, image: "/placeholder.svg?height=60&width=60" },
  { id: 2, name: "Lars Nielsen", position: "Forsvarer", rating: 82, image: "/placeholder.svg?height=60&width=60" },
  { id: 3, name: "Peter Andersen", position: "Forsvarer", rating: 79, image: "/placeholder.svg?height=60&width=60" },
  { id: 4, name: "Jesper Larsen", position: "Midtbane", rating: 88, image: "/placeholder.svg?height=60&width=60" },
  { id: 5, name: "Thomas Sørensen", position: "Midtbane", rating: 84, image: "/placeholder.svg?height=60&width=60" },
  { id: 6, name: "Anders Pedersen", position: "Angriber", rating: 90, image: "/placeholder.svg?height=60&width=60" },
  { id: 7, name: "Mikkel Jensen", position: "Angriber", rating: 87, image: "/placeholder.svg?height=60&width=60" },
  { id: 8, name: "Jonas Møller", position: "Forsvarer", rating: 83, image: "/placeholder.svg?height=60&width=60" },
  { id: 9, name: "Frederik Olsen", position: "Midtbane", rating: 86, image: "/placeholder.svg?height=60&width=60" },
  { id: 10, name: "Christian Bach", position: "Angriber", rating: 89, image: "/placeholder.svg?height=60&width=60" },
]

// 7-mands formation: 1-2-2-2 (Målmand, 2 forsvarere, 2 midtbane, 2 angribere)
const initialFormation: TeamPosition[] = [
  { id: "gk", name: "Målmand", player: null, x: 50, y: 85 },
  { id: "lb", name: "Venstre back", player: null, x: 30, y: 65 },
  { id: "rb", name: "Højre back", player: null, x: 70, y: 65 },
  { id: "lm", name: "Venstre midtbane", player: null, x: 35, y: 45 },
  { id: "rm", name: "Højre midtbane", player: null, x: 65, y: 45 },
  { id: "lf", name: "Venstre angriber", player: null, x: 35, y: 25 },
  { id: "rf", name: "Højre angriber", player: null, x: 65, y: 25 },
]

export default function TeamManager() {
  const [availablePlayers, setAvailablePlayers] = useState<Player[]>(mockPlayers)
  const [formation, setFormation] = useState<TeamPosition[]>(initialFormation)
  const [draggedPlayer, setDraggedPlayer] = useState<Player | null>(null)

  const handleDragStart = (player: Player) => {
    setDraggedPlayer(player)
  }

  const handleDragOver = (e: React.DragEvent) => {
    e.preventDefault()
  }

  const handleDrop = (e: React.DragEvent, positionId: string) => {
    e.preventDefault()

    if (!draggedPlayer) return

    const position = formation.find((pos) => pos.id === positionId)
    if (!position) return

    // Hvis positionen allerede har en spiller, flyt den tilbage til listen
    if (position.player) {
      setAvailablePlayers((prev) => [...prev, position.player!])
    }

    // Opdater formationen med den nye spiller
    setFormation((prev) => prev.map((pos) => (pos.id === positionId ? { ...pos, player: draggedPlayer } : pos)))

    // Fjern spilleren fra listen af tilgængelige spillere
    setAvailablePlayers((prev) => prev.filter((player) => player.id !== draggedPlayer.id))

    setDraggedPlayer(null)
  }

  const removePlayerFromFormation = (positionId: string) => {
    const position = formation.find((pos) => pos.id === positionId)
    if (!position?.player) return

    // Tilføj spilleren tilbage til listen
    setAvailablePlayers((prev) => [...prev, position.player!])

    // Fjern spilleren fra positionen
    setFormation((prev) => prev.map((pos) => (pos.id === positionId ? { ...pos, player: null } : pos)))
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
                        <img src={position.player.image || "/placeholder.svg"} alt={position.player.name} />
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
                  <img src={player.image || "/placeholder.svg"} alt={player.name} />
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
