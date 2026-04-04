import { cleanup, fireEvent, render, screen } from "@testing-library/react";
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";

import { EventList } from "./EventList";
import * as useEventsModule from "../hooks/useEvents";

vi.mock("../hooks/useEvents", () => ({
  useEvents: vi.fn(),
}));

describe("EventList", () => {
  const mockedUseEvents = vi.mocked(useEventsModule.useEvents);

  beforeEach(() => {
    mockedUseEvents.mockReset();
  });

  afterEach(() => {
    cleanup();
  });

  it("renders events returned by the hook", () => {
    mockedUseEvents.mockReturnValue({
      events: [
        {
          id: "1",
          title: "Montreal Jazz Fest",
          date: "2026-07-01",
          location: "Montreal",
          category: "Music",
        },
      ],
      searchQuery: "",
      setSearchQuery: vi.fn(),
    });

    render(<EventList />);

    expect(screen.getByText("Montreal Jazz Fest")).toBeDefined();
    expect(screen.getByText("Music")).toBeDefined();
    expect(screen.getByText("Reserve Ticket")).toBeDefined();
  });

  it("shows the empty state when there are no matching events", () => {
    mockedUseEvents.mockReturnValue({
      events: [],
      searchQuery: "opera",
      setSearchQuery: vi.fn(),
    });

    render(<EventList />);

    expect(
      screen.getByText("No events found matching your search."),
    ).toBeDefined();
  });

  it("forwards search input changes to the hook setter", () => {
    const setSearchQuery = vi.fn();

    mockedUseEvents.mockReturnValue({
      events: [],
      searchQuery: "",
      setSearchQuery,
    });

    render(<EventList />);

    fireEvent.change(
      screen.getByPlaceholderText("Search by concert, city, or category..."),
      {
        target: { value: "jazz" },
      },
    );

    expect(setSearchQuery).toHaveBeenCalledWith("jazz");
  });
});
