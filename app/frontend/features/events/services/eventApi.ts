export interface EventData {
  id: string;
  title: string;
  date: string;
  location: string;
  category: string;
}

export async function fetchEvents(): Promise<EventData[]> {
  // Simulating a fetch from your Spring Boot Java backend [cite: 7]
  return [
    { id: "1", title: "Montreal Jazz Fest", date: "2026-07-01", location: "Montreal", category: "Music" },
    { id: "2", title: "Tech Summit", date: "2026-08-15", location: "Toronto", category: "Conference" },
    { id: "3", title: "Grand Prix", date: "2026-06-12", location: "Montreal", category: "Sports" }
  ];
}