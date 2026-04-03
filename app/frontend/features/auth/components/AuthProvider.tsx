"use client";
import { createContext, useContext, useEffect, useState } from "react";
import {
  onAuthStateChanged,
  User,
  signInWithPopup,
  signOut,
} from "firebase/auth";
import { auth, googleProvider } from "@/lib/firebase";

const AuthContext = createContext<any>(null);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const [isAuthenticating, setIsAuthenticating] = useState(false);
  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (u) => {
      setUser(u);
      setLoading(false);
    });
    return () => unsubscribe();
  }, []);

  const login = async () => {
    if (isAuthenticating) return; // Prevent double clicks
    setIsAuthenticating(true);
    try {
      await signInWithPopup(auth, googleProvider);
    } catch (error) {
      console.error("Login failed", error);
    } finally {
      setIsAuthenticating(false); // Release the lock
    }
  };
  const logout = () => signOut(auth);

  return (
    <AuthContext.Provider
      value={{ user, loading, login, logout, isAuthenticating }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);
