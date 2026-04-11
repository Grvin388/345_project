"use client";

import { auth } from "@/lib/firebase";

const API_BASE_URL = "http://localhost:8080/api";

export type Reservation = {
  id: number;
  eventId: number;
  userUid: string;
  userEmail: string;
  quantity: number;
  status: string;
};

export async function getMyTickets(): Promise<Reservation[]> {
  const user = auth.currentUser;

  if (!user) {
    throw new Error("You must be logged in.");
  }

  const token = await user.getIdToken();

  const response = await fetch(`${API_BASE_URL}/reservations/me`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || "Failed to fetch tickets");
  }

  return response.json();
}

export async function cancelTicket(reservationId: number): Promise<Reservation> {
  const user = auth.currentUser;

  if (!user) {
    throw new Error("You must be logged in.");
  }

  const token = await user.getIdToken();

  const response = await fetch(`${API_BASE_URL}/reservations/${reservationId}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || "Failed to cancel ticket");
  }

  return response.json();
}