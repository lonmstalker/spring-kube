*Инфо*
Сервис для создания телеграм ботов-справочников, сбора статистики по телеграм

*Локальная сборка*
1. Создать бд и пользователя из [liquibase.properties](logic-service-db%2Fsrc%2Fmain%2Fresources%2Fliquibase.properties)
2. В консоли: mvn clean package -U "-Ddb.url=localhost:5432/spring_kube" "-Ddb.user=postgres" "-Ddb.password=postgres"

*Запуск logic-service*
1. Создать бд и пользователя из [application.yml](logic-service%2Fsrc%2Fmain%2Fresources%2Fapplication.yml)
2. Запустить logic-service
3. Извлечь в logic-common resources/cert публичный ключ из keystore.pfx: keytool -export -storetype PKCS12 -alias auth-server -keystore keystore.pfx -storepass changeit -file pub.pem

*Запуск auth-service*
1. Создать бд и пользователя из [application.yml](logic-auth-server%2Fsrc%2Fmain%2Fresources%2Fapplication.yml)
2. Сгенерировать пару публичного и приватного ключей: keytool -genkey -alias auth-server -storetype PKCS12 -keyalg RSA -validity 730 -keypass changeit -storepass changeit -keystore keystore.pfx
3. Положить сертификат в resources/cert 
4. Запустить auth-service

*Запуск сервисов в докере*
1. Выолнить локалькую сборку
2. Запустить [docker-compose.yml](infrastructure%2Fdocker-compose.yml)

*Стек*
1. Kotlin 1.7.22
2. Java 17
3. PostgreSQL 15
4. JOOQ 3.17
5. Liquibase 4.17
6. Spring Boot 3.0.6 (Spring WebFlux)
7. MapStruct
8. OpenAPI 3.0.0