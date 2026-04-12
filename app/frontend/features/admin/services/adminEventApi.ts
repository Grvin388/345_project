const API_BASE_URL = "http://localhost:8080/api";

export type AdminEvent = {
  id: number;
  title: string;
  date: string;
  location: string;
  category: string;
  status: "ACTIVE" | "CANCELLED";
};

export type EventPayload = {
  title: string;
  date: string;
  location: string;
  category: string;
};

export async function fetchAdminEvents(): Promise<AdminEvent[]> {
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

export async function createAdminEvent(payload: EventPayload): Promise<AdminEvent> {
  const response = await fetch(`${API_BASE_URL}/events`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || "Failed to create event.");
  }

  const event = await response.json();

  return {
    id: Number(event.id),
    title: event.title,
    date: event.date,
    location: event.location,
    category: event.category,
    status: event.status ?? "ACTIVE",
  };
}

export async function updateAdminEvent(
  id: number,
  payload: EventPayload
): Promise<AdminEvent> {
  const response = await fetch(`${API_BASE_URL}/events/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || "Failed to update event.");
  }

  const event = await response.json();

  return {
    id: Number(event.id),
    title: event.title,
    date: event.date,
    location: event.location,
    category: event.category,
    status: event.status ?? "ACTIVE",
  };
}

export async function cancelAdminEvent(id: number): Promise<AdminEvent> {
  const response = await fetch(`${API_BASE_URL}/events/${id}/cancel`, {
    method: "PATCH",
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || "Failed to cancel event.");
  }

  const event = await response.json();

  return {
    id: Number(event.id),
    title: event.title,
    date: event.date,
    location: event.location,
    category: event.category,
    status: event.status ?? "ACTIVE",
  };
}