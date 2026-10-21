# CabGo — Cab Booking Application

A full-stack local-demo starter based on the requested CabGo UI:
- React + Vite frontend
- Java 17 + Spring Boot REST API
- MySQL + Spring Data JPA
- Rider registration/login (demo session), cab selection, ride booking, ride history
- Driver dashboard and accept/complete ride actions
- Demo payment records (UPI/card/wallet/cash) and admin metrics

> This is a local development/demo project. Payment methods are simulated; no real money is charged.
> Live GPS tracking, Google Maps API, SMS/email notifications, production JWT/OAuth security, and deployment secrets must be configured separately before production use.

## Requirements
- Java 17+
- Maven 3.9+
- Node.js 20+
- MySQL 8+

## 1. Create database
```sql
CREATE DATABASE cab_booking_db;
```

## 2. Configure backend
Edit `backend/src/main/resources/application.properties` and set your MySQL username/password:
```properties
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

## 3. Run backend
```bash
cd backend
mvn spring-boot:run
```
API runs at `http://localhost:8081`. Tables are created automatically by JPA.

## 4. Run frontend
Open a second terminal:
```bash
cd frontend
npm install
npm run dev
```
Open the Vite URL shown in terminal (usually `http://localhost:5173`).

## Demo flow
1. Register or use the quick demo login (any name/email; demo mode).
2. Enter pickup and drop, select a cab, and book.
3. Open My Rides to view rides and demo payment.
4. Driver panel can accept or complete pending rides.
5. Admin panel displays totals from the API.

## API endpoints
- `GET /api/health`
- `GET /api/cabs`
- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/rides`
- `GET /api/rides?email=...`
- `GET /api/rides/all`
- `PATCH /api/rides/{id}/status`
- `POST /api/payments`
- `GET /api/payments?email=...`
- `GET /api/admin/summary`

## Notes
This starter is intentionally easy to run locally. Before public deployment, add validated credentials, JWT/session security and role-based access control, rate limiting, audit logs, real payment provider webhooks, proper map/geocoding integration, and automated tests.


## Interactive map
The React frontend includes a Leaflet/OpenStreetMap picker. Select Pickup or Drop and click the map. The displayed distance is a straight-line estimate, not a driving-route distance. OpenStreetMap attribution is included; follow the tile usage policy for deployments.

## Important demo limitations
- Payments are simulated; no real UPI/card transaction is processed.
- The map coordinates currently update the frontend and location labels. The existing ride API persists pickup/drop text and distance only; coordinates are not stored in the database.
- Configure your MySQL password in `backend/src/main/resources/application.properties` before starting the backend.
- This is a local learning/demo project, not production-ready security. Review backend authorization and role controls before public deployment.
