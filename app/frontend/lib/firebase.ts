
import { initializeApp, getApps, getApp } from "firebase/app";
import { getAuth, GoogleAuthProvider } from "firebase/auth"; // REMOVED /web-extension

const firebaseConfig = {
  apiKey: "AIzaSyAbCe8iijIejcU1mXPJtKKc5WaB7zvA4N4",
  authDomain: "soen345-42fbf.firebaseapp.com",
  projectId: "soen345-42fbf",
  storageBucket: "soen345-42fbf.firebasestorage.app",
  messagingSenderId: "996634598323",
  appId: "1:996634598323:web:71638662f0a1ad0f6b09c6",
  measurementId: "G-N75QY0ECV5"
};

const app = getApps().length > 0 ? getApp() : initializeApp(firebaseConfig);

export const auth = getAuth(app);
export const googleProvider = new GoogleAuthProvider();

googleProvider.setCustomParameters({ prompt: 'select_account' });