# PERSONALIZED MUSIC APP - API CONTRACT

This document defines the REST API contract for the Personalized Music App Backend. All API requests and responses must adhere to the formats and structures specified here.

## General Requirements
- **Protocol**: HTTP/HTTPS
- **Base URL**: `/api/v1`
- **Content-Type**: `application/json` for all requests/responses (except streaming which uses `audio/mpeg`).
- **Naming Convention**: All JSON keys **MUST** use `camelCase`.

---

## 1. Authentication & User Management (Auth)

### 1.1 Register Account
* **Endpoint**: `POST /api/v1/auth/register`
* **Request Body**:
  ```json
  {
    "email": "user@musicapp.com",
    "username": "demo_user",
    "password": "SecurePassword123",
    "fullName": "Demo User",
    "phone": "0987654321"
  }
  ```
* **Success Response (`HTTP 201 Created`)**:
  ```json
  {
    "userId": 2,
    "email": "user@musicapp.com",
    "username": "demo_user",
    "fullName": "Demo User",
    "status": "ACTIVE",
    "createdAt": "2026-09-13T10:00:00Z"
  }
  ```

### 1.2 Login Account
* **Endpoint**: `POST /api/v1/auth/login`
* **Request Body**:
  ```json
  {
    "username": "demo_user",
    "password": "SecurePassword123"
  }
  ```
* **Success Response (`HTTP 200 OK`)**:
  ```json
  {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "709218d6-e91e-450f-904c-35a16d00122e",
    "tokenType": "Bearer",
    "expiresIn": 3600,
    "user": {
      "id": 2,
      "email": "user@musicapp.com",
      "username": "demo_user",
      "fullName": "Demo User",
      "avatarUrl": "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200",
      "roles": ["ROLE_USER"]
    }
  }
  ```

### 1.3 Refresh Token & Logout
* **Refresh Endpoint**: `POST /api/v1/auth/refresh`
  - Body: `{"refreshToken": "token-uuid"}`
  - Response: `{"accessToken": "new-access-token", "refreshToken": "new-refresh-token", "tokenType": "Bearer", "expiresIn": 3600}`
* **Logout Endpoint**: `POST /api/v1/auth/logout`
  - Body: `{"refreshToken": "token-uuid"}`
  - Response: `{"message": "Logged out successfully"}`

---

## 2. Home Dashboard (Home)

### 2.1 Get Homepage Data
* **Endpoint**: `GET /api/v1/home/data`
* **Success Response (`HTTP 200 OK`)**:
  ```json
  {
    "banners": [
      {
        "id": 1,
        "title": "Feel the Beat",
        "subtitle": "Dive into the trending tracks of this week",
        "ctaText": "Listen Now",
        "backgroundImageUrl": "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=800",
        "targetType": "song",
        "targetId": 1
      }
    ],
    "popularSongs": [
      {
        "id": 1,
        "title": "Demo Song 1",
        "slug": "demo-song-1",
        "durationSeconds": 210,
        "audioUrl": "/api/v1/songs/1/stream",
        "coverImageUrl": "/storage/covers/demo-song-1.jpg",
        "playCount": 1500,
        "likeCount": 120,
        "isVipOnly": false,
        "isDownloadable": true,
        "artists": [
          {
            "id": 1,
            "name": "Demo Artist",
            "slug": "demo-artist"
          }
        ]
      }
    ],
    "categories": [
      {
        "id": 1,
        "name": "Pop",
        "slug": "pop",
        "coverImageUrl": "/storage/covers/pop.jpg"
      }
    ],
    "recommendedPlaylists": [
      {
        "id": 2,
        "title": "Top Trending",
        "slug": "top-trending",
        "description": "Playlist hệ thống các bài hát phổ biến",
        "coverImageUrl": "/storage/covers/top-trending.jpg",
        "totalSongs": 2,
        "totalDurationSeconds": 450
      }
    ]
  }
  ```


---

## 3. Song Catalog (Song)

### 3.1 Get All Songs
* **Endpoint**: `GET /api/v1/songs`
* **Success Response (`HTTP 200 OK`)**:
  ```json
  [
    {
      "id": 1,
      "title": "Demo Song 1",
      "slug": "demo-song-1",
      "durationSeconds": 210,
      "audioUrl": "/api/v1/songs/1/stream",
      "coverImageUrl": "/storage/covers/demo-song-1.jpg",
      "playCount": 1500,
      "likeCount": 120,
      "isVipOnly": false,
      "isDownloadable": true,
      "artists": [
        {
          "id": 1,
          "name": "Demo Artist",
          "slug": "demo-artist"
        }
      ]
    }
  ]
  ```

### 3.2 Get Song Details
* **Endpoint**: `GET /api/v1/songs/{id}`
* **Success Response (`HTTP 200 OK`)**:
  ```json
  {
    "id": 1,
    "title": "Demo Song 1",
    "slug": "demo-song-1",
    "durationSeconds": 210,
    "audioUrl": "/api/v1/songs/1/stream",
    "coverImageUrl": "/storage/covers/demo-song-1.jpg",
    "fileSizeBytes": 5242880,
    "mimeType": "audio/mpeg",
    "bitrate": 320,
    "playCount": 1500,
    "likeCount": 120,
    "isVipOnly": false,
    "isDownloadable": true,
    "releaseDate": "2026-05-01",
    "artists": [
      {
        "id": 1,
        "name": "Demo Artist",
        "slug": "demo-artist"
      }
    ],
    "genres": [
      {
        "id": 1,
        "name": "Pop",
        "slug": "pop"
      }
    ],
    "lyrics": {
      "id": 1,
      "content": "Lời bài hát demo 1...",
      "syncedLyrics": "[00:10.00]Lời bài hát demo 1...",
      "language": "vi"
    }
  }
  ```

---

## 4. Audio Streaming (Stream)

### 4.1 Stream Music File
* **Endpoint**: `GET /api/v1/songs/{id}/stream`
* **Description**: Streams the audio file of a specified song with support for byte-range requests (essential for scrub and pause operations on mobile devices).
* **Headers**:
  - `Range`: `bytes=start-end` (e.g., `bytes=0-1048575` to request the first 1MB)
* **Response Headers**:
  - `Content-Type`: `audio/mpeg`
  - `Accept-Ranges`: `bytes`
  - `Content-Range`: `bytes start-end/total`
  - `Content-Length`: Size of the returned chunk
* **Success Response Status**:
  - `HTTP 206 Partial Content` (when executing a range request)
  - `HTTP 200 OK` (when requesting the entire file without Range header)

---

## 5. Playlists (Playlist)

### 5.1 Get User Playlists
* **Endpoint**: `GET /api/v1/playlists`
* **Success Response (`HTTP 200 OK`)**:
  ```json
  [
    {
      "id": 1,
      "title": "Playlist yêu thích của tôi",
      "slug": "playlist-yeu-thich-cua-toi",
      "description": "Playlist demo của user",
      "coverImageUrl": "/storage/covers/my-favorite.jpg",
      "visibility": "PRIVATE",
      "playlistType": "USER_CREATED",
      "totalSongs": 2,
      "totalDurationSeconds": 405
    }
  ]
  ```

### 5.2 Get Playlist Details
* **Endpoint**: `GET /api/v1/playlists/{id}`
* **Success Response (`HTTP 200 OK`)**:
  ```json
  {
    "id": 1,
    "title": "Playlist yêu thích của tôi",
    "slug": "playlist-yeu-thich-cua-toi",
    "description": "Playlist demo của user",
    "coverImageUrl": "/storage/covers/my-favorite.jpg",
    "visibility": "PRIVATE",
    "playlistType": "USER_CREATED",
    "totalSongs": 2,
    "totalDurationSeconds": 405,
    "songs": [
      {
        "id": 1,
        "title": "Demo Song 1",
        "slug": "demo-song-1",
        "durationSeconds": 210,
        "audioUrl": "/api/v1/songs/1/stream",
        "coverImageUrl": "/storage/covers/demo-song-1.jpg",
        "playCount": 1500,
        "likeCount": 120,
        "isVipOnly": false,
        "isDownloadable": true,
        "artists": [
          {
            "id": 1,
            "name": "Demo Artist",
            "slug": "demo-artist"
          }
        ]
      }
    ]
  }
  ```

