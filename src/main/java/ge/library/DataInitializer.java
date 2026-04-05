package ge.library;

import ge.library.entity.Author;
import ge.library.entity.Book;
import ge.library.repository.AuthorRepository;
import ge.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Override
    public void run(String... args) {
        log.info("📚 სატესტო მონაცემების ჩატვირთვა...");

        Author rustaveli = authorRepository.save(Author.builder()
                .firstName("შოთა")
                .lastName("რუსთაველი")
                .biography("XII საუკუნის ქართველი პოეტი, 'ვეფხისტყაოსნის' ავტორი")
                .build());

        Author iliaChavchavadze = authorRepository.save(Author.builder()
                .firstName("ილია")
                .lastName("ჭავჭავაძე")
                .biography("XIX საუკუნის ქართველი მწერალი, პოეტი და საზოგადო მოღვაწე")
                .build());

        Author tolkien = authorRepository.save(Author.builder()
                .firstName("J.R.R.")
                .lastName("Tolkien")
                .biography("ინგლისელი მწერალი, 'ბეჭდების მბრძანებლის' ავტორი")
                .build());


        bookRepository.save(Book.builder()
                .title("კაცია-ადამიანი?!")
                .isbn("978-9941-0-0002-2")
                .publishedYear(1863)
                .genre("მოთხრობა")
                .available(true)
                .author(iliaChavchavadze)
                .build());

        bookRepository.save(Book.builder()
                .title("გლახის ნაამბობი")
                .isbn("978-9941-0-0003-3")
                .publishedYear(1866)
                .genre("მოთხრობა")
                .available(false)
                .author(iliaChavchavadze)
                .build());

        bookRepository.save(Book.builder()
                .title("The Lord of the Rings")
                .isbn("978-0-618-00222-3")
                .publishedYear(1954)
                .genre("Fantasy")
                .available(true)
                .author(tolkien)
                .build());

        bookRepository.save(Book.builder()
                .title("The Hobbit")
                .isbn("978-0-547-92822-7")
                .publishedYear(1937)
                .genre("Fantasy")
                .available(true)
                .author(tolkien)
                .build());

        log.info("✅ {} ავტორი და {} წიგნი ჩაიტვირთა წარმატებით",
                authorRepository.count(), bookRepository.count());
    }
}
