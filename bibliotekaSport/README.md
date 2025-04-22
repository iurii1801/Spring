# Лабораторная №3: Spring Boot приложение для управления спортивной библиотекой (JDBC-реализация)

## Введение

Данный проект представляет собой RESTful приложение, разработанное на Spring Boot, для управления спортивной библиотекой. Реализовано 5 взаимосвязанных сущностей: команды, игроки, тренеры, лиги и матчи. Основной акцент сделан на использование Spring JDBC (JdbcTemplate), работу через DTO и реализацию собственного слоя DAO **без использования Hibernate или JPA**.

## Цели работы

- Перейти от использования Hibernate к Spring JDBC.
- Использовать DTO для передачи данных между слоями.
- Обеспечить полноценную CRUD-функциональность для всех сущностей.
- Проверить существование внешних ключей перед сохранением.
- Тестировать API через Swagger.

## Используемые технологии

- **Spring Boot 3.2.5**
- **Spring Web (REST API)**
- **Spring JDBC (JdbcTemplate)**
- **MSSQL Server**
- **Lombok**
- **Swagger (springdoc-openapi)**
- **Maven**

## Архитектура проекта

### Структура слоёв

- **Controller** — REST-интерфейс API.
- **Service + Impl** — бизнес-логика и валидации.
- **Repository + Impl** — DAO-интерфейсы и JdbcTemplate-реализации.
- **DTO** — классы для передачи данных.
- **Exception** — собственное исключение `NotFoundException`.

### Полная структура пакетов

```text
com.example.bibliotekaSport
├── controller
│   ├── TeamController.java
│   ├── PlayerController.java
│   ├── CoachController.java
│   ├── LeagueController.java
│   └── MatchController.java
│
├── dto
│   ├── TeamDto.java
│   ├── PlayerDto.java
│   ├── CoachDto.java
│   ├── LeagueDto.java
│   └── MatchDto.java
│
├── exception
│   └── NotFoundException.java
│
├── repository
│   ├── TeamRepository.java
│   ├── PlayerRepository.java
│   ├── CoachRepository.java
│   ├── LeagueRepository.java
│   ├── MatchRepository.java
│   └── impl
│       ├── JdbcTeamRepository.java
│       ├── JdbcPlayerRepository.java
│       ├── JdbcCoachRepository.java
│       ├── JdbcLeagueRepository.java
│       └── JdbcMatchRepository.java
│
├── service
│   ├── TeamService.java
│   ├── PlayerService.java
│   ├── CoachService.java
│   ├── LeagueService.java
│   ├── MatchService.java
│   └── impl
│       ├── TeamServiceImpl.java
│       ├── PlayerServiceImpl.java
│       ├── CoachServiceImpl.java
│       ├── LeagueServiceImpl.java
│       └── MatchServiceImpl.java
│
├── BibliotekaSportApplication.java
└── resources
    ├── application.properties
    └── schema.sql
```

## Сущности и связи

- **League** — содержит команды.
- **Coach** — один тренер на команду.
- **Team** — связана с тренером, лигой, игроками и матчами.
- **Player** — связан с командой (Many-to-One).
- **Match** — содержит home_team, away_team, дату и счёт.

Все связи реализованы через внешние ключи и отражаются в DTO.

## Работа с DTO

Все контроллеры и сервисы работают исключительно с DTO-классами. Связанные сущности представлены в виде `ID` при создании/обновлении и полностью разворачиваются при получении.

### Пример: TeamDto

```java
package com.example.bibliotekaSport.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TeamDto {
    private Long id;

    @NotBlank(message = "Team name must not be blank")
    private String name;

    @NotNull(message = "Coach ID is required")
    private Long coachId;

    @NotNull(message = "League ID is required")
    private Long leagueId;

    @NotNull(message = "Player IDs are required")
    private List<Long> playerIds;

    // Расширенные поля для ответа (GET)
    private CoachDto coach;
    private LeagueDto league;
    private List<PlayerDto> players;
    private List<MatchDto> matches;
}
```

## CRUD и Swagger

Swagger UI доступен по адресу: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Поддерживаемые операции (на примере Team):

- `GET /api/teams` — получить все команды
- `GET /api/teams/{id}` — получить команду по id
- `POST /api/teams` — создать команду (с coachId, leagueId, playerIds)
- `PUT /api/teams/{id}` — обновить команду
- `DELETE /api/teams/{id}` — удалить команду

## Настройка подключения к базе данных

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=library_DB;encrypt=true;trustServerCertificate=true;
spring.datasource.username=sa
spring.datasource.password=iurii123
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect

spring.h2.console.enabled=false

springdoc.swagger-ui.path=/swagger-ui.html
```

## Примеры данных

Для проверки приложения вручную в MSSQL были добавлены:

- 5 лиг (Premier League, La Liga, Serie A, Bundesliga, Ligue 1)
- 5 тренеров (например, Carlo Ancelotti, Xavi, Erik ten Hag и др.)
- 5 команд с привязкой к тренерам и лигам
- 5 игроков (по одному на каждую команду)
- 5 матчей между командами с датой и счётом

## Ответы на вопросы

**Почему необходимо использовать DTO?**  
DTO позволяют отделить структуру API от внутренней логики приложения и избежать раскрытия деталей базы данных.

**Что произойдет, если в POST-запросе указать несуществующий `coachId` или `leagueId`?**  
Произойдет исключение `NotFoundException`, так как в сервисах вызывается проверка `existsById()` для связанных сущностей.

**Можно ли создавать новые вложенные сущности вместе с основной (например, команду и тренера одновременно)?**  
Нет, по условиям лабораторной работы при создании используются только уже существующие ID вложенных сущностей.

**Что делает слой Service?**  
Он содержит бизнес-логику, преобразование DTO, проверку существования сущностей и выбрасывание исключений при ошибках.

**Почему используется отдельная таблица `team_players`, а не `team_id` в таблице `players`?**  
Так реализована связь Many-to-Many или One-to-Many через внешнюю таблицу. Это даёт больше гибкости и нормализации.

**Каким образом реализована валидация?**  
Через аннотации в DTO-классах (`@NotBlank`, `@Min`, `@NotNull`) и вручную в сервисах — проверка ID, формат даты, и др.

**Как было протестировано приложение?**  
Через Swagger UI, где вручную протестированы все операции для каждой сущности. Дополнительно использовались SQL-запросы в SSMS для верификации связей.

## Вывод

В результате лабораторной работы было реализовано приложение с полной CRUD-функциональностью на Spring Boot и JDBC. Все операции работают с DTO, сущности связаны через внешние ключи, а проверка связей и тестирование проводились через Swagger. Проект выполнен строго в соответствии с условиями лабораторной №3.
