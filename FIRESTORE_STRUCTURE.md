# Firebase Firestore Database Structure for Vita Habitos

## Overview

The Vita Habitos app uses Firebase Firestore for user authentication and data storage with per-user data isolation. Each user can only access and modify their own data.

## Collections Structure

### 1. `users` Collection

Root collection that stores user profile information.

**Document ID**: Firebase Auth UID (auto-generated)

**Fields**:

- `name` (string): User's display name
- `email` (string): User's email address
- `bio` (string): User biography/description
- `joinDate` (string): Date when user joined (ISO format)

**Example**:

```
users/
  └── {userId}/
        ├── name: "Juan Pérez"
        ├── email: "juan@example.com"
        ├── bio: "Nuevo usuario de Vita Habitos"
        └── joinDate: "2025-10-16"
```

### 2. `habits` Subcollection (Per User)

Each user has their own habits subcollection under their user document.

**Path**: `users/{userId}/habits`
**Document ID**: Habit ID (integer as string)

**Fields**:

- `name` (string): Habit name
- `description` (string): Habit description
- `frequency` (string): "DAILY", "WEEKLY", or "MONTHLY"
- `color` (string): Hex color code (e.g., "#4CAF50")
- `icon` (string): Emoji or icon character
- `createdAt` (string): Creation timestamp (ISO format)
- `isActive` (boolean): Whether the habit is active
- `reminderTime` (string | null): Reminder time in HH:mm format
- `finishDate` (string | null): End date in yyyy-MM-dd format

**Example**:

```
users/
  └── {userId}/
        └── habits/
              ├── 1234567890/
              │     ├── name: "Ejercicio diario"
              │     ├── description: "30 minutos de ejercicio"
              │     ├── frequency: "DAILY"
              │     ├── color: "#4CAF50"
              │     ├── icon: "✓"
              │     ├── createdAt: "2025-10-16T10:30:00"
              │     ├── isActive: true
              │     ├── reminderTime: "09:00"
              │     └── finishDate: "2025-12-31"
              └── 1234567891/
                    ├── name: "Leer libro"
                    ├── description: "Leer 20 páginas"
                    ├── frequency: "WEEKLY"
                    └── ...
```

### 3. `habitEntries` Subcollection (Per User)

Tracks when users complete their habits.

**Path**: `users/{userId}/habitEntries`
**Document ID**: Entry ID (timestamp-based integer as string)

**Fields**:

- `habitId` (number): Reference to the habit ID
- `completedDate` (string): Date when habit was completed (yyyy-MM-dd)
- `completedTime` (string): Time when habit was completed (HH:mm:ss)
- `notes` (string): Optional notes about the completion

**Example**:

```
users/
  └── {userId}/
        └── habitEntries/
              ├── 1729073400001/
              │     ├── habitId: 1234567890
              │     ├── completedDate: "2025-10-16"
              │     ├── completedTime: "14:30:00"
              │     └── notes: ""
              └── 1729159800001/
                    ├── habitId: 1234567890
                    ├── completedDate: "2025-10-17"
                    └── ...
```

## Security Rules

To enable per-user data isolation, configure Firestore Security Rules:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {

    // Users can only read/write their own user document
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;

      // Users can only access their own habits
      match /habits/{habitId} {
        allow read, write: if request.auth != null && request.auth.uid == userId;
      }

      // Users can only access their own habit entries
      match /habitEntries/{entryId} {
        allow read, write: if request.auth != null && request.auth.uid == userId;
      }
    }
  }
}
```

## Indexes

Create these composite indexes for better query performance:

1. **habits Collection**:

   - Collection ID: `habits`
   - Fields: `isActive` (Ascending), `createdAt` (Descending)

2. **habitEntries Collection**:
   - Collection ID: `habitEntries`
   - Fields: `habitId` (Ascending), `completedDate` (Descending)

## Authentication

- **Method**: Firebase Authentication with Email/Password
- **User Management**: Automatic user creation on signup
- **Session Persistence**: Handled by SessionManager with SharedPreferences

## Real-time Updates

The app uses Firestore's real-time listeners to automatically sync data:

- Habits are updated in real-time across devices
- Habit completions sync immediately
- Changes from other devices appear automatically

## Data Privacy

✅ Each user's data is completely isolated  
✅ Users cannot see or modify other users' habits  
✅ All queries are scoped to the authenticated user's ID  
✅ Firestore Security Rules enforce data isolation at the database level
