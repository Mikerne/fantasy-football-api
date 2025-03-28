#  API Endpoints

Her er de vigtigste endpoints, opdelt efter funktionalitet:

##  Authentication (JWT)
- `POST /api/auth/register` – Opret en ny bruger
- `POST /api/auth/login` – Log ind og få en JWT-token
- `GET /api/auth/me` – Hent information om den autentificerede bruger

##  Brugere
- `GET /api/users` – Hent alle brugere
- `GET /api/users/{id}` – Hent en enkelt bruger
- `PUT /api/users/{id}` – Opdater brugerens oplysninger
- `DELETE /api/users/{id}` – Slet en bruger (kun ADMIN)

##  Spillere
- `GET /api/players` – Hent alle spillere
- `GET /api/players/{id}` – Hent en specifik spiller
- `POST /api/players` – Tilføj en ny spiller (kun ADMIN)
- `PUT /api/players/{id}` – Opdater en spiller (kun ADMIN)
- `DELETE /api/players/{id}` – Slet en spiller (kun ADMIN)

##  Fantasy Hold
- `GET /api/teams` – Hent alle fantasy-hold
- `GET /api/teams/{id}` – Hent detaljer om et specifikt hold
- `POST /api/teams` – Opret et nyt fantasy-hold
- `PUT /api/teams/{id}` – Opdater et fantasy-hold
- `DELETE /api/teams/{id}` – Slet et fantasy-hold (kun ADMIN eller ejeren)

##  Kampe og Stats
- `GET /api/matches` – Hent alle kampe
- `GET /api/matches/{id}` – Hent detaljer om en specifik kamp
- `POST /api/matches` – Tilføj en ny kamp (kun ADMIN)
- `PUT /api/matches/{id}` – Opdater en kamp (kun ADMIN)
- `DELETE /api/matches/{id}` – Slet en kamp (kun ADMIN)

## Point og Scoring
- `GET /api/points` – Hent alle brugeres point
- `GET /api/points/{userId}` – Hent point for en specifik bruger
- `POST /api/points` – Tilføj point til en bruger (kun ADMIN) / Hvis user ikke findes bliver der oprettet en ny row

## Ligaer
- `GET /api/leagues` – Hent alle ligaer
- `GET /api/leagues/{id}` – Hent en specifik liga
- `POST /api/leagues` – Opret en ny liga
- `PUT /api/leagues/{id}` – Opdater en liga
- `DELETE /api/leagues/{id}` – Slet en liga (kun ADMIN eller ejeren)