package com.example.library.dto;

import lombok.Data;
import java.util.List;

@Data
public class LibraryDTO {
    private Long id;
    private List<Long> bookIds;
}