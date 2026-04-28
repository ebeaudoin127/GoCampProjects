# API ReserveCamping

## Authentification
- `POST /auth/login` : connexion utilisateur
- `POST /auth/register` : création de compte

## Campings
- `GET /campgrounds` : liste des campings
- `GET /campgrounds/{id}` : détails d’un camping
- `POST /campgrounds` : ajout (ADMIN)

## Réservations
- `GET /reservations` : liste des réservations
- `POST /reservations` : créer une réservation
