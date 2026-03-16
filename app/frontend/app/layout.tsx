import { AuthProvider } from "@/features/auth/components/AuthProvider"; // Adjust path if needed
import { Navbar } from "@/components/Navbar/Navbar";
import "./globals.css";

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body className="antialiased bg-slate-50">
        {/* 1. The Provider creates the "Auth Cloud" around everything inside it */}
        <AuthProvider>
          
          {/* 2. Now Navbar is inside the cloud and can see the 'user' state */}
          <Navbar />
          
          <main>
            {children}
          </main>
          
        </AuthProvider>
      </body>
    </html>
  );
}