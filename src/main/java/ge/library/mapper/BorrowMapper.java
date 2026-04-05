package ge.library.mapper;

import ge.library.dto.BorrowRequestDto;
import ge.library.dto.BorrowResponseDto;
import ge.library.entity.BorrowRecord;
import org.mapstruct.*;

@Mapper
public interface BorrowMapper {

    // BorrowRequestDto → BorrowRecord
    // book, borrowDate, returnDate, returned are set in the service
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "borrowDate", ignore = true)
    @Mapping(target = "returnDate", ignore = true)
    @Mapping(target = "returned", ignore = true)
    BorrowRecord toEntity(BorrowRequestDto dto);

    // BorrowRecord → BorrowResponseDto
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    BorrowResponseDto toDto(BorrowRecord record);
}
