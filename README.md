# 📚 Library Management API

Spring Boot-ზე აგებული RESTful API ბიბლიოთეკის მართვისთვის.

---

## 🚀 პროექტის გაშვება

### მოთხოვნები
- Java 17+
- Maven 3.8+

### გაშვება
```bash
# კლონირება
git clone <repo-url>
cd library-api

# Build & Run
./mvnw spring-boot:run
```

სერვერი გაეშვება: **http://localhost:8080**

H2 Console (dev): **http://localhost:8080/h2-console**
- JDBC URL: `jdbc:h2:mem:librarydb`
- Username: `sa` | Password: _(ცარიელი)_

---

## 🏗 არქიტექტურა

```
ge.library/
├── controller/      ← REST endpoints
├── service/         ← ბიზნეს ლოგიკა
├── repository/      ← Spring Data JPA
├── entity/          ← DB ცხრილები (@Entity)
├── dto/             ← Request / Response ობიექტები
└── exception/       ← შეცდომების მართვა
```

**Entity კავშირები:**
- `Author` → `Book` : **@OneToMany** (ერთი ავტორი — ბევრი წიგნი)
- `Book` → `BorrowRecord` : **@OneToMany** (ერთი წიგნი — ბევრი გაცემის ჩანაწერი)

---

## 📋 API Endpoints

### 👤 Authors — `/api/authors`

| Method | URL | აღწერა |
|--------|-----|--------|
| `GET` | `/api/authors` | ყველა ავტორი |
| `GET` | `/api/authors?search=ილია` | ავტორის ძებნა |
| `GET` | `/api/authors/{id}` | ავტორი ID-ით |
| `POST` | `/api/authors` | ახალი ავტორი |
| `PUT` | `/api/authors/{id}` | ავტორის განახლება |
| `DELETE` | `/api/authors/{id}` | ავტორის წაშლა |

**POST /api/authors** — Request Body:
```json
{
  "firstName": "გიორგი",
  "lastName": "ლეონიძე",
  "biography": "ქართველი პოეტი და რომანისტი"
}
```

---

### 📖 Books — `/api/books`

| Method | URL | აღწერა |
|--------|-----|--------|
| `GET` | `/api/books` | ყველა წიგნი |
| `GET` | `/api/books?title=ვეფხ` | სათაურით ძებნა |
| `GET` | `/api/books?genre=პოეზია` | ჟანრით ფილტრი |
| `GET` | `/api/books?available=true` | ხელმისაწვდომი წიგნები |
| `GET` | `/api/books/{id}` | წიგნი ID-ით |
| `GET` | `/api/books/isbn/{isbn}` | წიგნი ISBN-ით |
| `GET` | `/api/books/author/{authorId}` | ავტორის წიგნები |
| `POST` | `/api/books` | ახალი წიგნი |
| `PUT` | `/api/books/{id}` | წიგნის განახლება |
| `DELETE` | `/api/books/{id}` | წიგნის წაშლა |

**POST /api/books** — Request Body:
```json
{
  "title": "ჩემი მოთხრობა",
  "isbn": "978-9941-0-9999-9",
  "publishedYear": 2024,
  "genre": "მოთხრობა",
  "authorId": 1
}
```

---

### 🔄 Borrows — `/api/borrows`

| Method | URL | აღწერა |
|--------|-----|--------|
| `GET` | `/api/borrows` | ყველა ჩანაწერი |
| `GET` | `/api/borrows?active=true` | მიმდინარე გაცემები |
| `GET` | `/api/borrows?borrower=გიო` | მსესხებლის ძებნა |
| `GET` | `/api/borrows?bookId=3` | წიგნის გაცემის ისტორია |
| `GET` | `/api/borrows/{id}` | ჩანაწერი ID-ით |
| `POST` | `/api/borrows` | წიგნის გაცემა |
| `PATCH` | `/api/borrows/return/{bookId}` | წიგნის დაბრუნება |

**POST /api/borrows** — Request Body:
```json
{
  "borrowerName": "გიორგი მაჩაბელი",
  "bookId": 1
}
```

---

## ❌ Error Responses

ყველა შეცდომა სტანდარტული ფორმატით ბრუნდება:

```json
{
  "status": 404,
  "message": "წიგნი ID=99 ვერ მოიძებნა",
  "timestamp": "2024-01-15T10:30:00"
}
```

ვალიდაციის შეცდომები:
```json
{
  "status": 400,
  "message": "ვალიდაციის შეცდომა",
  "timestamp": "2024-01-15T10:30:00",
  "fieldErrors": {
    "isbn": "ISBN ფორმატი არასწორია",
    "title": "სათაური სავალდებულოა"
  }
}
```

---

## 🧪 სწრაფი ტესტი (curl)

```bash
# ყველა ავტორი
curl http://localhost:8080/api/authors

# ახალი ავტორი
curl -X POST http://localhost:8080/api/authors \
  -H "Content-Type: application/json" \
  -d '{"firstName":"ნინო","lastName":"ჰაიდარი","biography":"თანამედროვე ქართველი მწერალი"}'

# ახალი წიგნი
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"ახალი ეპოქა","isbn":"978-9941-0-8888-8","publishedYear":2023,"genre":"რომანი","authorId":1}'

# წიგნის გაცემა
curl -X POST http://localhost:8080/api/borrows \
  -H "Content-Type: application/json" \
  -d '{"borrowerName":"გიორგი ბერიძე","bookId":1}'

# წიგნის დაბრუნება
curl -X PATCH http://localhost:8080/api/borrows/return/1
```

---

## 🛠 ტექნოლოგიები

| ტექნოლოგია | მიზანი |
|-----------|--------|
| Spring Boot 3.2 | ფრეიმვორკი |
| Spring Data JPA | ORM / Repository |
| H2 Database | In-memory DB |
| Bean Validation | @Valid ვალიდაცია |
| Lombok | Boilerplate შემცირება |
| Maven | Build tool |
