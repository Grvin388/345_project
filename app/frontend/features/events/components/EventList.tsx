"use client";
import { useEvents } from "../hooks/useEvents";
import { Input } from "@/components/ui/input";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Calendar, MapPin, Search } from "lucide-react"; // Common icons
import styles from "../style/Events.module.css";

export function EventList() {
  const { events, searchQuery, setSearchQuery } = useEvents();

  return (
    <div className="space-y-10">
      {/* Search Header Section */}
      <div className="relative max-w-xl mx-auto md:mx-0">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-slate-400" />
        <Input 
          className="pl-10 h-12 shadow-sm border-slate-200 focus:ring-2 focus:ring-blue-500"
          placeholder="Search by concert, city, or category..." 
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </div>

      {/* Grid Layout */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        {events.length > 0 ? (
          events.map(event => (
            <Card key={event.id} className={styles.eventCard}>
              {/* Visual "Image" area */}
              <div className={styles.imagePlaceholder}>
                <span className="text-xs font-medium uppercase tracking-widest">Preview</span>
              </div>

              <div className={styles.cardHeader}>
                <span className={styles.categoryBadge}>{event.category}</span>
                <h3 className={styles.eventTitle}>{event.title}</h3>
                
                <div className={styles.detailRow}>
                  <Calendar className="h-4 w-4" />
                  <span>{event.date}</span>
                </div>
                
                <div className={styles.detailRow}>
                  <MapPin className="h-4 w-4" />
                  <span>{event.location}</span>
                </div>
              </div>

              <div className={styles.cardFooter}>
                <Button className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-5">
                  Reserve Ticket
                </Button>
              </div>
            </Card>
          ))
        ) : (
          <div className="col-span-full py-20 text-center">
            <p className="text-slate-400 text-lg italic">No events found matching your search.</p>
          </div>
        )}
      </div>
    </div>
  );
}