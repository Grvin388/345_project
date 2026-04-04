import { describe, expect, it } from "vitest";

import { fetchEvents } from "./eventApi";

describe("fetchEvents()", () => {
  it("returns the seeded list of events", async () => {
    const events = await fetchEvents();

    expect(events).toHaveLength(3);
    expect(events).toEqual(
      expect.arrayContaining([
        expect.objectContaining({
          id: "1",
          title: "Montreal Jazz Fest",
          date: "2026-07-01",
          location: "Montreal",
          category: "Music",
        }),
      ]),
    );
  });

  it("returns events with the expected shape", async () => {
    const events = await fetchEvents();

    for (const event of events) {
      expect(event).toMatchObject({
        id: expect.any(String),
        title: expect.any(String),
        date: expect.any(String),
        location: expect.any(String),
        category: expect.any(String),
      });
    }
  });
});
