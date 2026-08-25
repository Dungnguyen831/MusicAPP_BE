# Backend Development Tasks for Mobile Music Streaming App

This document outlines the development phases and tasks for the Spring Boot Backend designed to serve the Flutter Mobile Music Streaming application using a Clean Layered Architecture.

---

## PHASES & TASKS

### Phase 1: Domain Entities & Database Seed
- [x] **Initialize Domain Entities**
  - Define `Song` model representing songs with streaming details.
  - Define `Artist` model representing artists.
  - Define `BannerItem` model representing promotional sliders.
- [x] **Spring Data JPA Repositories**
  - Create `SongRepository`, `ArtistRepository`, `BannerRepository` extending JPA interfaces.
- [x] **H2 Database Integration**
  - Configure `application.yml` for H2 console, JDBC URLs, and Hibernate Auto-DDL settings.
- [x] **Data Seeding & Initialization**
  - Create `DataSeeder` (`CommandLineRunner`) to populate mock songs, artists, and banners.
  - Use high-quality mock stream links and covers compatible with Flutter's UI.

### Phase 2: Core Streaming & REST APIs
- [x] **Audio Streaming Service (Partial Content Support)**
  - Implement `SongStreamingService` to stream audio with HTTP Range Header support (`HTTP 206 Partial Content`).
  - Support smooth scrubbing/seeking for Flutter mobile client.
- [x] **REST Controllers**
  - `HomeController`: Endpoint `GET /api/v1/home/data` returning dynamic homepage payloads (Banners, Popular Songs, Categories, Artists).
  - `SongController`: Endpoint `GET /api/v1/songs` to query all songs and `GET /api/v1/songs/{id}/stream` to handle the media streaming.
- [x] **CORS Configuration**
  - Configure global cross-origin resource sharing (`WebMvcConfig`) to allow connection from external Flutter clients (Web & Mobile IPs).

### Phase 3: Auth & Security (Future Plan)
- [ ] **User Registration & Login**
- [ ] **JWT Authentication**
- [ ] **Access Control for Premium Songs**
