package com.sellbycar.marketplace.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class ResponseBody<T> {

    @Nullable
    private String message;
    @NotNull
    private String status;
    @Nullable
    private T data;
}
