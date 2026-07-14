package com.nanakusa.zanshin.entity;

public enum SecurityEventType {
    USER_CREATION_SUCCESSFULLY,
    USER_CREATION_FAILED,
    LOGIN_SUCCESS,
    LOGIN_FAILED,
    LOGOUT,
    LOGOUT_FAILED,
    REFRESH_TOKEN_USED,
    REFRESH_TOKEN_REUSE_DETECTED
}
