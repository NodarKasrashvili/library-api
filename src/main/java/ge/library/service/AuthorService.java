package ge.library.service;

import ge.library.dto.AuthorRequestDto;
import ge.library.dto.AuthorResponseDto;
import ge.library.entity.Author;
import ge.library.exception.BusinessException;
import ge.library.exception.ResourceNotFoundException;
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

    public List<AuthorResponseDto> getAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public AuthorResponseDto getAuthorById(Long id) {
        Author author = findAuthorOrThrow(id);
        return toResponseDto(author);
    }

    public List<AuthorResponseDto> searchAuthors(String query) {
        return authorRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Transactional
    public AuthorResponseDto createAuthor(AuthorRequestDto dto) {
        if (authorRepository.existsByFirstNameAndLastName(dto.getFirstName(), dto.getLastName())) {
            throw new BusinessException(
                    "ავტორი სახელით '" + dto.getFirstName() + " " + dto.getLastName() + "' უკვე არსებობს"
            );
        }

        Author author = Author.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .biography(dto.getBiography())
                .build();

        return toResponseDto(authorRepository.save(author));
    }

    @Transactional
    public AuthorResponseDto updateAuthor(Long id, AuthorRequestDto dto) {
        Author author = findAuthorOrThrow(id);

        author.setFirstName(dto.getFirstName());
        author.setLastName(dto.getLastName());
        author.setBiography(dto.getBiography());

        return toResponseDto(authorRepository.save(author));
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

    // --- helpers ---

    private Author findAuthorOrThrow(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ავტორი", id));
    }

    private AuthorResponseDto toResponseDto(Author author) {
        return AuthorResponseDto.builder()
                .id(author.getId())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .biography(author.getBiography())
                .bookCount(author.getBooks().size())
                .build();
    }
}
