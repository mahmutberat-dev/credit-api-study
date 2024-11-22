package com.mbi.study.controller.dto;

import java.util.Date;

public record APIErrorResponse(String message, String errorCode, Date timestamp) {
}
