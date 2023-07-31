# java-explore-with-me

### Описание

Свободное время — ценный ресурс. Ежедневно мы планируем, как его потратить — куда и с кем сходить.
Сложнее всего в таком планировании поиск информации и переговоры.
Нужно учесть много деталей: какие намечаются мероприятия, свободны ли в этот момент друзья, как всех пригласить и где
собраться.

Реализован бэкэнд приложения — афиши.
В этой афише можно предложить какое-либо событие от выставки до похода в кино и собрать компанию для участия в нём.

Приложение состоит из двух микросервисов:

- Основной сервис
- Сервис статистики

### Стек:

- Java 11
- Spring Boot
- Hibernate, Criteria API
- PostgreSQL 14
- Maven
- Lombok, MapStruct
- Junit5, Mockito
- Postman
- Docker

## ТЗ

- [Техническое задание для API основного сервиса](./ewm-main-service-spec.json)
- [Техническое задание для API сервиса статистики](./ewm-stats-service-spec.json)

## Postman

- [Тесты API основного сервиса](./postman/ewm-main-service.json)
- [Тесты API сервиса статистики](./postman/ewm-stat-service.json)
- [Тесты API комментариев (дополнительная функциональность)](./postman/feature.json)

## Ссылка на последний pull-request

[открыть](https://github.com/IceCubeNext/java-explore-with-me/pull/5)

<img title="ER diagram" alt="ER diagram" src="/images/EWM.jpg">

## Схема базы данных

<img title="ER diagram" alt="ER diagram" src="/images/EWM.jpg">
