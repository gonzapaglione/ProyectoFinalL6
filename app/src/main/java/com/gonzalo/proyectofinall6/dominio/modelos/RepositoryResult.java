package com.gonzalo.proyectofinall6.dominio.modelos;

import androidx.annotation.Nullable;

public final class RepositoryResult<T> {

    @Nullable
    private final T data;

    @Nullable
    private final String error;

    private RepositoryResult(@Nullable T data, @Nullable String error) {
        this.data = data;
        this.error = error;
    }

    public static <T> RepositoryResult<T> success(@Nullable T data) {
        return new RepositoryResult<>(data, null);
    }

    public static <T> RepositoryResult<T> error(String error) {
        return new RepositoryResult<>(null, error);
    }

    public boolean isSuccess() {
        return error == null;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    public String getError() {
        return error;
    }
}
