package ge.library.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequestDto {

    @NotBlank(message = "სათაური სავალდებულოა")
    @Size(min = 1, max = 200)
    private String title;

    @NotBlank(message = "ISBN სავალდებულოა")
    @Pattern(regexp = "^[0-9\\-]{10,17}$", message = "ISBN ფორმატი არასწორია")
    private String isbn;

    @NotNull(message = "გამოცემის წელი სავალდებულოა")
    @Min(value = 1450)
    @Max(value = 2100)
    private Integer publishedYear;

    @NotBlank(message = "ჟანრი სავალდებულოა")
    private String genre;

    @NotNull(message = "ავტორის ID სავალდებულოა")
    private Long authorId;
}
