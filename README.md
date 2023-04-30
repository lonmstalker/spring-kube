*Инфо*
Сервис для создания телеграм ботов-справочников, сбора статистики по телеграм

*Сборка*
1. Создать бд и пользователя из [liquibase.properties](logic-service-db%2Fsrc%2Fmain%2Fresources%2Fliquibase.properties)
2. cd logic-service-db
3. mvn clean package -U
4. mvn liquibase:update
5. cd ..
6. mvn clean package -U