package ge.library.service;

import ge.library.dto.BookRequestDto;
import ge.library.dto.BookResponseDto;
import ge.library.entity.Author;
import ge.library.entity.Book;
import ge.library.exception.BusinessException;
import ge.library.exception.ResourceNotFoundException;
import ge.library.repository.AuthorRepository;
import ge.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public List<BookResponseDto> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public BookResponseDto getBookById(Long id) {
        return toResponseDto(findBookOrThrow(id));
    }

    public BookResponseDto getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("წიგნი ISBN='" + isbn + "'-ით ვერ მოიძებნა"));
        return toResponseDto(book);
    }

    public List<BookResponseDto> getBooksByAuthor(Long authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new ResourceNotFoundException("ავტორი", authorId);
        }
        return bookRepository.findByAuthorId(authorId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<BookResponseDto> getAvailableBooks() {
        return bookRepository.findByAvailable(true)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<BookResponseDto> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<BookResponseDto> getBooksByGenre(String genre) {
        return bookRepository.findByGenreIgnoreCase(genre)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Transactional
    public BookResponseDto createBook(BookRequestDto dto) {
        if (bookRepository.existsByIsbn(dto.getIsbn())) {
            throw new BusinessException("ISBN '" + dto.getIsbn() + "' უკვე გამოყენებულია");
        }

        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("ავტორი", dto.getAuthorId()));

        Book book = Book.builder()
                .title(dto.getTitle())
                .isbn(dto.getIsbn())
                .publishedYear(dto.getPublishedYear())
                .genre(dto.getGenre())
                .available(true)
                .author(author)
                .build();

        return toResponseDto(bookRepository.save(book));
    }

    @Transactional
    public BookResponseDto updateBook(Long id, BookRequestDto dto) {
        Book book = findBookOrThrow(id);

        // Allow ISBN change only if new ISBN is not taken by another book
        if (!book.getIsbn().equals(dto.getIsbn()) && bookRepository.existsByIsbn(dto.getIsbn())) {
            throw new BusinessException("ISBN '" + dto.getIsbn() + "' უკვე გამოყენებულია");
        }

        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("ავტორი", dto.getAuthorId()));

        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPublishedYear(dto.getPublishedYear());
        book.setGenre(dto.getGenre());
        book.setAuthor(author);

        return toResponseDto(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = findBookOrThrow(id);

        if (!book.getAvailable()) {
            throw new BusinessException("გაცემული წიგნის წაშლა შეუძლებელია — ჯერ დაბრუნება საჭიროა");
        }

        bookRepository.delete(book);
    }

    // --- helpers ---

    Book findBookOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("წიგნი", id));
    }

    private BookResponseDto toResponseDto(Book book) {
        return BookResponseDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .publishedYear(book.getPublishedYear())
                .genre(book.getGenre())
                .available(book.getAvailable())
                .authorFullName(book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName())
                .authorId(book.getAuthor().getId())
                .build();
    }
}
