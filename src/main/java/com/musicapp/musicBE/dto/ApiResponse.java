package com.musicapp.musicBE.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

/**
 * Generic API response wrapper.
 * <p>
 * All endpoints return responses in the format:
 * <pre>
 * {
 *   "success": true,
 *   "data": { ... },
 *   "message": "Optional message"
 * }
 * </pre>
 *
 * @param <T> the type of the data payload
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final String message;

    // -------------------------------------------------------
    // Static factory helpers
    // -------------------------------------------------------

    /**
     * Returns a successful response with a data payload.
     */
    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    /**
     * Returns a successful response with a data payload and message.
     */
    public static <T> ApiResponse<T> ok(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .build();
    }

    /**
     * Returns a failure response with an error message.
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
}
