package ge.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowRequestDto {

    @NotBlank(message = "მსესხებლის სახელი სავალდებულოა")
    private String borrowerName;

    @NotNull(message = "წიგნის ID სავალდებულოა")
    private Long bookId;
}
