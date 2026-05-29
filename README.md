# Event Ledger API

A Spring Boot based Event Ledger API that handles financial transaction events with support for:

* Idempotent event processing
* Out-of-order event handling
* Account balance computation
* Validation and exception handling
* Swagger/OpenAPI documentation
* Automated testing

---

## Tech Stack

* Java 17
* Spring Boot 3
* Spring Data JPA
* H2 In-Memory Database
* Maven
* Swagger/OpenAPI
* JUnit 5
* Mockito

---

## Features

### Event APIs

* Create transaction events
* Retrieve event by ID
* Retrieve account events in chronological order
* Compute account balance

### Idempotency

Duplicate event submissions with the same `eventId` do not create duplicate records.

### Out-of-Order Event Handling

Events are always returned ordered by `eventTimestamp` regardless of arrival order.

### Validation

The API validates:

* Required fields
* Positive amount values
* Valid event types

### Automated Tests

Includes:

* Integration tests
* Service layer unit tests

---

## Running the Application

### Prerequisites

* Java 17
* Maven

### Start Application

```bash
mvnw.cmd spring-boot:run
```

Application starts on:

```text
http://localhost:8080
```

---

## Swagger API Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

---

## Running Tests

```bash
mvnw.cmd test
```

---

## API Endpoints

| Method | Endpoint                        | Description              |
| ------ | ------------------------------- | ------------------------ |
| POST   | `/events`                       | Submit transaction event |
| GET    | `/events/{id}`                  | Get event by ID          |
| GET    | `/events?account={accountId}`   | Get account events       |
| GET    | `/accounts/{accountId}/balance` | Get account balance      |

---

## Sample Event Request

```json
{
  "eventId": "evt-001",
  "accountId": "acct-123",
  "type": "CREDIT",
  "amount": 150.00,
  "currency": "USD",
  "eventTimestamp": "2026-05-15T14:02:11Z",
  "metadata": "mainframe-batch"
}
```

---

## Project Structure

```text
controller
service
repository
entity
dto
exception
config
```

---

## Future Improvements

* Pagination support
* Docker support
* Authentication & Authorization
* Kafka integration for asynchronous processing
* Concurrency optimization
