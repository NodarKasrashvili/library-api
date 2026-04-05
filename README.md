# 📚 Library Management API

Spring Boot-ზე აგებული RESTful API ბიბლიოთეკის მართვისთვის.

---

## 🚀 პროექტის გაშვება

### მოთხოვნები
- Java 17+
- Maven 3.8+

### გაშვება
```bash
git clone <repo-url>
cd library-api
./mvnw spring-boot:run
```

| URL | აღწერა |
|-----|--------|
| http://localhost:8080/swagger-ui.html | **Swagger UI** — ინტერაქტიური დოკუმენტაცია |
| http://localhost:8080/v3/api-docs | OpenAPI JSON spec |
| http://localhost:8080/h2-console | H2 DB Console (JDBC: `jdbc:h2:mem:librarydb`, user: `sa`) |

---

## 🏗 არქიტექტურა

```
ge.library/
├── config/          ← OpenApiConfig (Swagger bean)
├── controller/      ← REST endpoints (@Tag, @Operation, @ApiResponse)
├── service/         ← ბიზნეს ლოგიკა
├── repository/      ← Spring Data JPA
├── mapper/          ← MapStruct (Entity ↔ DTO)
│   ├── AuthorMapper
│   ├── BookMapper
│   └── BorrowMapper
├── entity/          ← @Entity კლასები
├── dto/             ← Request / Response ობიექტები
└── exception/       ← GlobalExceptionHandler
```

**Entity კავშირები:**
- `Author` → `Book` : **@OneToMany** (ერთი ავტორი — ბევრი წიგნი)
- `Book` → `BorrowRecord` : **@ManyToOne**

---

## 📋 API Endpoints

### 👤 Authors `/api/authors`

| Method | URL | აღწერა |
|--------|-----|--------|
| GET | `/api/authors` | ყველა ავტორი |
| GET | `/api/authors?search=ილია` | ძებნა |
| GET | `/api/authors/{id}` | ID-ით |
| POST | `/api/authors` | შექმნა |
| PUT | `/api/authors/{id}` | განახლება |
| DELETE | `/api/authors/{id}` | წაშლა |

### 📖 Books `/api/books`

| Method | URL | აღწერა |
|--------|-----|--------|
| GET | `/api/books` | ყველა |
| GET | `/api/books?title=ვეფხ` | სათაურით |
| GET | `/api/books?genre=პოეზია` | ჟანრით |
| GET | `/api/books?available=true` | ხელმისაწვდომი |
| GET | `/api/books/{id}` | ID-ით |
| GET | `/api/books/isbn/{isbn}` | ISBN-ით |
| GET | `/api/books/author/{authorId}` | ავტორის წიგნები |
| POST | `/api/books` | შექმნა |
| PUT | `/api/books/{id}` | განახლება |
| DELETE | `/api/books/{id}` | წაშლა |

### 🔄 Borrows `/api/borrows`

| Method | URL | აღწერა |
|--------|-----|--------|
| GET | `/api/borrows` | ყველა ჩანაწერი |
| GET | `/api/borrows?active=true` | მიმდინარე გაცემები |
| GET | `/api/borrows?borrower=გიო` | მსესხებლის ძებნა |
| GET | `/api/borrows?bookId=3` | წიგნის ისტორია |
| POST | `/api/borrows` | წიგნის გაცემა |
| PATCH | `/api/borrows/return/{bookId}` | დაბრუნება |

---

## 🛠 ტექნოლოგიები

| ტექნოლოგია | მიზანი |
|-----------|--------|
| Spring Boot 3.2 | ფრეიმვორკი |
| Spring Data JPA | ORM / Repository |
| MapStruct 1.5.5 | Entity ↔ DTO mapper |
| SpringDoc OpenAPI 2.3 | Swagger UI |
| H2 Database | In-memory DB |
| Bean Validation | @Valid |
| Lombok | Boilerplate შემცირება |
