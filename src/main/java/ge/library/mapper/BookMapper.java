package ge.library.mapper;

import ge.library.dto.BookRequestDto;
import ge.library.dto.BookResponseDto;
import ge.library.entity.Author;
import ge.library.entity.Book;
import org.mapstruct.*;

@Mapper
public interface BookMapper {

    // BookRequestDto → Book
    // author and available are set manually in the service
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "available", ignore = true)
    Book toEntity(BookRequestDto dto);

    // Book → BookResponseDto
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorFullName", expression = "java(fullName(book.getAuthor()))")
    BookResponseDto toDto(Book book);

    // Partial update: apply dto fields onto existing entity (ignore nulls)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "available", ignore = true)
    void updateEntityFromDto(BookRequestDto dto, @MappingTarget Book book);

    // helper called in @Mapping expression above
    default String fullName(Author author) {
        if (author == null) return null;
        return author.getFirstName() + " " + author.getLastName();
    }
}
