package ge.library;

import ge.library.dto.AuthorRequestDto;
import ge.library.dto.AuthorResponseDto;
import ge.library.entity.Author;
import ge.library.exception.BusinessException;
import ge.library.exception.ResourceNotFoundException;
import ge.library.repository.AuthorRepository;
import ge.library.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private Author sampleAuthor;

    @BeforeEach
    void setUp() {
        sampleAuthor = Author.builder()
                .id(1L)
                .firstName("ილია")
                .lastName("ჭავჭავაძე")
                .biography("XIX საუკუნის მწერალი")
                .build();
    }

    @Test
    void getAllAuthors_returnsAllAuthors() {
        when(authorRepository.findAll()).thenReturn(List.of(sampleAuthor));

        List<AuthorResponseDto> result = authorService.getAllAuthors();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("ილია");
    }

    @Test
    void getAuthorById_existingId_returnsAuthor() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(sampleAuthor));

        AuthorResponseDto result = authorService.getAuthorById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getLastName()).isEqualTo("ჭავჭავაძე");
    }

    @Test
    void getAuthorById_nonExistingId_throwsNotFoundException() {
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.getAuthorById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void createAuthor_newAuthor_savesAndReturns() {
        AuthorRequestDto dto = new AuthorRequestDto("გიორგი", "ლეონიძე", "პოეტი");

        when(authorRepository.existsByFirstNameAndLastName("გიორგი", "ლეონიძე")).thenReturn(false);
        when(authorRepository.save(any())).thenReturn(
                Author.builder().id(2L).firstName("გიორგი").lastName("ლეონიძე").build()
        );

        AuthorResponseDto result = authorService.createAuthor(dto);

        assertThat(result.getId()).isEqualTo(2L);
        verify(authorRepository, times(1)).save(any());
    }

    @Test
    void createAuthor_duplicateName_throwsBusinessException() {
        AuthorRequestDto dto = new AuthorRequestDto("ილია", "ჭავჭავაძე", null);

        when(authorRepository.existsByFirstNameAndLastName("ილია", "ჭავჭავაძე")).thenReturn(true);

        assertThatThrownBy(() -> authorService.createAuthor(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("უკვე არსებობს");

        verify(authorRepository, never()).save(any());
    }

    @Test
    void deleteAuthor_withBooks_throwsBusinessException() {
        sampleAuthor.getBooks().add(
                ge.library.entity.Book.builder().title("ტესტი").build()
        );
        when(authorRepository.findById(1L)).thenReturn(Optional.of(sampleAuthor));

        assertThatThrownBy(() -> authorService.deleteAuthor(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("წიგნი");

        verify(authorRepository, never()).delete(any());
    }
}
