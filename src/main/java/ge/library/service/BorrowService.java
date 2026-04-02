package ge.library.service;

import ge.library.dto.BorrowRequestDto;
import ge.library.dto.BorrowResponseDto;
import ge.library.entity.Book;
import ge.library.entity.BorrowRecord;
import ge.library.exception.BusinessException;
import ge.library.exception.ResourceNotFoundException;
import ge.library.repository.BorrowRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookService bookService;

    public List<BorrowResponseDto> getAllRecords() {
        return borrowRecordRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public BorrowResponseDto getRecordById(Long id) {
        return toResponseDto(findRecordOrThrow(id));
    }

    public List<BorrowResponseDto> getActiveLoans() {
        return borrowRecordRepository.findByReturned(false)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<BorrowResponseDto> getRecordsByBook(Long bookId) {
        return borrowRecordRepository.findByBookId(bookId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<BorrowResponseDto> searchByBorrower(String name) {
        return borrowRecordRepository.findByBorrowerNameContainingIgnoreCase(name)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Transactional
    public BorrowResponseDto borrowBook(BorrowRequestDto dto) {
        Book book = bookService.findBookOrThrow(dto.getBookId());

        if (!book.getAvailable()) {
            throw new BusinessException(
                    "წიგნი '" + book.getTitle() + "' ამჟამად გაცემულია და ხელმიუწვდომელია"
            );
        }

        // Mark book as unavailable
        book.setAvailable(false);

        BorrowRecord record = BorrowRecord.builder()
                .borrowerName(dto.getBorrowerName())
                .book(book)
                .borrowDate(LocalDate.now())
                .returned(false)
                .build();

        return toResponseDto(borrowRecordRepository.save(record));
    }

    @Transactional
    public BorrowResponseDto returnBook(Long bookId) {
        BorrowRecord record = borrowRecordRepository.findByBookIdAndReturnedFalse(bookId)
                .orElseThrow(() -> new BusinessException(
                        "წიგნი ID=" + bookId + " არ არის გაცემულ სიაში"
                ));

        // Mark book as available again
        record.getBook().setAvailable(true);
        record.setReturned(true);
        record.setReturnDate(LocalDate.now());

        return toResponseDto(borrowRecordRepository.save(record));
    }

    // --- helpers ---

    private BorrowRecord findRecordOrThrow(Long id) {
        return borrowRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("გაცემის ჩანაწერი", id));
    }

    private BorrowResponseDto toResponseDto(BorrowRecord record) {
        return BorrowResponseDto.builder()
                .id(record.getId())
                .borrowerName(record.getBorrowerName())
                .borrowDate(record.getBorrowDate())
                .returnDate(record.getReturnDate())
                .returned(record.getReturned())
                .bookTitle(record.getBook().getTitle())
                .bookId(record.getBook().getId())
                .build();
    }
}
