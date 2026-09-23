-- ============================================================
-- DATABASE: personalized_music_app
-- Project: Ứng dụng nghe nhạc cá nhân hóa người dùng
-- Compatible: MySQL / MariaDB on XAMPP
-- Charset: utf8mb4
-- ============================================================

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";
SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS personalized_music_app;
CREATE DATABASE personalized_music_app
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE personalized_music_app;

-- ============================================================
-- 1. AUTHENTICATION & USER MANAGEMENT
-- ============================================================

CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(150) NOT NULL UNIQUE,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(150),
    phone VARCHAR(30),
    avatar_url TEXT,
    status ENUM('ACTIVE', 'INACTIVE', 'BANNED', 'PENDING_VERIFICATION') NOT NULL DEFAULT 'ACTIVE',
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    last_login_at DATETIME NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at DATETIME NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_users_email (email),
    INDEX idx_users_username (username),
    INDEX idx_users_status (status),
    INDEX idx_users_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    assigned_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id) REFERENCES roles(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE user_profiles (
    user_id BIGINT PRIMARY KEY,
    bio TEXT,
    gender ENUM('MALE', 'FEMALE', 'OTHER', 'UNKNOWN') NOT NULL DEFAULT 'UNKNOWN',
    date_of_birth DATE NULL,
    country VARCHAR(100),
    preferred_language VARCHAR(30) DEFAULT 'vi',
    theme_preference ENUM('LIGHT', 'DARK', 'SYSTEM') NOT NULL DEFAULT 'SYSTEM',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_profiles_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE refresh_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token_hash VARCHAR(255) NOT NULL UNIQUE,
    device_id VARCHAR(150),
    ip_address VARCHAR(100),
    user_agent TEXT,
    expires_at DATETIME NOT NULL,
    revoked_at DATETIME NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_refresh_tokens_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_refresh_tokens_user (user_id),
    INDEX idx_refresh_tokens_expires_at (expires_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE user_devices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    device_uid VARCHAR(150) NOT NULL,
    device_name VARCHAR(150),
    platform ENUM('ANDROID', 'IOS', 'WEB', 'DESKTOP', 'UNKNOWN') NOT NULL DEFAULT 'UNKNOWN',
    fcm_token TEXT,
    last_active_at DATETIME NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uq_user_device (user_id, device_uid),
    CONSTRAINT fk_user_devices_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_user_devices_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE vip_subscriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    plan_name VARCHAR(100) NOT NULL,
    status ENUM('ACTIVE', 'EXPIRED', 'CANCELLED', 'PENDING') NOT NULL DEFAULT 'PENDING',
    started_at DATETIME NOT NULL,
    expired_at DATETIME NOT NULL,
    auto_renew BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_vip_subscriptions_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_vip_user_status (user_id, status),
    INDEX idx_vip_expired_at (expired_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- 2. MUSIC CATALOG
-- ============================================================

CREATE TABLE artists (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    slug VARCHAR(180) NOT NULL UNIQUE,
    biography TEXT,
    avatar_url TEXT,
    country VARCHAR(100),
    status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at DATETIME NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_artists_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_artists_updated_by FOREIGN KEY (updated_by) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_artists_name (name),
    INDEX idx_artists_status (status),
    INDEX idx_artists_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE genres (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    slug VARCHAR(120) NOT NULL UNIQUE,
    description TEXT,
    cover_image_url TEXT,
    status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at DATETIME NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_genres_name (name),
    INDEX idx_genres_status (status),
    INDEX idx_genres_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE albums (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    slug VARCHAR(230) NOT NULL UNIQUE,
    primary_artist_id BIGINT NULL,
    description TEXT,
    cover_image_url TEXT,
    release_date DATE NULL,
    status ENUM('DRAFT', 'PUBLISHED', 'HIDDEN') NOT NULL DEFAULT 'DRAFT',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at DATETIME NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_albums_primary_artist
        FOREIGN KEY (primary_artist_id) REFERENCES artists(id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_albums_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_albums_updated_by FOREIGN KEY (updated_by) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_albums_title (title),
    INDEX idx_albums_artist (primary_artist_id),
    INDEX idx_albums_status (status),
    INDEX idx_albums_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE songs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    slug VARCHAR(230) NOT NULL UNIQUE,
    album_id BIGINT NULL,
    duration_seconds INT NOT NULL DEFAULT 0,
    audio_url TEXT NOT NULL,
    cover_image_url TEXT,
    file_size_bytes BIGINT NULL,
    mime_type VARCHAR(100) DEFAULT 'audio/mpeg',
    bitrate INT NULL,
    release_date DATE NULL,
    play_count BIGINT NOT NULL DEFAULT 0,
    like_count BIGINT NOT NULL DEFAULT 0,
    status ENUM('DRAFT', 'PUBLISHED', 'HIDDEN', 'BLOCKED') NOT NULL DEFAULT 'DRAFT',
    is_vip_only BOOLEAN NOT NULL DEFAULT FALSE,
    is_downloadable BOOLEAN NOT NULL DEFAULT FALSE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at DATETIME NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_songs_album
        FOREIGN KEY (album_id) REFERENCES albums(id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_songs_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_songs_updated_by FOREIGN KEY (updated_by) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_songs_title (title),
    INDEX idx_songs_album (album_id),
    INDEX idx_songs_status (status),
    INDEX idx_songs_play_count (play_count),
    INDEX idx_songs_created_at (created_at),
    INDEX idx_songs_is_deleted (is_deleted),
    FULLTEXT KEY ft_songs_title (title)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE song_artists (
    song_id BIGINT NOT NULL,
    artist_id BIGINT NOT NULL,
    role ENUM('MAIN', 'FEATURED', 'COMPOSER', 'PRODUCER') NOT NULL DEFAULT 'MAIN',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (song_id, artist_id, role),
    CONSTRAINT fk_song_artists_song
        FOREIGN KEY (song_id) REFERENCES songs(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_song_artists_artist
        FOREIGN KEY (artist_id) REFERENCES artists(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_song_artists_artist (artist_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE song_genres (
    song_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (song_id, genre_id),
    CONSTRAINT fk_song_genres_song
        FOREIGN KEY (song_id) REFERENCES songs(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_song_genres_genre
        FOREIGN KEY (genre_id) REFERENCES genres(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_song_genres_genre (genre_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE lyrics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    song_id BIGINT NOT NULL UNIQUE,
    content LONGTEXT,
    synced_lyrics LONGTEXT,
    language VARCHAR(30) DEFAULT 'vi',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_lyrics_song
        FOREIGN KEY (song_id) REFERENCES songs(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- 3. PLAYLIST
-- ============================================================

CREATE TABLE playlists (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NULL,
    title VARCHAR(200) NOT NULL,
    slug VARCHAR(230),
    description TEXT,
    cover_image_url TEXT,
    visibility ENUM('PUBLIC', 'PRIVATE', 'UNLISTED') NOT NULL DEFAULT 'PRIVATE',
    playlist_type ENUM('USER_CREATED', 'SYSTEM', 'RECOMMENDED') NOT NULL DEFAULT 'USER_CREATED',
    total_songs INT NOT NULL DEFAULT 0,
    total_duration_seconds BIGINT NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at DATETIME NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_playlists_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_playlists_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_playlists_updated_by FOREIGN KEY (updated_by) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_playlists_user (user_id),
    INDEX idx_playlists_visibility (visibility),
    INDEX idx_playlists_type (playlist_type),
    INDEX idx_playlists_is_deleted (is_deleted),
    INDEX idx_playlists_slug (slug)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE playlist_songs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    playlist_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    position INT NOT NULL DEFAULT 0,
    added_by BIGINT NULL,
    added_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_playlist_song (playlist_id, song_id),
    CONSTRAINT fk_playlist_songs_playlist
        FOREIGN KEY (playlist_id) REFERENCES playlists(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_playlist_songs_song
        FOREIGN KEY (song_id) REFERENCES songs(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_playlist_songs_added_by
        FOREIGN KEY (added_by) REFERENCES users(id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_playlist_songs_playlist (playlist_id),
    INDEX idx_playlist_songs_song (song_id),
    INDEX idx_playlist_songs_position (playlist_id, position)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- 4. USER INTERACTIONS
-- ============================================================

CREATE TABLE favorites (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    target_type ENUM('SONG', 'ALBUM', 'ARTIST', 'PLAYLIST') NOT NULL,
    target_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_favorite_user_target (user_id, target_type, target_id),
    CONSTRAINT fk_favorites_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_favorites_user (user_id),
    INDEX idx_favorites_target (target_type, target_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE listening_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    device_id BIGINT NULL,
    source_type ENUM('SEARCH', 'PLAYLIST', 'RECOMMENDATION', 'ALBUM', 'ARTIST', 'HOME', 'UNKNOWN') NOT NULL DEFAULT 'UNKNOWN',
    source_id BIGINT NULL,
    listened_seconds INT NOT NULL DEFAULT 0,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    listened_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_listening_history_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_listening_history_song
        FOREIGN KEY (song_id) REFERENCES songs(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_listening_history_device
        FOREIGN KEY (device_id) REFERENCES user_devices(id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_history_user_time (user_id, listened_at),
    INDEX idx_history_song_time (song_id, listened_at),
    INDEX idx_history_source (source_type, source_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE search_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NULL,
    keyword VARCHAR(255) NOT NULL,
    result_count INT NOT NULL DEFAULT 0,
    searched_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_search_history_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_search_user_time (user_id, searched_at),
    INDEX idx_search_keyword (keyword)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    parent_id BIGINT NULL,
    content TEXT NOT NULL,
    status ENUM('VISIBLE', 'HIDDEN', 'DELETED') NOT NULL DEFAULT 'VISIBLE',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_comments_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_comments_song
        FOREIGN KEY (song_id) REFERENCES songs(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_comments_parent
        FOREIGN KEY (parent_id) REFERENCES comments(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_comments_song (song_id),
    INDEX idx_comments_user (user_id),
    INDEX idx_comments_parent (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reporter_id BIGINT NOT NULL,
    target_type ENUM('SONG', 'COMMENT', 'PLAYLIST', 'USER') NOT NULL,
    target_id BIGINT NOT NULL,
    reason VARCHAR(255) NOT NULL,
    detail TEXT,
    status ENUM('PENDING', 'REVIEWED', 'RESOLVED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    handled_by BIGINT NULL,
    handled_at DATETIME NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reports_reporter
        FOREIGN KEY (reporter_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_reports_handled_by
        FOREIGN KEY (handled_by) REFERENCES users(id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_reports_target (target_type, target_id),
    INDEX idx_reports_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- 5. RECOMMENDATION
-- ============================================================

CREATE TABLE user_music_preferences (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    genre_id BIGINT NULL,
    artist_id BIGINT NULL,
    score DECIMAL(10,4) NOT NULL DEFAULT 0,
    last_calculated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_user_pref_genre_artist (user_id, genre_id, artist_id),
    CONSTRAINT fk_user_music_preferences_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_user_music_preferences_genre
        FOREIGN KEY (genre_id) REFERENCES genres(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_user_music_preferences_artist
        FOREIGN KEY (artist_id) REFERENCES artists(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_user_pref_user_score (user_id, score),
    INDEX idx_user_pref_genre (genre_id),
    INDEX idx_user_pref_artist (artist_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE recommendation_scores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    score DECIMAL(10,4) NOT NULL DEFAULT 0,
    reason ENUM('GENRE_MATCH', 'ARTIST_MATCH', 'HISTORY', 'FAVORITE', 'TRENDING', 'SIMILAR_SONG', 'HYBRID') NOT NULL DEFAULT 'HYBRID',
    generated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at DATETIME NULL,
    UNIQUE KEY uq_recommendation_user_song (user_id, song_id),
    CONSTRAINT fk_recommendation_scores_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_recommendation_scores_song
        FOREIGN KEY (song_id) REFERENCES songs(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_recommendation_user_score (user_id, score),
    INDEX idx_recommendation_song (song_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE song_similarity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    source_song_id BIGINT NOT NULL,
    similar_song_id BIGINT NOT NULL,
    similarity_score DECIMAL(10,4) NOT NULL DEFAULT 0,
    algorithm VARCHAR(100) NOT NULL DEFAULT 'RULE_BASED',
    calculated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_song_similarity (source_song_id, similar_song_id),
    CONSTRAINT fk_song_similarity_source
        FOREIGN KEY (source_song_id) REFERENCES songs(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_song_similarity_similar
        FOREIGN KEY (similar_song_id) REFERENCES songs(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_song_similarity_source_score (source_song_id, similarity_score),
    INDEX idx_song_similarity_similar (similar_song_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE recommendation_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    action ENUM('SHOWN', 'CLICKED', 'PLAYED', 'SKIPPED', 'LIKED') NOT NULL,
    context VARCHAR(100),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_recommendation_logs_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_recommendation_logs_song
        FOREIGN KEY (song_id) REFERENCES songs(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_recommendation_logs_user_time (user_id, created_at),
    INDEX idx_recommendation_logs_song_action (song_id, action)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- 6. NOTIFICATIONS
-- ============================================================

CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    type ENUM('SYSTEM', 'NEW_SONG', 'PLAYLIST', 'VIP', 'RECOMMENDATION') NOT NULL DEFAULT 'SYSTEM',
    target_type ENUM('NONE', 'SONG', 'PLAYLIST', 'ALBUM', 'ARTIST') NOT NULL DEFAULT 'NONE',
    target_id BIGINT NULL,
    created_by BIGINT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notifications_created_by
        FOREIGN KEY (created_by) REFERENCES users(id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_notifications_type (type),
    INDEX idx_notifications_target (target_type, target_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE user_notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    notification_id BIGINT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    read_at DATETIME NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_user_notification (user_id, notification_id),
    CONSTRAINT fk_user_notifications_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_user_notifications_notification
        FOREIGN KEY (notification_id) REFERENCES notifications(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_user_notifications_user_read (user_id, is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- 7. ANALYTICS & ADMIN
-- ============================================================

CREATE TABLE song_play_stats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    song_id BIGINT NOT NULL,
    stat_date DATE NOT NULL,
    play_count BIGINT NOT NULL DEFAULT 0,
    unique_listener_count BIGINT NOT NULL DEFAULT 0,
    total_listened_seconds BIGINT NOT NULL DEFAULT 0,
    UNIQUE KEY uq_song_play_stats_date (song_id, stat_date),
    CONSTRAINT fk_song_play_stats_song
        FOREIGN KEY (song_id) REFERENCES songs(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_song_play_stats_date (stat_date),
    INDEX idx_song_play_stats_play_count (play_count)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE artist_play_stats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    artist_id BIGINT NOT NULL,
    stat_date DATE NOT NULL,
    play_count BIGINT NOT NULL DEFAULT 0,
    unique_listener_count BIGINT NOT NULL DEFAULT 0,
    UNIQUE KEY uq_artist_play_stats_date (artist_id, stat_date),
    CONSTRAINT fk_artist_play_stats_artist
        FOREIGN KEY (artist_id) REFERENCES artists(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_artist_play_stats_date (stat_date),
    INDEX idx_artist_play_stats_play_count (play_count)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE genre_play_stats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    genre_id BIGINT NOT NULL,
    stat_date DATE NOT NULL,
    play_count BIGINT NOT NULL DEFAULT 0,
    unique_listener_count BIGINT NOT NULL DEFAULT 0,
    UNIQUE KEY uq_genre_play_stats_date (genre_id, stat_date),
    CONSTRAINT fk_genre_play_stats_genre
        FOREIGN KEY (genre_id) REFERENCES genres(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_genre_play_stats_date (stat_date),
    INDEX idx_genre_play_stats_play_count (play_count)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE user_activity_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NULL,
    action VARCHAR(100) NOT NULL,
    target_type VARCHAR(50),
    target_id BIGINT,
    ip_address VARCHAR(100),
    user_agent TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_activity_logs_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_user_activity_user_time (user_id, created_at),
    INDEX idx_user_activity_action (action)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE admin_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    admin_id BIGINT NULL,
    action VARCHAR(100) NOT NULL,
    target_table VARCHAR(100),
    target_id BIGINT,
    old_value JSON NULL,
    new_value JSON NULL,
    ip_address VARCHAR(100),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_admin_logs_admin
        FOREIGN KEY (admin_id) REFERENCES users(id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_admin_logs_admin_time (admin_id, created_at),
    INDEX idx_admin_logs_target (target_table, target_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE system_settings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    setting_key VARCHAR(150) NOT NULL UNIQUE,
    setting_value TEXT,
    description VARCHAR(255),
    updated_by BIGINT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_system_settings_updated_by
        FOREIGN KEY (updated_by) REFERENCES users(id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- 8. SEED DATA
-- Password hash below is placeholder. Replace in Spring Boot.
-- ============================================================

INSERT INTO roles (name, description) VALUES
('ROLE_USER', 'Người dùng nghe nhạc'),
('ROLE_ADMIN', 'Quản trị viên hệ thống'),
('ROLE_VIP', 'Người dùng thành viên VIP');

INSERT INTO users (email, username, password_hash, full_name, status, email_verified)
VALUES
('admin@musicapp.com', 'admin', '$2a$10$replace_with_real_bcrypt_hash', 'System Admin', 'ACTIVE', TRUE),
('user@musicapp.com', 'demo_user', '$2a$10$replace_with_real_bcrypt_hash', 'Demo User', 'ACTIVE', TRUE);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ROLE_USER';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'demo_user' AND r.name = 'ROLE_USER';

INSERT INTO genres (name, slug, description) VALUES
('Pop', 'pop', 'Nhạc Pop'),
('Ballad', 'ballad', 'Nhạc Ballad'),
('EDM', 'edm', 'Nhạc điện tử'),
('Lo-fi', 'lo-fi', 'Nhạc thư giãn, tập trung'),
('Rap', 'rap', 'Nhạc Rap/Hip-hop');

INSERT INTO artists (name, slug, biography, country, status) VALUES
('Demo Artist', 'demo-artist', 'Nghệ sĩ mẫu dùng cho dữ liệu demo', 'Vietnam', 'ACTIVE'),
('Chill Producer', 'chill-producer', 'Nhà sản xuất nhạc chill mẫu', 'Vietnam', 'ACTIVE');

INSERT INTO albums (title, slug, primary_artist_id, description, status)
VALUES
('Demo Album', 'demo-album', 1, 'Album mẫu cho hệ thống', 'PUBLISHED');

INSERT INTO songs (
    title, slug, album_id, duration_seconds, audio_url, cover_image_url,
    status, is_vip_only, is_downloadable
) VALUES
('Demo Song 1', 'demo-song-1', 1, 210, '/storage/audio/demo-song-1.mp3', '/storage/covers/demo-song-1.jpg', 'PUBLISHED', FALSE, TRUE),
('Demo Song 2', 'demo-song-2', 1, 195, '/storage/audio/demo-song-2.mp3', '/storage/covers/demo-song-2.jpg', 'PUBLISHED', FALSE, TRUE),
('VIP Chill Track', 'vip-chill-track', 1, 240, '/storage/audio/vip-chill-track.mp3', '/storage/covers/vip-chill-track.jpg', 'PUBLISHED', TRUE, TRUE);

INSERT INTO song_artists (song_id, artist_id, role) VALUES
(1, 1, 'MAIN'),
(2, 1, 'MAIN'),
(3, 2, 'MAIN');

INSERT INTO song_genres (song_id, genre_id) VALUES
(1, 1),
(1, 2),
(2, 1),
(3, 4);

INSERT INTO lyrics (song_id, content, language) VALUES
(1, 'Lời bài hát demo 1...', 'vi'),
(2, 'Lời bài hát demo 2...', 'vi'),
(3, 'Lời bài hát VIP Chill Track...', 'vi');

INSERT INTO playlists (user_id, title, slug, description, visibility, playlist_type, created_by)
VALUES
(2, 'Playlist yêu thích của tôi', 'playlist-yeu-thich-cua-toi', 'Playlist demo của user', 'PRIVATE', 'USER_CREATED', 2),
(NULL, 'Top Trending', 'top-trending', 'Playlist hệ thống các bài hát phổ biến', 'PUBLIC', 'SYSTEM', 1);

INSERT INTO playlist_songs (playlist_id, song_id, position, added_by) VALUES
(1, 1, 1, 2),
(1, 2, 2, 2),
(2, 1, 1, 1),
(2, 3, 2, 1);

INSERT INTO favorites (user_id, target_type, target_id) VALUES
(2, 'SONG', 1),
(2, 'ARTIST', 1),
(2, 'PLAYLIST', 2);

INSERT INTO system_settings (setting_key, setting_value, description)
VALUES
('APP_NAME', 'Personalized Music App', 'Tên ứng dụng'),
('RECOMMENDATION_MODE', 'RULE_BASED', 'Chế độ gợi ý hiện tại'),
('STORAGE_MODE', 'LOCAL', 'Chế độ lưu trữ file hiện tại');

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 9. SAMPLE QUERIES
-- ============================================================

-- Lấy danh sách bài hát đã publish:
-- SELECT * FROM songs WHERE status = 'PUBLISHED' AND is_deleted = FALSE ORDER BY created_at DESC;

-- Lấy bài hát theo thể loại:
-- SELECT s.*
-- FROM songs s
-- JOIN song_genres sg ON s.id = sg.song_id
-- JOIN genres g ON sg.genre_id = g.id
-- WHERE g.slug = 'pop' AND s.status = 'PUBLISHED' AND s.is_deleted = FALSE;

-- Lấy playlist của user:
-- SELECT * FROM playlists WHERE user_id = 2 AND is_deleted = FALSE ORDER BY created_at DESC;

-- Lấy bài hát trong playlist:
-- SELECT s.*, ps.position
-- FROM playlist_songs ps
-- JOIN songs s ON ps.song_id = s.id
-- WHERE ps.playlist_id = 1
-- ORDER BY ps.position ASC;

-- Lấy lịch sử nghe của user:
-- SELECT lh.*, s.title
-- FROM listening_history lh
-- JOIN songs s ON lh.song_id = s.id
-- WHERE lh.user_id = 2
-- ORDER BY lh.listened_at DESC;

-- Lấy top songs:
-- SELECT * FROM songs WHERE status = 'PUBLISHED' AND is_deleted = FALSE ORDER BY play_count DESC LIMIT 10;
