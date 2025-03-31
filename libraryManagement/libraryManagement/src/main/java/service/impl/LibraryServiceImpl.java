package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.dto.LibraryDto;
import com.example.librarymanagement.entity.Book;
import com.example.librarymanagement.entity.Library;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.repository.LibraryRepository;
import com.example.librarymanagement.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    private final LibraryRepository libraryRepository;
    private final BookRepository bookRepository;

    @Override
    public LibraryDto createLibrary(LibraryDto dto) {
        Library library = Library.builder()
                .location(dto.getLocation())
                .books(bookRepository.findAllById(dto.getBookIds()))
                .build();
        return mapToDto(libraryRepository.save(library));
    }

    @Override
    public LibraryDto getLibraryById(Long id) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Library not found"));
        return mapToDto(library);
    }

    @Override
    public List<LibraryDto> getAllLibraries() {
        return libraryRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public LibraryDto updateLibrary(Long id, LibraryDto dto) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Library not found"));
        library.setLocation(dto.getLocation());
        library.setBooks(bookRepository.findAllById(dto.getBookIds()));
        return mapToDto(libraryRepository.save(library));
    }

    @Override
    public void deleteLibrary(Long id) {
        libraryRepository.deleteById(id);
    }

    private LibraryDto mapToDto(Library library) {
        return LibraryDto.builder()
                .id(library.getId())
                .location(library.getLocation())
                .bookIds(library.getBooks().stream().map(Book::getId).collect(Collectors.toList()))
                .build();
    }
}
