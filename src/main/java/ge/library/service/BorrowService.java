package ge.library.service;

import ge.library.dto.BorrowRequestDto;
import ge.library.dto.BorrowResponseDto;
import ge.library.entity.Book;
import ge.library.entity.BorrowRecord;
import ge.library.exception.BusinessException;
import ge.library.exception.ResourceNotFoundException;
import ge.library.mapper.BorrowMapper;
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
    private final BorrowMapper borrowMapper;

    public List<BorrowResponseDto> getAllRecords() {
        return borrowRecordRepository.findAll().stream().map(borrowMapper::toDto).toList();
    }

    public BorrowResponseDto getRecordById(Long id) {
        return borrowMapper.toDto(findRecordOrThrow(id));
    }

    public List<BorrowResponseDto> getActiveLoans() {
        return borrowRecordRepository.findByReturned(false).stream().map(borrowMapper::toDto).toList();
    }

    public List<BorrowResponseDto> getRecordsByBook(Long bookId) {
        return borrowRecordRepository.findByBookId(bookId).stream().map(borrowMapper::toDto).toList();
    }

    public List<BorrowResponseDto> searchByBorrower(String name) {
        return borrowRecordRepository.findByBorrowerNameContainingIgnoreCase(name).stream().map(borrowMapper::toDto).toList();
    }

    @Transactional
    public BorrowResponseDto borrowBook(BorrowRequestDto dto) {
        Book book = bookService.findBookOrThrow(dto.getBookId());

        if (!book.getAvailable()) {
            throw new BusinessException("წიგნი '" + book.getTitle() + "' ამჟამად გაცემულია და ხელმიუწვდომელია");
        }

        book.setAvailable(false);

        BorrowRecord record = borrowMapper.toEntity(dto);
        record.setBook(book);
        record.setBorrowDate(LocalDate.now());
        record.setReturned(false);

        return borrowMapper.toDto(borrowRecordRepository.save(record));
    }

    @Transactional
    public BorrowResponseDto returnBook(Long bookId) {
        BorrowRecord record = borrowRecordRepository.findByBookIdAndReturnedFalse(bookId)
                .orElseThrow(() -> new BusinessException("წიგნი ID=" + bookId + " არ არის გაცემულ სიაში"));

        record.getBook().setAvailable(true);
        record.setReturned(true);
        record.setReturnDate(LocalDate.now());

        return borrowMapper.toDto(borrowRecordRepository.save(record));
    }

    private BorrowRecord findRecordOrThrow(Long id) {
        return borrowRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("გაცემის ჩანაწერი", id));
    }
}
