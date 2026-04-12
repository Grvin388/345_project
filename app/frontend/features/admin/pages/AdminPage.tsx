"use client";

import { useEffect, useState } from "react";
import { Calendar, MapPin, Pencil, Plus, Trash2, Tag } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import {
  AdminEvent,
  cancelAdminEvent,
  createAdminEvent,
  fetchAdminEvents,
  updateAdminEvent,
} from "../services/adminEventApi";
import styles from "../styles/AdminPage.module.css";

type FormData = {
  title: string;
  date: string;
  location: string;
  category: string;
};

const emptyForm: FormData = {
  title: "",
  date: "",
  location: "",
  category: "",
};

export function AdminPage() {
  const [events, setEvents] = useState<AdminEvent[]>([]);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [formData, setFormData] = useState<FormData>(emptyForm);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [cancellingId, setCancellingId] = useState<number | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  useEffect(() => {
    loadEvents();
  }, []);

  async function loadEvents() {
    try {
      setLoading(true);
      setError(null);

      const data = await fetchAdminEvents();
      setEvents(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to load events.");
    } finally {
      setLoading(false);
    }
  }

  const handleChange = (field: keyof FormData, value: string) => {
    setFormData((prev) => ({
      ...prev,
      [field]: value,
    }));
  };

  const resetForm = () => {
    setEditingId(null);
    setFormData(emptyForm);
  };

  const validateForm = () => {
    return (
      formData.title.trim() &&
      formData.date.trim() &&
      formData.location.trim() &&
      formData.category.trim()
    );
  };

  const handleAddEvent = async () => {
    if (!validateForm()) {
      setError("Please fill in all fields before adding an event.");
      return;
    }

    try {
      setSaving(true);
      setError(null);
      setSuccessMessage(null);

      const createdEvent = await createAdminEvent(formData);
      setEvents((prev) => [createdEvent, ...prev]);
      setSuccessMessage("Event added successfully.");
      resetForm();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to add event.");
    } finally {
      setSaving(false);
    }
  };

  const handleStartEdit = (event: AdminEvent) => {
    setEditingId(event.id);
    setError(null);
    setSuccessMessage(null);
    setFormData({
      title: event.title,
      date: event.date,
      location: event.location,
      category: event.category,
    });
  };

  const handleSaveEdit = async () => {
    if (!editingId) return;

    if (!validateForm()) {
      setError("Please fill in all fields before saving changes.");
      return;
    }

    try {
      setSaving(true);
      setError(null);
      setSuccessMessage(null);

      const updatedEvent = await updateAdminEvent(editingId, formData);

      setEvents((prev) =>
        prev.map((event) => (event.id === editingId ? updatedEvent : event))
      );

      setSuccessMessage("Event updated successfully.");
      resetForm();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to update event.");
    } finally {
      setSaving(false);
    }
  };

  const handleCancelEdit = () => {
    resetForm();
    setError(null);
    setSuccessMessage(null);
  };

  const handleCancelEvent = async (id: number) => {
    try {
      setCancellingId(id);
      setError(null);
      setSuccessMessage(null);

      const cancelledEvent = await cancelAdminEvent(id);

      setEvents((prev) =>
        prev.map((event) => (event.id === id ? cancelledEvent : event))
      );

      setSuccessMessage("Event cancelled successfully.");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to cancel event.");
    } finally {
      setCancellingId(null);
    }
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

      {error && (
        <div className="rounded-md border border-red-200 bg-red-50 px-4 py-3 text-red-700">
          {error}
        </div>
      )}

      {successMessage && (
        <div className="rounded-md border border-green-200 bg-green-50 px-4 py-3 text-green-700">
          {successMessage}
        </div>
      )}

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
                disabled={saving}
              >
                <Pencil className="h-4 w-4 mr-2" />
                {saving ? "Saving..." : "Save Changes"}
              </Button>

              <Button variant="outline" onClick={handleCancelEdit} disabled={saving}>
                Cancel
              </Button>
            </>
          ) : (
            <Button
              className="bg-blue-600 hover:bg-blue-700 text-white"
              onClick={handleAddEvent}
              disabled={saving}
            >
              <Plus className="h-4 w-4 mr-2" />
              {saving ? "Adding..." : "Add Event"}
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

        {loading ? (
          <div className="py-12 text-center">
            <p className="text-slate-400 text-lg italic">Loading events...</p>
          </div>
        ) : (
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
                    disabled={
                      event.status === "CANCELLED" || cancellingId === event.id
                    }
                  >
                    <Trash2 className="h-4 w-4 mr-2" />
                    {event.status === "CANCELLED"
                      ? "Cancelled"
                      : cancellingId === event.id
                      ? "Cancelling..."
                      : "Cancel Event"}
                  </Button>
                </div>
              </Card>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}