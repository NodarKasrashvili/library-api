package ge.library.controller;

import ge.library.dto.AuthorRequestDto;
import ge.library.dto.AuthorResponseDto;
import ge.library.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    // GET /api/authors
    @GetMapping
    public ResponseEntity<List<AuthorResponseDto>> getAllAuthors(
            @RequestParam(required = false) String search
    ) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(authorService.searchAuthors(search));
        }
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    // GET /api/authors/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    // POST /api/authors
    @PostMapping
    public ResponseEntity<AuthorResponseDto> createAuthor(@Valid @RequestBody AuthorRequestDto dto) {
        AuthorResponseDto created = authorService.createAuthor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/authors/{id}
    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> updateAuthor(
            @PathVariable Long id,
            @Valid @RequestBody AuthorRequestDto dto
    ) {
        return ResponseEntity.ok(authorService.updateAuthor(id, dto));
    }

    // DELETE /api/authors/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
