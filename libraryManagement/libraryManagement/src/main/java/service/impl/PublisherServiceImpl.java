package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.dto.PublisherDto;
import com.example.librarymanagement.entity.Publisher;
import com.example.librarymanagement.repository.PublisherRepository;
import com.example.librarymanagement.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;

    @Override
    public PublisherDto createPublisher(PublisherDto dto) {
        Publisher publisher = Publisher.builder().name(dto.getName()).build();
        return mapToDto(publisherRepository.save(publisher));
    }

    @Override
    public PublisherDto getPublisherById(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher not found"));
        return mapToDto(publisher);
    }

    @Override
    public List<PublisherDto> getAllPublishers() {
        return publisherRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public PublisherDto updatePublisher(Long id, PublisherDto dto) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher not found"));
        publisher.setName(dto.getName());
        return mapToDto(publisherRepository.save(publisher));
    }

    @Override
    public void deletePublisher(Long id) {
        publisherRepository.deleteById(id);
    }

    private PublisherDto mapToDto(Publisher publisher) {
        return PublisherDto.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .build();
    }
}
