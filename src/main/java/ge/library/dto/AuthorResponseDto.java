package ge.library.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String biography;
    private int bookCount;
}
