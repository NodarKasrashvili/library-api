package ge.library.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDto {
    private Long id;
    private String title;
    private String isbn;
    private Integer publishedYear;
    private String genre;
    private Boolean available;
    private String authorFullName;
    private Long authorId;
}
