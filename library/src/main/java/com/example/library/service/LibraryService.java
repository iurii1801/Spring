package com.example.library.service;

import com.example.library.dto.LibraryDTO;
import com.example.library.entity.Library;
import com.example.library.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryService {

    @Autowired
    private LibraryRepository libraryRepository;

    public List<LibraryDTO> getAllLibraries() {
        return libraryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LibraryDTO getLibraryById(Long id) {
        return libraryRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public LibraryDTO createLibrary(LibraryDTO libraryDTO) {
        Library library = new Library();
        library.setBookIds(libraryDTO.getBookIds());
        Library savedLibrary = libraryRepository.save(library);
        return convertToDTO(savedLibrary);
    }

    public LibraryDTO updateLibrary(Long id, LibraryDTO libraryDTO) {
        return libraryRepository.findById(id)
                .map(library -> {
                    library.setBookIds(libraryDTO.getBookIds());
                    Library updatedLibrary = libraryRepository.save(library);
                    return convertToDTO(updatedLibrary);
                })
                .orElse(null);
    }

    public void deleteLibrary(Long id) {
        libraryRepository.deleteById(id);
    }

    private LibraryDTO convertToDTO(Library library) {
        LibraryDTO libraryDTO = new LibraryDTO();
        libraryDTO.setId(library.getId());
        libraryDTO.setBookIds(library.getBookIds());
        return libraryDTO;
    }
}