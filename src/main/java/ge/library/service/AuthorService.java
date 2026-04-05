package ge.library.service;

import ge.library.dto.AuthorRequestDto;
import ge.library.dto.AuthorResponseDto;
import ge.library.entity.Author;
import ge.library.exception.BusinessException;
import ge.library.exception.ResourceNotFoundException;
import ge.library.mapper.AuthorMapper;
import ge.library.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public List<AuthorResponseDto> getAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toDto)
                .toList();
    }

    public AuthorResponseDto getAuthorById(Long id) {
        return authorMapper.toDto(findAuthorOrThrow(id));
    }

    public List<AuthorResponseDto> searchAuthors(String query) {
        return authorRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query)
                .stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @Transactional
    public AuthorResponseDto createAuthor(AuthorRequestDto dto) {
        if (authorRepository.existsByFirstNameAndLastName(dto.getFirstName(), dto.getLastName())) {
            throw new BusinessException(
                    "ავტორი სახელით '" + dto.getFirstName() + " " + dto.getLastName() + "' უკვე არსებობს"
            );
        }
        Author author = authorMapper.toEntity(dto);
        return authorMapper.toDto(authorRepository.save(author));
    }

    @Transactional
    public AuthorResponseDto updateAuthor(Long id, AuthorRequestDto dto) {
        Author author = findAuthorOrThrow(id);
        authorMapper.updateEntityFromDto(dto, author);
        return authorMapper.toDto(authorRepository.save(author));
    }

    @Transactional
    public void deleteAuthor(Long id) {
        Author author = findAuthorOrThrow(id);
        if (!author.getBooks().isEmpty()) {
            throw new BusinessException(
                    "ავტორის წაშლა შეუძლებელია — მას " + author.getBooks().size() + " წიგნი აქვს რეგისტრირებული"
            );
        }
        authorRepository.delete(author);
    }

    Author findAuthorOrThrow(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ავტორი", id));
    }
}
