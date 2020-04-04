package com.teamalpha.bloodpals.logic.session;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SessionResource<T> {

    @NonNull
    public final SessionStatus status;

    @Nullable
    public T data;

    public SessionResource(@NonNull SessionStatus status, @Nullable T data) {
        this.status = status;
        this.data = data;
    }

    public void setData(@Nullable T data) {
        this.data = data;
    }

    public static <T> SessionResource<T> loading(@Nullable T data) {
        return new SessionResource<>(SessionStatus.LOADING, data);
    }

    public static <T> SessionResource<T> authenticate (@Nullable T data) {
        return new SessionResource<>(SessionStatus.AUTHENTICATED, data);
    }

    public static <T> SessionResource<T> logout () {
        return new SessionResource<>(SessionStatus.NOT_AUTHENTICATED, null);
    }

    public enum SessionStatus { LOADING, AUTHENTICATED, NOT_AUTHENTICATED}

}
