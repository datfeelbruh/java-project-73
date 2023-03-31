### Hexlet tests and linter status:
[![Actions Status](https://github.com/datfeelbruh/java-project-73/workflows/hexlet-check/badge.svg)](https://github.com/datfeelbruh/java-project-73/actions)  [![main CI](https://github.com/datfeelbruh/java-project-73/actions/workflows/CI.yml/badge.svg)](https://github.com/datfeelbruh/java-project-73/actions/workflows/CI.yml)  [![Maintainability](https://api.codeclimate.com/v1/badges/db04d449e857b08796d4/maintainability)](https://codeclimate.com/github/datfeelbruh/java-project-73/maintainability) [![Test Coverage](https://api.codeclimate.com/v1/badges/db04d449e857b08796d4/test_coverage)](https://codeclimate.com/github/datfeelbruh/java-project-73/test_coverage)
___
# [Менеджер задач](https://project-5-g1sl.onrender.com)  
___
## Как использовать?
Web приложеие для создание пользователей и задач для них. При создании задач, им можно добавить исполнителя, статус, маркер.  
Статусы и маркеры так же могут быть добавлены на странице приложения авторизованными пользьвателями.
### Примеры сущностей приложения:
#### Пользователь
```
{
    "email": "ivan@google.com",
    "firstName": "Ivan",
    "lastName": "Petrov",
    "password": "some-password"
}
```
#### Маркер
```
{
    "name": "Новая метка",
}
```
#### Статус
```
{
    "name": "Новый"
}
```
#### Задача
```
{
    "name": "Новая задача",
    "description": "Описание новой задачи",
    "executorId": 1,
    "taskStatusId": 1,
    "labelIds" : [
        1
    ]
}
```
___
## API
API построен с использованием REST-style. Представлены endpoints для всех CRUD-операций над сущностями.  
Доступна [документация](https://project-5-g1sl.onrender.com/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config), сгенерированна с помощью open-api.
___
## Использованные технологии:
### Core
* Java
* Gradle
* Spring Web,
* Spring Security, 
* Spring Boot
### Database
* Spring Jpa
* Querydsl
* H2
* Postgres
* Liquibase
### Test
* Junit
* Spring Test

