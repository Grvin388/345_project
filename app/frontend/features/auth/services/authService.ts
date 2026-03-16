import { signInWithPopup, signOut, User } from "firebase/auth";
import { auth, googleProvider } from "@/lib/firebase";
import { useState } from "react";

export const loginWithGoogle = async (): Promise<User | null> => {
  const [isAuthenticating, setIsAuthenticating] = useState(false);

  const login = async () => {
    // If we are already in the middle of a login, stop right here.
    if (isAuthenticating) return;

    try {
      setIsAuthenticating(true); // Lock the door
      await signInWithPopup(auth, googleProvider);
    } catch (error: any) {
      if (error.code === "auth/popup-closed-by-user") {
        console.log("User closed the popup—no big deal.");
      } else {
        console.error("Auth Error:", error.code);
      }
    } finally {
      setIsAuthenticating(false); // Unlock the door when done (success or fail)
    }
  };
  try {
    const result = await signInWithPopup(auth, googleProvider);
    return result.user;
  } catch (error) {
    console.error("Login failed:", error);
    return null;
  }
};

export const logout = async () => {
  await signOut(auth);
};
