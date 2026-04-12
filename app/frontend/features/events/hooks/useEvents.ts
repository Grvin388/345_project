"use client";

import { useEffect, useMemo, useState } from "react";
import { EventData, fetchEvents } from "../services/eventApi";

export function useEvents() {
  const [allEvents, setAllEvents] = useState<EventData[]>([]);
  const [searchQuery, setSearchQuery] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function loadEvents() {
      try {
        setLoading(true);
        setError(null);

        const data = await fetchEvents();
        setAllEvents(data);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Failed to load events.");
      } finally {
        setLoading(false);
      }
    }

    loadEvents();
  }, []);

  const events = useMemo(() => {
    const query = searchQuery.trim().toLowerCase();

    return allEvents
      .filter((event) => event.status !== "CANCELLED")
      .filter((event) => {
        if (!query) return true;

        return (
          event.title.toLowerCase().includes(query) ||
          event.location.toLowerCase().includes(query) ||
          event.category.toLowerCase().includes(query)
        );
      });
  }, [allEvents, searchQuery]);

  return {
    events,
    loading,
    error,
    searchQuery,
    setSearchQuery,
    refreshEvents: async () => {
      try {
        setLoading(true);
        setError(null);

        const data = await fetchEvents();
        setAllEvents(data);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Failed to refresh events.");
      } finally {
        setLoading(false);
      }
    },
  };
}