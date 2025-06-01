"use client"

import { useState, useMemo, useEffect } from "react"
import "./transfer-market.css"


const positions = ["Alle", "GK", "CB", "LB", "RB", "CM", "CAM", "LW", "RW", "ST"]
const sortOptions = [
    { value: "rating-desc", label: "Rating (Højest)" },
    { value: "rating-asc", label: "Rating (Lavest)" },
    { value: "name-asc", label: "Navn (A-Z)" },
    { value: "name-desc", label: "Navn (Z-A)" },
    { value: "price-desc", label: "Pris (Højest)" },
    { value: "price-asc", label: "Pris (Lavest)" },
]

// Laver en pris ud fra rating af spilleren
const calculatePrice = (rating) => {
    return Math.round((rating - 70) * 2.5 + Math.random() * 10) * 1000000
}

export default function TransferMarket() {
    const [searchTerm, setSearchTerm] = useState("")
    const [selectedPosition, setSelectedPosition] = useState("Alle")
    const [sortBy, setSortBy] = useState("rating-desc")
    const [cart, setCart] = useState([])
    const [showCart, setShowCart] = useState(false)
    const [playersWithPrices, setPlayersWithPrices] = useState([])

    //Henter vores datasæt fra json serveren, og sætter en pris på spilleren ud fra rating.
    useEffect(() => {
        async function fetchPlayers() {
            try {
                const res = await fetch("http://localhost:3001/players")
                if (!res.ok) throw new Error("Failed to fetch players")
                const data = await res.json()
                const playersWithPrice = data.map((player) => ({
                    ...player,
                    price: calculatePrice(player.rating),
                }))
                setPlayersWithPrices(playersWithPrice)
            } catch (error) {
                console.error(error)
            }
        }

        fetchPlayers()
    }, [])

    // Filter af spillere.
    const filteredPlayers = useMemo(() => {
        const filtered = playersWithPrices.filter((player) => {
            const matchesSearch =
                player.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                player.club.toLowerCase().includes(searchTerm.toLowerCase())
            const matchesPosition = selectedPosition === "Alle" || player.position === selectedPosition
            const notInCart = !cart.some((cartPlayer) => cartPlayer.id === player.id)

            return matchesSearch && matchesPosition && notInCart
        })

        // Sort players
        filtered.sort((a, b) => {
            switch (sortBy) {
                case "rating-desc":
                    return b.rating - a.rating
                case "rating-asc":
                    return a.rating - b.rating
                case "name-asc":
                    return a.name.localeCompare(b.name)
                case "name-desc":
                    return b.name.localeCompare(a.name)
                case "price-desc":
                    return b.price - a.price
                case "price-asc":
                    return a.price - b.price
                default:
                    return 0
            }
        })

        return filtered
    }, [playersWithPrices, searchTerm, selectedPosition, sortBy, cart])

    const addToCart = (player) => {
        setCart((prev) => [...prev, player])
    }

    const removeFromCart = (playerId) => {
        setCart((prev) => prev.filter((player) => player.id !== playerId))
    }

    const getTotalPrice = () => {
        return cart.reduce((total, player) => total + player.price, 0)
    }

    //Simple funktion der returner om det er millioner eller tusinder
    const formatPrice = (price) => {
        if (price >= 1000000) {
            return `€${(price / 1000000).toFixed(1)}M`
        }
        return `€${(price / 1000).toFixed(0)}K`
    }


    const checkout = async () => {
        const username = localStorage.getItem("username")
        if (!username) {
            alert("Du skal være logget ind for at gennemføre køb.")
            return
        }

        try {
            // Hent eksisterende userTeam for brugeren
            const res = await fetch(`http://localhost:3001/userTeams?username=${username}`)
            const userTeams = await res.json()

            if (userTeams.length > 0) {
                // Brugeren har allerede et team - opdater det
                const userTeam = userTeams[0]
                const existingPlayers = userTeam.players || []

                // Undgå duplikater
                const newPlayers = cart.filter(
                    (cartPlayer) => !existingPlayers.some((p) => p.id === cartPlayer.id)
                )
                const updatedPlayers = [...existingPlayers, ...newPlayers]

                const patchRes = await fetch(`http://localhost:3001/userTeams/${userTeam.id}`, {
                    method: "PATCH",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({ players: updatedPlayers }),
                })

                if (!patchRes.ok) throw new Error("Kunne ikke opdatere holdet.")

            } else {
                // Brugeren har ikke et team endnu - opret nyt team
                const postRes = await fetch(`http://localhost:3001/userTeams`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        username,
                        players: cart,
                    }),
                })

                if (!postRes.ok) throw new Error("Kunne ikke oprette holdet.")
            }

            alert("Køb gennemført!")
            setCart([])
            setShowCart(false)
        } catch (error) {
            console.error(error)
            alert("Der skete en fejl under gennemførelsen.")
        }
    }



    return (
        <div className="transfer-market">
            <header className="header">
                <div className="header-content">
                    <h1>Transfer Marked</h1>
                    <p>Find og køb de bedste spillere til dit hold</p>
                    <div className="market-stats">
                        <span className="stat">
                            <strong>{filteredPlayers.length}</strong> spillere tilgængelige
                        </span>
                        <button
                            className={`cart-toggle ${cart.length > 0 ? "has-items" : ""}`}
                            onClick={() => setShowCart(!showCart)}
                        >
                            🛒 Kurv ({cart.length})
                        </button>
                    </div>
                </div>
            </header>

            <main className="main-content">
                <aside className="filters-sidebar">
                    <div className="filters-section">
                        <h3>Søg & Filtrer</h3>

                        <div className="search-box">
                            <input
                                type="text"
                                placeholder="Søg efter spiller eller klub..."
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                                className="search-input"
                            />
                        </div>

                        <div className="filter-group">
                            <label>Position</label>
                            <select
                                value={selectedPosition}
                                onChange={(e) => setSelectedPosition(e.target.value)}
                                className="filter-select"
                            >
                                {positions.map((position) => (
                                    <option key={position} value={position}>
                                        {position}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div className="filter-group">
                            <label>Sorter efter</label>
                            <select value={sortBy} onChange={(e) => setSortBy(e.target.value)} className="filter-select">
                                {sortOptions.map((option) => (
                                    <option key={option.value} value={option.value}>
                                        {option.label}
                                    </option>
                                ))}
                            </select>
                        </div>
                    </div>

                    {showCart && (
                        <div className="cart-section">
                            <h3>Din Kurv</h3>
                            {cart.length === 0 ? (
                                <p className="empty-cart">Kurven er tom</p>
                            ) : (
                                <>
                                    <div className="cart-items">
                                        {cart.map((player) => (
                                            <div key={player.id} className="cart-item">
                                                <div className="cart-player-info">
                                                    <span className="cart-player-name">{player.name}</span>
                                                    <span className="cart-player-price">{formatPrice(player.price)}</span>
                                                </div>
                                                <button onClick={() => removeFromCart(player.id)} className="remove-from-cart">
                                                    ×
                                                </button>
                                            </div>
                                        ))}
                                    </div>
                                    <div className="cart-total">
                                        <strong>Total: {formatPrice(getTotalPrice())}</strong>
                                    </div>
                                    <button className="checkout-btn" onClick={checkout}>Gennemfør Køb</button>
                                </>
                            )}
                        </div>
                    )}
                </aside>

                <section className="players-grid-section">
                    <div className="players-grid">
                        {filteredPlayers.map((player) => (
                            <div key={player.id} className="player-market-card">
                                <div className="player-card-header">
                                    <div className="player-avatar">
                                        <img src={player.imageUrl} alt={player.name} />
                                    </div>
                                    <div className="player-rating-badge">{player.rating}</div>
                                </div>

                                <div className="player-card-body">
                                    <h3 className="player-name">{player.name}</h3>
                                    <p className="player-club">{player.club}</p>
                                    <div className="player-details">
                                        <span className="player-position">{player.position}</span>
                                        <span className="player-price">{formatPrice(player.price)}</span>
                                    </div>
                                </div>

                                <div className="player-card-footer">
                                    <button onClick={() => addToCart(player)} className="buy-button">
                                        Køb Spiller
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>

                    {filteredPlayers.length === 0 && (
                        <div className="no-results">
                            <h3>Ingen spillere fundet</h3>
                            <p>Prøv at justere dine søgekriterier</p>
                        </div>
                    )}
                </section>
            </main>
        </div>
    )
}
