const API_BASE_URL = "http://localhost:8080/api";

export interface EventData {
  id: number;
  title: string;
  date: string;
  location: string;
  category: string;
  status: "ACTIVE" | "CANCELLED";
}

export async function fetchEvents(): Promise<EventData[]> {
  const response = await fetch(`${API_BASE_URL}/events`, {
    method: "GET",
    cache: "no-store",
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || "Failed to fetch events.");
  }

  const data = await response.json();

  return data.map((event: any) => ({
    id: Number(event.id),
    title: event.title,
    date: event.date,
    location: event.location,
    category: event.category,
    status: event.status ?? "ACTIVE",
  }));
}