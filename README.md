*Инфо*
Сервис для создания телеграм ботов-справочников, сбора статистики по телеграм

*Локальная сборка*
1. Создать бд и пользователя из [liquibase.properties](logic-service-db%2Fsrc%2Fmain%2Fresources%2Fliquibase.properties)
2. В консоли: mvn clean package -U "-Ddb.url=localhost:5432/spring_kube" "-Ddb.user=postgres" "-Ddb.password=postgres"

*Стек*
1. Kotlin 1.7.22
2. Java 17
3. PostgreSQL 15
4. JOOQ 3.17
5. Liquibase 4.17
6. Spring Boot 3.0.6 (Spring WebFlux)
7. MapStruct
8. OpenAPI 3.0.0