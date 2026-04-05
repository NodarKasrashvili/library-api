package ge.library.controller;

import ge.library.dto.BookRequestDto;
import ge.library.dto.BookResponseDto;
import ge.library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "წიგნების მართვა")
public class BookController {

    private final BookService bookService;

    @GetMapping
    @Operation(
            summary = "ყველა წიგნი",
            description = "ფილტრები: ?title= (ძებნა), ?genre= (ჟანრი), ?available=true (ხელმისაწვდომი)"
    )
    public ResponseEntity<List<BookResponseDto>> getAllBooks(
            @Parameter(description = "სათაურით ძებნა") @RequestParam(required = false) String title,
            @Parameter(description = "ჟანრით ფილტრი")  @RequestParam(required = false) String genre,
            @Parameter(description = "მხოლოდ ხელმისაწვდომი") @RequestParam(required = false) Boolean available
    ) {
        if (title != null && !title.isBlank())       return ResponseEntity.ok(bookService.searchByTitle(title));
        if (genre != null && !genre.isBlank())       return ResponseEntity.ok(bookService.getBooksByGenre(genre));
        if (Boolean.TRUE.equals(available))          return ResponseEntity.ok(bookService.getAvailableBooks());
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "წიგნი ID-ით")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "წიგნი მოიძებნა"),
            @ApiResponse(responseCode = "404", description = "წიგნი ვერ მოიძებნა")
    })
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/isbn/{isbn}")
    @Operation(summary = "წიგნი ISBN-ით")
    public ResponseEntity<BookResponseDto> getBookByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(bookService.getBookByIsbn(isbn));
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "ავტორის ყველა წიგნი")
    public ResponseEntity<List<BookResponseDto>> getBooksByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorId));
    }

    @PostMapping
    @Operation(summary = "ახალი წიგნი")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "წიგნი შეიქმნა"),
            @ApiResponse(responseCode = "400", description = "ვალიდაციის შეცდომა ან ISBN დუბლიკატი"),
            @ApiResponse(responseCode = "404", description = "ავტორი ვერ მოიძებნა")
    })
    public ResponseEntity<BookResponseDto> createBook(@Valid @RequestBody BookRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "წიგნის განახლება")
    public ResponseEntity<BookResponseDto> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDto dto
    ) {
        return ResponseEntity.ok(bookService.updateBook(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "წიგნის წაშლა", description = "ვერ წაიშლება თუ გაცემულია")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "წაიშალა"),
            @ApiResponse(responseCode = "400", description = "წიგნი გაცემულია"),
            @ApiResponse(responseCode = "404", description = "წიგნი ვერ მოიძებნა")
    })
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
