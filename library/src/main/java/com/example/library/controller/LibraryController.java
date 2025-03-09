package com.example.library.controller;

import com.example.library.dto.LibraryDTO;
import com.example.library.entity.Library;
import com.example.library.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/libraries")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;

    @GetMapping
    public List<LibraryDTO> getAllLibraries() {
        return libraryService.getAllLibraries();
    }

    @GetMapping("/{id}")
    public LibraryDTO getLibraryById(@PathVariable Long id) {
        return libraryService.getLibraryById(id);
    }

    @PostMapping
    public LibraryDTO createLibrary(@RequestBody LibraryDTO libraryDTO) {
        return libraryService.createLibrary(libraryDTO);
    }

    @PutMapping("/{id}")
    public LibraryDTO updateLibrary(@PathVariable Long id, @RequestBody LibraryDTO libraryDTO) {
        return libraryService.updateLibrary(id, libraryDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteLibrary(@PathVariable Long id) {
        libraryService.deleteLibrary(id);
    }
}
