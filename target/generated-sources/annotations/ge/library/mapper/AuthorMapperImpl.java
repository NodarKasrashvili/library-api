package ge.library.mapper;

import ge.library.dto.AuthorRequestDto;
import ge.library.dto.AuthorResponseDto;
import ge.library.entity.Author;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-05T21:09:43+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class AuthorMapperImpl implements AuthorMapper {

    @Override
    public Author toEntity(AuthorRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Author.AuthorBuilder author = Author.builder();

        author.firstName( dto.getFirstName() );
        author.lastName( dto.getLastName() );
        author.biography( dto.getBiography() );

        return author.build();
    }

    @Override
    public AuthorResponseDto toDto(Author author) {
        if ( author == null ) {
            return null;
        }

        AuthorResponseDto.AuthorResponseDtoBuilder authorResponseDto = AuthorResponseDto.builder();

        authorResponseDto.id( author.getId() );
        authorResponseDto.firstName( author.getFirstName() );
        authorResponseDto.lastName( author.getLastName() );
        authorResponseDto.biography( author.getBiography() );

        authorResponseDto.bookCount( author.getBooks().size() );

        return authorResponseDto.build();
    }

    @Override
    public void updateEntityFromDto(AuthorRequestDto dto, Author author) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getFirstName() != null ) {
            author.setFirstName( dto.getFirstName() );
        }
        if ( dto.getLastName() != null ) {
            author.setLastName( dto.getLastName() );
        }
        if ( dto.getBiography() != null ) {
            author.setBiography( dto.getBiography() );
        }
    }
}
