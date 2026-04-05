package ge.library.controller;

import ge.library.dto.AuthorRequestDto;
import ge.library.dto.AuthorResponseDto;
import ge.library.service.AuthorService;
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
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@Tag(name = "Authors", description = "ავტორების მართვა")
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    @Operation(summary = "ყველა ავტორი", description = "აბრუნებს ყველა ავტორს. ?search= პარამეტრით ძებნა შესაძლებელია.")
    public ResponseEntity<List<AuthorResponseDto>> getAllAuthors(
            @Parameter(description = "ძებნა სახელით ან გვარით")
            @RequestParam(required = false) String search
    ) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(authorService.searchAuthors(search));
        }
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    @Operation(summary = "ავტორი ID-ით")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ავტორი მოიძებნა"),
            @ApiResponse(responseCode = "404", description = "ავტორი ვერ მოიძებნა")
    })
    public ResponseEntity<AuthorResponseDto> getAuthorById(
            @Parameter(description = "ავტორის ID") @PathVariable Long id
    ) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PostMapping
    @Operation(summary = "ახალი ავტორი")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "ავტორი შეიქმნა"),
            @ApiResponse(responseCode = "400", description = "ვალიდაციის შეცდომა ან დუბლიკატი")
    })
    public ResponseEntity<AuthorResponseDto> createAuthor(@Valid @RequestBody AuthorRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createAuthor(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "ავტორის განახლება")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "განახლდა"),
            @ApiResponse(responseCode = "404", description = "ავტორი ვერ მოიძებნა")
    })
    public ResponseEntity<AuthorResponseDto> updateAuthor(
            @PathVariable Long id,
            @Valid @RequestBody AuthorRequestDto dto
    ) {
        return ResponseEntity.ok(authorService.updateAuthor(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "ავტორის წაშლა", description = "ვერ წაიშლება თუ ავტორს წიგნები აქვს")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "წაიშალა"),
            @ApiResponse(responseCode = "400", description = "ავტორს წიგნები აქვს"),
            @ApiResponse(responseCode = "404", description = "ავტორი ვერ მოიძებნა")
    })
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
