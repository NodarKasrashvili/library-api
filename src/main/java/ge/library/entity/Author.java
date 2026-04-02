package ge.library.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "სახელი სავალდებულოა")
    @Size(min = 2, max = 100, message = "სახელი უნდა იყოს 2-დან 100 სიმბოლომდე")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "გვარი სავალდებულოა")
    @Size(min = 2, max = 100, message = "გვარი უნდა იყოს 2-დან 100 სიმბოლომდე")
    @Column(nullable = false)
    private String lastName;

    @Size(max = 500, message = "ბიოგრაფია არ უნდა აღემატებოდეს 500 სიმბოლოს")
    @Column(length = 500)
    private String biography;

    // One author can have many books
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<Book> books = new ArrayList<>();
}
