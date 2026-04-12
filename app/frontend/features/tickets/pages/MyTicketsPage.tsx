"use client";

import { useEffect, useState } from "react";
import { getMyTickets, cancelTicket, Reservation } from "../services/ticketService";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { useAuth } from "@/features/auth/components/AuthProvider";

export function MyTicketsPage() {
  const { user, loading: authLoading } = useAuth();

  const [tickets, setTickets] = useState<Reservation[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [cancellingId, setCancellingId] = useState<number | null>(null);

  useEffect(() => {
    async function loadTickets() {
      if (authLoading) return;

      if (!user) {
        setError("You must be logged in.");
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        setError(null);
        const data = await getMyTickets();
        setTickets(data);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Failed to load tickets");
      } finally {
        setLoading(false);
      }
    }

    loadTickets();
  }, [user, authLoading]);

  const handleCancel = async (reservationId: number) => {
    try {
      setCancellingId(reservationId);
      const updatedTicket = await cancelTicket(reservationId);

      setTickets((prev) =>
        prev.map((ticket) =>
          ticket.id === reservationId ? updatedTicket : ticket
        )
      );
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to cancel ticket");
    } finally {
      setCancellingId(null);
    }
  };

  if (authLoading || loading) {
    return <div className="p-6">Loading your tickets...</div>;
  }

  if (error) {
    return <div className="p-6 text-red-600">{error}</div>;
  }

  return (
    <div className="p-6 space-y-6">
      <h1 className="text-2xl font-bold">My Tickets</h1>

      {tickets.length === 0 ? (
        <p className="text-slate-500">You do not have any tickets yet.</p>
      ) : (
        <div className="grid gap-4">
          {tickets.map((ticket) => (
            <Card key={ticket.id} className="p-4 space-y-3">
              <div className="space-y-1">
                <p><strong>Reservation ID:</strong> {ticket.id}</p>
                <p><strong>Event ID:</strong> {ticket.eventId}</p>
                <p><strong>Quantity:</strong> {ticket.quantity}</p>
                <p><strong>Status:</strong> {ticket.status}</p>
              </div>

              <Button
                onClick={() => handleCancel(ticket.id)}
                disabled={ticket.status === "CANCELLED" || cancellingId === ticket.id}
                className="bg-red-600 hover:bg-red-700 text-white"
              >
                {ticket.status === "CANCELLED"
                  ? "Cancelled"
                  : cancellingId === ticket.id
                  ? "Cancelling..."
                  : "Cancel Ticket"}
              </Button>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
}