# TicketReserve 🎟️

TicketReserve is a modern ticket reservation platform for university campus events. It provides a smooth experience from browsing events to secure checkout.

---

## 🚀 Features

- Real-time event discovery with category filtering  
- Google authentication via Firebase  
- Role-based access (users & admins)  
- Responsive UI (mobile + desktop)  
- Secure ticket reservation flow  

---

## 🛠️ Tech Stack

- Frontend: Next.js, TypeScript, Tailwind CSS  
- Backend: Spring Boot, Spring Security  
- Auth: Firebase Authentication  
- UI: Shadcn UI, Lucide Icons  
- Build Tools: npm, Maven  

---

## 📂 Project Structure

frontend/   # Next.js app  
backend/    # Spring Boot app  

---

## ⚙️ Requirements

- Node.js + npm  
- Java (JDK 17+)  
- Maven (optional if using wrapper)  
- firebase file to put inside backend ressource folder

---

## ▶️ Running the App

Run frontend and backend at the same time in separate terminals.

---

### Frontend

cd frontend  
npm install  
npm run dev  

Runs on: http://localhost:5173  

---

### Backend

cd backend  

Mac / Linux:  
./mvnw spring-boot:run  

Windows:  
mvnw.cmd spring-boot:run  

If Maven wrapper is not included:  
mvn spring-boot:run  

Runs on: http://localhost:8080  

---

## ⚠️ Notes

- Both services must be running  
- Ensure ports 5173 and 8080 are available  
- Install dependencies before starting  

---

## 👨‍💻 Author

Alex Luangxay, Yassine Abdellatif, Gavin Chock-Chiong