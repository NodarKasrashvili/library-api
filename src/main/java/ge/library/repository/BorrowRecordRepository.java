package ge.library.repository;

import ge.library.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByBookId(Long bookId);
    List<BorrowRecord> findByBorrowerNameContainingIgnoreCase(String borrowerName);
    List<BorrowRecord> findByReturned(Boolean returned);
    Optional<BorrowRecord> findByBookIdAndReturnedFalse(Long bookId);
}
