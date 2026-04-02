package ge.library.controller;

import ge.library.dto.BookRequestDto;
import ge.library.dto.BookResponseDto;
import ge.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // GET /api/books  (+ ?title=... / ?genre=... / ?available=true filters)
    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getAllBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Boolean available
    ) {
        if (title != null && !title.isBlank()) {
            return ResponseEntity.ok(bookService.searchByTitle(title));
        }
        if (genre != null && !genre.isBlank()) {
            return ResponseEntity.ok(bookService.getBooksByGenre(genre));
        }
        if (Boolean.TRUE.equals(available)) {
            return ResponseEntity.ok(bookService.getAvailableBooks());
        }
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // GET /api/books/{id}
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    // GET /api/books/isbn/{isbn}
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookResponseDto> getBookByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(bookService.getBookByIsbn(isbn));
    }

    // GET /api/books/author/{authorId}
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<BookResponseDto>> getBooksByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorId));
    }

    // POST /api/books
    @PostMapping
    public ResponseEntity<BookResponseDto> createBook(@Valid @RequestBody BookRequestDto dto) {
        BookResponseDto created = bookService.createBook(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/books/{id}
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDto dto
    ) {
        return ResponseEntity.ok(bookService.updateBook(id, dto));
    }

    // DELETE /api/books/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
