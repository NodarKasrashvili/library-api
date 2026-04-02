package ge.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorRequestDto {

    @NotBlank(message = "სახელი სავალდებულოა")
    @Size(min = 2, max = 100)
    private String firstName;

    @NotBlank(message = "გვარი სავალდებულოა")
    @Size(min = 2, max = 100)
    private String lastName;

    @Size(max = 500)
    private String biography;
}
