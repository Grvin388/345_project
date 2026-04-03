import { act, renderHook, waitFor } from "@testing-library/react";
import { beforeEach, describe, expect, it, vi } from "vitest";

import type { EventData } from "../services/eventApi";
import * as eventApi from "../services/eventApi";
import { useEvents } from "./useEvents";

const mockEvents: EventData[] = [
  {
    id: "1",
    title: "Montreal Jazz Fest",
    date: "2026-07-01",
    location: "Montreal",
    category: "Music",
  },
  {
    id: "2",
    title: "Tech Summit",
    date: "2026-08-15",
    location: "Toronto",
    category: "Conference",
  },
  {
    id: "3",
    title: "Grand Prix",
    date: "2026-06-12",
    location: "Montreal",
    category: "Sports",
  },
];

describe("useEvents()", () => {
  beforeEach(() => {
    vi.restoreAllMocks();
    vi.spyOn(eventApi, "fetchEvents").mockResolvedValue(mockEvents);
  });

  it("loads all events when the search query is empty", async () => {
    const { result } = renderHook(() => useEvents());

    await waitFor(() => {
      expect(result.current.events).toHaveLength(3);
    });

    expect(result.current.searchQuery).toBe("");
  });

  it("filters events case-insensitively by title, location, or category", async () => {
    const { result } = renderHook(() => useEvents());

    await waitFor(() => {
      expect(result.current.events).toHaveLength(3);
    });

    act(() => {
      result.current.setSearchQuery("mOnTrEaL");
    });

    expect(result.current.events.map((event) => event.id)).toEqual(["1", "3"]);

    act(() => {
      result.current.setSearchQuery("conference");
    });

    expect(result.current.events.map((event) => event.id)).toEqual(["2"]);
  });

  it("returns no events when nothing matches the search query", async () => {
    const { result } = renderHook(() => useEvents());

    await waitFor(() => {
      expect(result.current.events).toHaveLength(3);
    });

    act(() => {
      result.current.setSearchQuery("opera");
    });

    expect(result.current.events).toEqual([]);
  });
});
