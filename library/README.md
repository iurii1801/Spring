# Лабораторная №1. Spring Boot приложение для управления библиотекой

## Введение

Этот проект представляет собой Spring Boot приложение для управления библиотекой. Оно поддерживает CRUD-операции и использует JPA для работы с базой данных. Основной целью является создание удобного интерфейса для работы с книгами, авторами, издателями и категориями.

### Функциональные возможности

* Управление авторами (добавление, редактирование, удаление, просмотр);
* Управление книгами, их категориями и издателями;
* Управление библиотекой (хранение коллекции книг);
* Взаимодействие через DTO, а не напрямую с сущностями;
* Работа с реляционной базой данных посредством JPA.

## Архитектура проекта

### Используемые технологии

* **Spring Boot** - основной фреймворк;
* **Spring Data JPA** - для взаимодействия с базой данных;
* **MSSQL** - в качестве базы данных;
* **Maven** - для управления зависимостями;
* **Lombok** - для упрощения работы с сущностями;
* **Spring Web** - для разработки REST API.

### Структура слоёв

* **Контроллеры (Controllers)**: управляют HTTP-запросами и вызывают сервисы.
* **Сервисы (Services)**: бизнес-логика приложения.
* **Репозитории (Repositories)**: работа с базой данных через JPA.
* **DTO (Data Transfer Objects)**: объекты для передачи данных.

## Описание сущностей и их связей

### **Пример: Book (Книга)**

**Описание:** Книга является основной сущностью системы. Она имеет связь с автором, издателем и категориями.

**Связи:**

* Принадлежит одному автору (**Many-to-One**).
* Имеет одного издателя (**Many-to-One**).
* Может принадлежать к нескольким категориям (**Many-to-Many**).

### **Структура класса Book**

```java
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;

    @ManyToMany
    @JoinTable(
        name = "book_category",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;
}
```

### **Использование CRUD-операций в MS SQL с книгами**

1. **Создание книги (INSERT)**

```sql
INSERT INTO Book (title, author_id, publisher_id)
VALUES ('Spring Boot Guide', 1, 1);
```

2. **Чтение всех книг (SELECT)**

```sql
SELECT * FROM Book;
```

3. **Чтение книги по ID**

```sql
SELECT * FROM Book WHERE id = 1;
```

4. **Обновление информации о книге (UPDATE)**

```sql
UPDATE Book SET title = 'Spring Boot Advanced Guide' WHERE id = 1;
```

5. **Удаление книги (DELETE)**

```sql
DELETE FROM Book WHERE id = 1;
```

## Важные концепции

### **JPA аннотации инжекции**

* `@Entity` - обозначает, что класс является сущностью JPA.
* `@Id` - идентификатор сущности.
* `@GeneratedValue(strategy = GenerationType.IDENTITY)` - автоматическая генерация ID.
* `@ManyToOne`, `@OneToMany`, `@ManyToMany`, `@OneToOne` - аннотации для описания связей между таблицами.

### **Lombok Аннотации**

* `@Getter` и `@Setter` - создают геттеры и сеттеры автоматически.
* `@NoArgsConstructor` - создаёт конструктор без аргументов.
* `@AllArgsConstructor` - создаёт конструктор со всеми аргументами.
* `@Data` - объединяет в себе `@Getter`, `@Setter`, `@ToString`, `@EqualsAndHashCode` и `@RequiredArgsConstructor`.

### **JPA Репозиторий**

**Что это?**  
JPA Repository — это интерфейс, предоставляющий методы для работы с базой данных.

**Для чего он нужен?**

* Упрощает взаимодействие с базой данных.
* Позволяет легко реализовать CRUD-операции без написания SQL-запросов.
* Использует Spring Data JPA для автоматического создания запросов.

### **DTO (Data Transfer Object)**

**Для чего нужен DTO?**

* Используется для передачи данных между слоями приложения.
* Позволяет скрыть детали сущностей.
* Предотвращает прямое изменение данных в базе через сущности.
* В отличие от сущностей, в DTO передаётся `id` в конструктор.

### **Сервисный слой (Service Layer)**

**Для чего он нужен?**

* Реализует бизнес-логику приложения.
* Обрабатывает запросы, выполняет валидацию данных.
* Вызывает методы репозитория для взаимодействия с базой данных.

### **Контроллеры (Controllers)**

**Для чего они нужны?**

* Обрабатывают HTTP-запросы.
* Возвращают ответы в виде JSON.
* Используют сервисы для выполнения операций над данными.

### **Инжекция репозитория**

Репозиторий можно инжектировать в сервис двумя способами:

**Через поле:**

```java
@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
}
```

**Через конструктор:**

```java
@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
}
```

## Вывод 

В ходе выполнения данной работы были изучены и применены принципы построения Spring Boot приложения, использующего JPA и REST API. Было реализовано разделение на уровни контроллеров, сервисов и репозиториев, что позволило обеспечить гибкость и масштабируемость системы. Использование Lombok упростило код, а применение DTO позволило минимизировать зависимость представлений от сущностей базы данных.
