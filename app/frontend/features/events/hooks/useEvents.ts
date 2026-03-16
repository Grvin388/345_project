import { useState, useEffect } from "react";
import { fetchEvents, EventData } from "../services/eventApi";

export function useEvents() {
  const [events, setEvents] = useState<EventData[]>([]);
  const [searchQuery, setSearchQuery] = useState("");

  useEffect(() => {
    fetchEvents().then((data) => setEvents(data));
  }, []);

  // Filter logic to fulfill the functional requirement 
  const filteredEvents = events.filter((e) => 
    e.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
    e.location.toLowerCase().includes(searchQuery.toLowerCase()) ||
    e.category.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return { events: filteredEvents, searchQuery, setSearchQuery };
}