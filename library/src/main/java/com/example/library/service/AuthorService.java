package com.example.library.service;

import com.example.library.dto.AuthorDTO;
import com.example.library.entity.Author;
import com.example.library.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AuthorDTO getAuthorById(Long id) {
        return authorRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setName(authorDTO.getName());
        Author savedAuthor = authorRepository.save(author);
        return convertToDTO(savedAuthor);
    }

    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        return authorRepository.findById(id)
                .map(author -> {
                    author.setName(authorDTO.getName());
                    Author updatedAuthor = authorRepository.save(author);
                    return convertToDTO(updatedAuthor);
                })
                .orElse(null);
    }

    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }

    private AuthorDTO convertToDTO(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setName(author.getName());
        return authorDTO;
    }
}