import React, { useEffect, useState } from 'react';

const TodaysMatchesTable = () => {
    const [matches, setMatches] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchMatches = async () => {
            try {
                const res = await fetch('/api/v4/matches', {
                    headers: {
                        'X-Auth-Token': '262343d711ec42bc89f246010e3a1bcc'
                    }
                });

                if (!res.ok) throw new Error(`API error: ${res.status}`);
                const data = await res.json();

                const today = new Date().toISOString().split('T')[0];
                const todaysMatches = data.matches.filter((match) =>
                    match.utcDate.startsWith(today)
                );

                setMatches(todaysMatches);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchMatches();
    }, []);


    if (loading) return <p>Loading matches...</p>;
    if (error) return <p>Error: {error}</p>;
    if (matches.length === 0) return <p>No matches today.</p>;

    return (
        <table style={{ borderCollapse: 'collapse', width: '100%' }}>
            <thead>
                <tr>
                    <th style={{ borderBottom: '1px solid #ccc', padding: '8px' }}>Time</th>
                    <th style={{ borderBottom: '1px solid #ccc', padding: '8px' }}>Home Team</th>
                    <th style={{ borderBottom: '1px solid #ccc', padding: '8px' }}>Away Team</th>
                    <th style={{ borderBottom: '1px solid #ccc', padding: '8px' }}>Status</th>
                    <th style={{ borderBottom: '1px solid #ccc', padding: '8px' }}>Score</th>
                </tr>
            </thead>
            <tbody>
                {matches.map((match) => {
                    const matchTime = new Date(match.utcDate).toLocaleTimeString([], {
                        hour: '2-digit',
                        minute: '2-digit',
                    });

                    const score =
                        match.score.fullTime.home !== null
                            ? `${match.score.fullTime.home} - ${match.score.fullTime.away}`
                            : '–';

                    return (
                        <tr key={match.id}>
                            <td style={{ padding: '8px' }}>{matchTime}</td>
                            <td style={{ padding: '8px' }}>{match.homeTeam.name}</td>
                            <td style={{ padding: '8px' }}>{match.awayTeam.name}</td>
                            <td style={{ padding: '8px' }}>{match.status}</td>
                            <td style={{ padding: '8px' }}>{score}</td>
                        </tr>
                    );
                })}
            </tbody>
        </table>
    );
};

export default TodaysMatchesTable;
