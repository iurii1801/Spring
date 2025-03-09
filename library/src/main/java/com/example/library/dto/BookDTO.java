package com.example.library.dto;

import lombok.Data;
import java.util.List;

@Data
public class BookDTO {
    private Long id;
    private String title;
    private Long authorId;
    private Long publisherId;
    private List<Long> categoryIds;
}