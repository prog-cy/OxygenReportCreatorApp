# Oxygen Report Creator

An Android Application to create a Oxygen Report in pdf format.

## Requirements
- Android Studio
- JDK(Java Development Key)

## Tech Stack

**Design:** XML(Extensible Markup Language)

**Backend:** Java

**Firebase:** Firebase Authentication, Firebase FireStoreDB.

## Dependencies
Add below dependencies under (Gradle Script) inside Module Sections.

**These four Dependencies for Firebase services**
- implementation platform('com.google.firebase:firebase-bom:30.4.1')
-  implementation 'com.google.firebase:firebase-firestore'
- implementation 'com.google.firebase:firebase-auth'

## Permissions

**Add Permissions inside Manifests**

- uses-permission android:name="android.permission.STORAGE"
- uses-permission android:name="android.permission.INTERNET"

## Screenshots