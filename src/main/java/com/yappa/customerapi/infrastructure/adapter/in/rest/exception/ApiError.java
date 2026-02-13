package com.yappa.customerapi.infrastructure.adapter.in.rest.exception;

import java.time.OffsetDateTime;
import java.util.Map;

public record ApiError(
        OffsetDateTime timestamp,
        int status,
        String message,
        Map<String, Object> details
) {}
