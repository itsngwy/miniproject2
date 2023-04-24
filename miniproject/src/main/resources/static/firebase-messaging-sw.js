importScripts("https://www.gstatic.com/firebasejs/9.19.1/firebase-app-compat.js");
importScripts("https://www.gstatic.com/firebasejs/9.19.1/firebase-messaging-compat.js");
import { environment } from "../../environments/environment";

firebase.initializeApp({
    apiKey: environment.firebase.apiKey,
    authDomain: "nus-miniproject-41100.firebaseapp.com",
    projectId: "nus-miniproject-41100",
    storageBucket: "nus-miniproject-41100.appspot.com",
    messagingSenderId: "499580987599",
    appId: "1:499580987599:web:e88cbf6f72a0c44446efba",
    measurementId: "G-1RCBNBP1KD"
});

const messaging = firebase.messaging();