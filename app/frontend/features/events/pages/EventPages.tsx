import { EventList } from "../components/EventList";

export function EventsPage() {
  return (
    <main className="min-h-screen bg-slate-50 p-8">
      <div className="max-w-6xl mx-auto">
        <header className="mb-10">
          <h1 className="text-3xl font-bold text-slate-900">Available Events</h1>
          <p className="text-slate-500">Find and reserve your next experience</p>
        </header>
        
        {/* Render the assembled component */}
        <EventList />
      </div>
    </main>
  );
}