package ge.library.mapper;

import ge.library.dto.BorrowRequestDto;
import ge.library.dto.BorrowResponseDto;
import ge.library.entity.Book;
import ge.library.entity.BorrowRecord;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-05T21:09:43+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class BorrowMapperImpl implements BorrowMapper {

    @Override
    public BorrowRecord toEntity(BorrowRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        BorrowRecord.BorrowRecordBuilder borrowRecord = BorrowRecord.builder();

        borrowRecord.borrowerName( dto.getBorrowerName() );

        return borrowRecord.build();
    }

    @Override
    public BorrowResponseDto toDto(BorrowRecord record) {
        if ( record == null ) {
            return null;
        }

        BorrowResponseDto.BorrowResponseDtoBuilder borrowResponseDto = BorrowResponseDto.builder();

        borrowResponseDto.bookId( recordBookId( record ) );
        borrowResponseDto.bookTitle( recordBookTitle( record ) );
        borrowResponseDto.id( record.getId() );
        borrowResponseDto.borrowerName( record.getBorrowerName() );
        borrowResponseDto.borrowDate( record.getBorrowDate() );
        borrowResponseDto.returnDate( record.getReturnDate() );
        borrowResponseDto.returned( record.getReturned() );

        return borrowResponseDto.build();
    }

    private Long recordBookId(BorrowRecord borrowRecord) {
        if ( borrowRecord == null ) {
            return null;
        }
        Book book = borrowRecord.getBook();
        if ( book == null ) {
            return null;
        }
        Long id = book.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String recordBookTitle(BorrowRecord borrowRecord) {
        if ( borrowRecord == null ) {
            return null;
        }
        Book book = borrowRecord.getBook();
        if ( book == null ) {
            return null;
        }
        String title = book.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }
}
