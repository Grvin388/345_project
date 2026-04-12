"use client";

import { auth } from "@/lib/firebase";
const API_BASE_URL = "http://localhost:8080/api";

export type ReservationPayload = {
  eventId: number;
  quantity: number;
};

export async function reserveTicket(payload: ReservationPayload) {
  const user = auth.currentUser;

  if (!user) {
    throw new Error("You must be logged in to reserve a ticket.");
  }

  const token = await user.getIdToken();

  const response = await fetch(`${API_BASE_URL}/reservations`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || "Failed to reserve ticket.");
  }

  return response.json();
}