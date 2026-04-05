package ge.library.service;

import ge.library.dto.BookRequestDto;
import ge.library.dto.BookResponseDto;
import ge.library.entity.Author;
import ge.library.entity.Book;
import ge.library.exception.BusinessException;
import ge.library.exception.ResourceNotFoundException;
import ge.library.mapper.BookMapper;
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
    private final BookMapper bookMapper;

    public List<BookResponseDto> getAllBooks() {
        return bookRepository.findAll().stream().map(bookMapper::toDto).toList();
    }

    public BookResponseDto getBookById(Long id) {
        return bookMapper.toDto(findBookOrThrow(id));
    }

    public BookResponseDto getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("წიგნი ISBN='" + isbn + "'-ით ვერ მოიძებნა"));
        return bookMapper.toDto(book);
    }

    public List<BookResponseDto> getBooksByAuthor(Long authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new ResourceNotFoundException("ავტორი", authorId);
        }
        return bookRepository.findByAuthorId(authorId).stream().map(bookMapper::toDto).toList();
    }

    public List<BookResponseDto> getAvailableBooks() {
        return bookRepository.findByAvailable(true).stream().map(bookMapper::toDto).toList();
    }

    public List<BookResponseDto> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream().map(bookMapper::toDto).toList();
    }

    public List<BookResponseDto> getBooksByGenre(String genre) {
        return bookRepository.findByGenreIgnoreCase(genre).stream().map(bookMapper::toDto).toList();
    }

    @Transactional
    public BookResponseDto createBook(BookRequestDto dto) {
        if (bookRepository.existsByIsbn(dto.getIsbn())) {
            throw new BusinessException("ISBN '" + dto.getIsbn() + "' უკვე გამოყენებულია");
        }

        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("ავტორი", dto.getAuthorId()));

        Book book = bookMapper.toEntity(dto);
        book.setAuthor(author);
        book.setAvailable(true);

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Transactional
    public BookResponseDto updateBook(Long id, BookRequestDto dto) {
        Book book = findBookOrThrow(id);

        if (!book.getIsbn().equals(dto.getIsbn()) && bookRepository.existsByIsbn(dto.getIsbn())) {
            throw new BusinessException("ISBN '" + dto.getIsbn() + "' უკვე გამოყენებულია");
        }

        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("ავტორი", dto.getAuthorId()));

        bookMapper.updateEntityFromDto(dto, book);
        book.setAuthor(author);

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = findBookOrThrow(id);
        if (!book.getAvailable()) {
            throw new BusinessException("გაცემული წიგნის წაშლა შეუძლებელია — ჯერ დაბრუნება საჭიროა");
        }
        bookRepository.delete(book);
    }

    // package-private — used by BorrowService
    Book findBookOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("წიგნი", id));
    }
}
