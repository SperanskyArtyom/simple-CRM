# Simple CRM

Backend-сервис для управления продавцами и их транзакциями.

## Описание проекта
Проект представляет собой REST API приложение.
Система позволяет вести учет продавцов и регистрировать их транзакции. Данные хранятся в PostgreSQL. Реализовано управление 
схемой БД через Liquibase.

## Основная функциональность
* **Управление продавцами**: Создание, чтение, обновление и удаление (CRUD) профилей продавцов.
* **Учёт транзакций**: Регистрация новых транзакций с привязкой к продавцу.
* **Аналитика и поиск**: Получение списка всех транзакций
и фильтрация по конкретному продавцу (параметр запроса `sellerId`).
Получение статистики лучшего продавца за период, 
и списка продавцов, сумма транзакций которых меньше переданного числа
* **Документация**: Интерактивная документация API через Swagger UI.
* **Обработка исключений**: Стандартизированный вывод ошибок в JSON формате при обработке запроса.

## Технологии и зависимости
* **Java 21**
* **Spring Boot 4.0.6**:
    * *Spring Web* — реализация REST-контроллеров.
    * *Spring Data JPA* — работа с базой данных через Hibernate.
    * *Validation* — проверка входных данных.
* **PostgreSQL 18**
* **Lombok** - генерация boilerplate кода
* **Liquibase** — управление миграциями БД.
* **SpringDoc OpenAPI** — генерация документации Swagger.
* **Testcontainers** — использование Docker-контейнеров для интеграционных тестов.

## Инструкции по сборке и запуску

### Запуск через Docker
Требуется установленный Docker и Docker Compose.

1.  Выполните команду в корне проекта:
    ```bash
    docker-compose up --build -d
    ```
    *Приложение соберется многоэтапным билдом на базе JDK 21.*

2.  Приложение будет доступно по адресу: `http://localhost:8081`
3.  База данных доступна извне по адресу: `localhost:5433`

### Локальный запуск
Требуется JDK 21 и запущенный PostgreSQL.

1.  Настройте параметры базы данных в `src/main/resources/application.yaml`
2.  Соберите и запустите проект:
    ```bash
    ./gradlew bootRun
    ```
3.  Приложение будет доступно по адресу `http://localhost:8080`

## Примеры использования API

### Продавцы
* **Создать продавца**: 
  * `POST /api/v1/sellers`, 
  * Тело запроса - CreateSellerRequest
* **Получить список всех продавцов**:
  * `GET /api/v1/sellers`
* **Получить продавца по id**:
  * `GET /api/v1/sellers/{id}`
* **Обновить данные продавца по id**:
  * `PATCH /api/v1/sellers/{id}`,
  * Тело запроса - UpdateSellerRequest
* **Удалить продавца по id**:
  * `DELETE /api/v1/sellers/{id}`

**CreateSellerRequest** (все поля обязательны):
```json
{
  "name": "string",
  "contactInfo": "string"
}
```

**UpdateSellerRequest**:
```json
{
  "name": "string",
  "contactInfo": "string"
}
```

**Пример ответа**:
```json
{
  "id": 1,
  "name": "Jhon",
  "contactInfo": "phone: +1234567890000",
  "registrationDate": "2026-05-12T17:46:33.137"
}
```

### Транзакции
* **Создать транзакцию**: 
  * `POST /api/v1/transactions`
  * Тело запроса - CreateTransactionRequest
* **Получить список всех транзакции**:
  * `GET /api/v1/transactions`
* **Получить транзакцию по id**: 
  * `GET /api/v1/transactions/{id}`
* **Получить список транзакций конкретного продавца**: 
  * `GET /api/v1/transactions?sellerId=1`

**CreateTransactionRequest** (все поля обязательны):
```json
{
  "sellerId": 1,
  "amount": 100,
  "paymentType": "CASH"
}
```

**Пример ответа**:
```json
{
    "id": 1,
    "sellerId": 1,
    "amount": 100,
    "paymentType": "CASH",
    "transactionDate": "2026-05-12T17:54:34.332"
}
```

### Аналитика
* **Получить лучшего продавца за период** 
    * `GET /api/v1/statistics/top?period=DAY`
    * Значения параметра period: `DAY, MONTH, QUARTER, YEAR`
* **Получить список продавцов с суммой транзакций за указанный период меньше указанной**:
    * `GET /api/v1/statistics/sellers-under-threshold?start=startDate&end=endDate&maxTotalAmount=100` 
    * Параметры запроса:
      * start / end - дата начала и конца периода в формате ISO LocalDateTime (`yyyy-MM-ddTHH:mm:ss.SSS`)
        * *Пример*: `2026-05-12T18:06:09`
        * *Важно*: Не указывайте символ Z в конце (строго без временной зоны).
      * maxTotalAmount - максимальная сумма транзакций number

### Вывод ошибок
Ошибки при выполнении запроса возвращаются в стандартизированном формате:
```json
{
  "message": "Error message",
  "timestamp": "2026-05-12T18:06:09.698",
  "errors": {
    "additionalError1": "Additional error message",
    "additionalError2": "Additional error message",
    "additionalError3": "..."
  }
}
```

**Подробная спецификация доступна при запущенном приложении по следующему URL** - 
    `/swagger-ui/index.html`

## Тестирование
Запуск всех тестов:
```bash
./gradlew test
```

Проект включает в себя Unit-тесты для бизнес-логики (с использованием **Mockito**)
и интеграционные тесты с использованием реальной БД в контейнере (**Testcontainers**).