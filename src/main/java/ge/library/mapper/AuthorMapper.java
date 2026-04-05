package ge.library.mapper;

import ge.library.dto.AuthorRequestDto;
import ge.library.dto.AuthorResponseDto;
import ge.library.entity.Author;
import org.mapstruct.*;

@Mapper
public interface AuthorMapper {

    // AuthorRequestDto → Author (ignore id and books — set by JPA)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    Author toEntity(AuthorRequestDto dto);

    // Author → AuthorResponseDto  (books.size() → bookCount)
    @Mapping(target = "bookCount", expression = "java(author.getBooks().size())")
    AuthorResponseDto toDto(Author author);

    // Partial update: apply dto fields onto existing entity (ignore nulls)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    void updateEntityFromDto(AuthorRequestDto dto, @MappingTarget Author author);
}
