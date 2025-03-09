package com.example.library.service;

import com.example.library.dto.PublisherDTO;
import com.example.library.entity.Publisher;
import com.example.library.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherService {

    @Autowired
    private PublisherRepository publisherRepository;

    public List<PublisherDTO> getAllPublishers() {
        return publisherRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PublisherDTO getPublisherById(Long id) {
        return publisherRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public PublisherDTO createPublisher(PublisherDTO publisherDTO) {
        Publisher publisher = new Publisher();
        publisher.setName(publisherDTO.getName());
        Publisher savedPublisher = publisherRepository.save(publisher);
        return convertToDTO(savedPublisher);
    }

    public PublisherDTO updatePublisher(Long id, PublisherDTO publisherDTO) {
        return publisherRepository.findById(id)
                .map(publisher -> {
                    publisher.setName(publisherDTO.getName());
                    Publisher updatedPublisher = publisherRepository.save(publisher);
                    return convertToDTO(updatedPublisher);
                })
                .orElse(null);
    }

    public void deletePublisher(Long id) {
        publisherRepository.deleteById(id);
    }

    private PublisherDTO convertToDTO(Publisher publisher) {
        PublisherDTO publisherDTO = new PublisherDTO();
        publisherDTO.setId(publisher.getId());
        publisherDTO.setName(publisher.getName());
        return publisherDTO;
    }
}