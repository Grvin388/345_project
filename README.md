# TicketReserve 🎟️

TicketReserve is a modern, high-performance ticket reservation platform designed for university campus events. Built with a micro-feature architecture, it ensures a seamless user experience from event discovery to secure checkout.

## 🚀 Core Features

- **Dynamic Event Discovery**: Real-time browsing of campus events with category filtering.
- **Secure Authentication**: Integrated Firebase Google Auth with persistent sessions.
- **Role-Based Access**: Specialized views for standard users and administrators.
- **Responsive Design**: Fully optimized for mobile and desktop using Tailwind CSS.
- **Action-Based Guards**: Browse freely, but authenticate securely to reserve tickets.

---

## 🛠️ Technology Stack

| Layer | Technology |
| :--- | :--- |
| **Frontend** | Next.js 16 (App Router), TypeScript, Tailwind CSS |
| **UI Components** | Shadcn UI, Lucide Icons |
| **State/Auth** | React Context API, Firebase Authentication |
| **Backend** | Java Spring Boot, Spring Security |
| **Build Tool** | Maven / NPM |

---

## 📂 Project Structure

```text
├── frontend/             # Next.js Application
│   ├── app/              # App Router (Pages & Layouts)
│   ├── components/       # Global UI (Navbar, UI library)
│   ├── features/         # Feature-based logic (Auth, Events)
│   └── lib/              # Firebase & API configurations
└── backend/              # Spring Boot Application
    ├── src/main/java     # Controllers, Services, Models
    └── src/main/resources# application.properties & SQL