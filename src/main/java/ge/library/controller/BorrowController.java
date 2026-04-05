package ge.library.controller;

import ge.library.dto.BorrowRequestDto;
import ge.library.dto.BorrowResponseDto;
import ge.library.service.BorrowService;
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
@RequestMapping("/api/borrows")
@RequiredArgsConstructor
@Tag(name = "Borrows", description = "წიგნების გაცემა და დაბრუნება")
public class BorrowController {

    private final BorrowService borrowService;

    @GetMapping
    @Operation(
            summary = "გაცემის ჩანაწერები",
            description = "ფილტრები: ?active=true (გაცემული), ?borrower= (მსესხებელი), ?bookId= (წიგნი)"
    )
    public ResponseEntity<List<BorrowResponseDto>> getAllRecords(
            @Parameter(description = "მსესხებლის სახელი") @RequestParam(required = false) String borrower,
            @Parameter(description = "წიგნის ID")          @RequestParam(required = false) Long bookId,
            @Parameter(description = "მხოლოდ გაუბრუნებელი") @RequestParam(required = false) Boolean active
    ) {
        if (borrower != null && !borrower.isBlank()) return ResponseEntity.ok(borrowService.searchByBorrower(borrower));
        if (bookId != null)                          return ResponseEntity.ok(borrowService.getRecordsByBook(bookId));
        if (Boolean.TRUE.equals(active))             return ResponseEntity.ok(borrowService.getActiveLoans());
        return ResponseEntity.ok(borrowService.getAllRecords());
    }

    @GetMapping("/{id}")
    @Operation(summary = "ჩანაწერი ID-ით")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ჩანაწერი მოიძებნა"),
            @ApiResponse(responseCode = "404", description = "ჩანაწერი ვერ მოიძებნა")
    })
    public ResponseEntity<BorrowResponseDto> getRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(borrowService.getRecordById(id));
    }

    @PostMapping
    @Operation(summary = "წიგნის გაცემა")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "წიგნი გაიცა"),
            @ApiResponse(responseCode = "400", description = "წიგნი უკვე გაცემულია"),
            @ApiResponse(responseCode = "404", description = "წიგნი ვერ მოიძებნა")
    })
    public ResponseEntity<BorrowResponseDto> borrowBook(@Valid @RequestBody BorrowRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(borrowService.borrowBook(dto));
    }

    @PatchMapping("/return/{bookId}")
    @Operation(summary = "წიგნის დაბრუნება", description = "წიგნს ხელმისაწვდომად ნიშნავს")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "წიგნი დაბრუნდა"),
            @ApiResponse(responseCode = "400", description = "წიგნი გაცემულ სიაში არ არის")
    })
    public ResponseEntity<BorrowResponseDto> returnBook(
            @Parameter(description = "დასაბრუნებელი წიგნის ID") @PathVariable Long bookId
    ) {
        return ResponseEntity.ok(borrowService.returnBook(bookId));
    }
}
