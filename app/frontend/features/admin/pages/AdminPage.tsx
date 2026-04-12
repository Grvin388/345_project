"use client";

import { useState } from "react";
import { Calendar, MapPin, Pencil, Plus, Trash2, Tag } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import styles from "../styles/AdminPage.module.css";

type AdminEvent = {
  id: string;
  title: string;
  date: string;
  location: string;
  category: string;
  status: "ACTIVE" | "CANCELLED";
};

const initialEvents: AdminEvent[] = [
  {
    id: "1",
    title: "Montreal Jazz Fest",
    date: "2026-07-01",
    location: "Montreal",
    category: "Music",
    status: "ACTIVE",
  },
  {
    id: "2",
    title: "Tech Summit",
    date: "2026-08-15",
    location: "Toronto",
    category: "Conference",
    status: "ACTIVE",
  },
  {
    id: "3",
    title: "Grand Prix",
    date: "2026-06-12",
    location: "Montreal",
    category: "Sports",
    status: "CANCELLED",
  },
];

export function AdminPage() {
  const [events, setEvents] = useState<AdminEvent[]>(initialEvents);
  const [editingId, setEditingId] = useState<string | null>(null);

  const [formData, setFormData] = useState({
    title: "",
    date: "",
    location: "",
    category: "",
  });

  const handleChange = (field: string, value: string) => {
    setFormData((prev) => ({
      ...prev,
      [field]: value,
    }));
  };

  const handleAddEvent = () => {
    if (!formData.title || !formData.date || !formData.location || !formData.category) {
      return;
    }

    const newEvent: AdminEvent = {
      id: Date.now().toString(),
      title: formData.title,
      date: formData.date,
      location: formData.location,
      category: formData.category,
      status: "ACTIVE",
    };

    setEvents((prev) => [newEvent, ...prev]);
    setFormData({
      title: "",
      date: "",
      location: "",
      category: "",
    });
  };

  const handleStartEdit = (event: AdminEvent) => {
    setEditingId(event.id);
    setFormData({
      title: event.title,
      date: event.date,
      location: event.location,
      category: event.category,
    });
  };

  const handleSaveEdit = () => {
    if (!editingId) return;

    setEvents((prev) =>
      prev.map((event) =>
        event.id === editingId
          ? {
              ...event,
              title: formData.title,
              date: formData.date,
              location: formData.location,
              category: formData.category,
            }
          : event
      )
    );

    setEditingId(null);
    setFormData({
      title: "",
      date: "",
      location: "",
      category: "",
    });
  };

  const handleCancelEdit = () => {
    setEditingId(null);
    setFormData({
      title: "",
      date: "",
      location: "",
      category: "",
    });
  };

  const handleCancelEvent = (id: string) => {
    setEvents((prev) =>
      prev.map((event) =>
        event.id === id ? { ...event, status: "CANCELLED" } : event
      )
    );
  };

  return (
    <div className={styles.adminPage}>
      <div className={styles.heroSection}>
        <span className={styles.heroBadge}>Admin Dashboard</span>
        <h1 className={styles.heroTitle}>Manage Events</h1>
        <p className={styles.heroSubtitle}>
          Add new events, update existing details, and cancel events through a clean
          administrative interface.
        </p>
      </div>

      <Card className={styles.formCard}>
        <div className={styles.formHeader}>
          <div>
            <h2 className={styles.sectionTitle}>
              {editingId ? "Edit Event" : "Add New Event"}
            </h2>
            <p className={styles.sectionDescription}>
              Fill in the event information below.
            </p>
          </div>
        </div>

        <div className={styles.formGrid}>
          <div className={styles.inputGroup}>
            <label className={styles.label}>Event Title</label>
            <Input
              placeholder="Enter event title"
              value={formData.title}
              onChange={(e) => handleChange("title", e.target.value)}
            />
          </div>

          <div className={styles.inputGroup}>
            <label className={styles.label}>Date</label>
            <Input
              type="date"
              value={formData.date}
              onChange={(e) => handleChange("date", e.target.value)}
            />
          </div>

          <div className={styles.inputGroup}>
            <label className={styles.label}>Location</label>
            <Input
              placeholder="Enter city or venue"
              value={formData.location}
              onChange={(e) => handleChange("location", e.target.value)}
            />
          </div>

          <div className={styles.inputGroup}>
            <label className={styles.label}>Category</label>
            <Input
              placeholder="Music, Sports, Conference..."
              value={formData.category}
              onChange={(e) => handleChange("category", e.target.value)}
            />
          </div>
        </div>

        <div className={styles.formActions}>
          {editingId ? (
            <>
              <Button
                className="bg-blue-600 hover:bg-blue-700 text-white"
                onClick={handleSaveEdit}
              >
                <Pencil className="h-4 w-4 mr-2" />
                Save Changes
              </Button>

              <Button variant="outline" onClick={handleCancelEdit}>
                Cancel
              </Button>
            </>
          ) : (
            <Button
              className="bg-blue-600 hover:bg-blue-700 text-white"
              onClick={handleAddEvent}
            >
              <Plus className="h-4 w-4 mr-2" />
              Add Event
            </Button>
          )}
        </div>
      </Card>

      <div className={styles.listSection}>
        <div className={styles.listHeader}>
          <h2 className={styles.sectionTitle}>Existing Events</h2>
          <p className={styles.sectionDescription}>
            Review and manage all currently available events.
          </p>
        </div>

        <div className={styles.eventGrid}>
          {events.map((event) => (
            <Card key={event.id} className={styles.eventCard}>
              <div className={styles.cardTop}>
                <span
                  className={
                    event.status === "ACTIVE"
                      ? styles.statusActive
                      : styles.statusCancelled
                  }
                >
                  {event.status}
                </span>
                <span className={styles.categoryBadge}>{event.category}</span>
              </div>

              <h3 className={styles.eventTitle}>{event.title}</h3>

              <div className={styles.detailRow}>
                <Calendar className="h-4 w-4" />
                <span>{event.date}</span>
              </div>

              <div className={styles.detailRow}>
                <MapPin className="h-4 w-4" />
                <span>{event.location}</span>
              </div>

              <div className={styles.detailRow}>
                <Tag className="h-4 w-4" />
                <span>{event.category}</span>
              </div>

              <div className={styles.cardActions}>
                <Button
                  variant="outline"
                  className={styles.editButton}
                  onClick={() => handleStartEdit(event)}
                >
                  <Pencil className="h-4 w-4 mr-2" />
                  Edit
                </Button>

                <Button
                  className={styles.cancelButton}
                  onClick={() => handleCancelEvent(event.id)}
                  disabled={event.status === "CANCELLED"}
                >
                  <Trash2 className="h-4 w-4 mr-2" />
                  {event.status === "CANCELLED" ? "Cancelled" : "Cancel Event"}
                </Button>
              </div>
            </Card>
          ))}
        </div>
      </div>
    </div>
  );
}