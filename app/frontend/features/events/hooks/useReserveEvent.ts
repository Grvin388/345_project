"use client";

import { useState } from "react";
import { reserveTicket } from "../services/reservationService";

export function useReserveEvent() {
  const [loadingEventId, setLoadingEventId] = useState<number | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [successEventId, setSuccessEventId] = useState<number | null>(null);

  const reserve = async (eventId: number, quantity = 1) => {
    try {
      setLoadingEventId(eventId);
      setError(null);
      setSuccessEventId(null);

      await reserveTicket({ eventId, quantity });

      setSuccessEventId(eventId);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Reservation failed.");
    } finally {
      setLoadingEventId(null);
    }
  };

  return {
    reserve,
    loadingEventId,
    error,
    successEventId,
  };
}