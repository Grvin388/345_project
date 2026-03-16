"use client";
import Link from "next/link";
import { Ticket, LogOut, User as UserIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useAuth } from "@/features/auth/components/AuthProvider";
import styles from "./Navbar.module.css";

export function Navbar() {
  const { user, login, logout, loading, isAuthenticating } = useAuth();

  return (
    <nav className={styles.navWrapper}>
      <div className={styles.navContainer}>
        {/* Brand Logo */}
        <Link href="/" className={styles.brandLogo}>
          <div className={styles.logoIcon}>
            <Ticket className="h-5 w-5 text-white" />
          </div>
          <span className={styles.brandText}>TicketReserve</span>
        </Link>

        {/* Center Links */}
        <div className={styles.navLinks}>
          <Link href="/" className={styles.navLink}>
            Browse Events
          </Link>
          <Link href="/reservations" className={styles.navLink}>
            My Tickets
          </Link>
          <Link href="/admin" className={styles.navLink}>
            Admin
          </Link>
        </div>

        {/* Right Side: Auth UI */}
        <div className={styles.authSection}>
          {loading ? (
            <div className={styles.skeletonPulse} />
          ) : user ? (
            <div className="flex items-center gap-4">
              <div className={styles.userProfile}>
                {user.photoURL ? (
                  <img
                    src={user.photoURL}
                    alt="Profile"
                    className={styles.userAvatar}
                  />
                ) : (
                  <UserIcon className="h-5 w-5" />
                )}
                <span className="hidden sm:inline">{user.displayName}</span>
              </div>
              <Button
                variant="ghost"
                size="sm"
                onClick={logout}
                className="text-red-600 hover:text-red-700"
              >
                <LogOut className="h-4 w-4" />
              </Button>
            </div>
          ) : (
            <>
              <Button variant="ghost" size="sm" onClick={login} disabled={isAuthenticating}>
                Sign In
              </Button>
              <Button
                onClick={login}
                size="sm"
                className="bg-blue-600 hover:bg-blue-700 text-white"
                disabled={isAuthenticating}
              >
                Get Started
              </Button>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}
