package ge.library.mapper;

import ge.library.dto.BookRequestDto;
import ge.library.dto.BookResponseDto;
import ge.library.entity.Author;
import ge.library.entity.Book;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-05T21:09:43+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class BookMapperImpl implements BookMapper {

    @Override
    public Book toEntity(BookRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Book.BookBuilder book = Book.builder();

        book.title( dto.getTitle() );
        book.isbn( dto.getIsbn() );
        book.publishedYear( dto.getPublishedYear() );
        book.genre( dto.getGenre() );

        return book.build();
    }

    @Override
    public BookResponseDto toDto(Book book) {
        if ( book == null ) {
            return null;
        }

        BookResponseDto.BookResponseDtoBuilder bookResponseDto = BookResponseDto.builder();

        bookResponseDto.authorId( bookAuthorId( book ) );
        bookResponseDto.id( book.getId() );
        bookResponseDto.title( book.getTitle() );
        bookResponseDto.isbn( book.getIsbn() );
        bookResponseDto.publishedYear( book.getPublishedYear() );
        bookResponseDto.genre( book.getGenre() );
        bookResponseDto.available( book.getAvailable() );

        bookResponseDto.authorFullName( fullName(book.getAuthor()) );

        return bookResponseDto.build();
    }

    @Override
    public void updateEntityFromDto(BookRequestDto dto, Book book) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getTitle() != null ) {
            book.setTitle( dto.getTitle() );
        }
        if ( dto.getIsbn() != null ) {
            book.setIsbn( dto.getIsbn() );
        }
        if ( dto.getPublishedYear() != null ) {
            book.setPublishedYear( dto.getPublishedYear() );
        }
        if ( dto.getGenre() != null ) {
            book.setGenre( dto.getGenre() );
        }
    }

    private Long bookAuthorId(Book book) {
        if ( book == null ) {
            return null;
        }
        Author author = book.getAuthor();
        if ( author == null ) {
            return null;
        }
        Long id = author.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
