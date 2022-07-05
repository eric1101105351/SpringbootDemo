package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsyncResult {

    private int index;
    private Object result;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

}
