package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsyncResult<T> {

    private int index;
    private T result;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

}
