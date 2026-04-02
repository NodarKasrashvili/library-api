package ge.library.controller;

import ge.library.dto.BorrowRequestDto;
import ge.library.dto.BorrowResponseDto;
import ge.library.service.BorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrows")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;

    // GET /api/borrows  (+ ?borrower=... / ?bookId=... / ?active=true)
    @GetMapping
    public ResponseEntity<List<BorrowResponseDto>> getAllRecords(
            @RequestParam(required = false) String borrower,
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) Boolean active
    ) {
        if (borrower != null && !borrower.isBlank()) {
            return ResponseEntity.ok(borrowService.searchByBorrower(borrower));
        }
        if (bookId != null) {
            return ResponseEntity.ok(borrowService.getRecordsByBook(bookId));
        }
        if (Boolean.TRUE.equals(active)) {
            return ResponseEntity.ok(borrowService.getActiveLoans());
        }
        return ResponseEntity.ok(borrowService.getAllRecords());
    }

    // GET /api/borrows/{id}
    @GetMapping("/{id}")
    public ResponseEntity<BorrowResponseDto> getRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(borrowService.getRecordById(id));
    }

    // POST /api/borrows  — გაცემა
    @PostMapping
    public ResponseEntity<BorrowResponseDto> borrowBook(@Valid @RequestBody BorrowRequestDto dto) {
        BorrowResponseDto record = borrowService.borrowBook(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

    // PATCH /api/borrows/return/{bookId}  — დაბრუნება
    @PatchMapping("/return/{bookId}")
    public ResponseEntity<BorrowResponseDto> returnBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(borrowService.returnBook(bookId));
    }
}
