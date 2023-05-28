# aki-back

Модуль aki-back предназначен для предоставления API агрегатора площадок и услуг креативных индустрий Москвы. Все данные передаются по протоколу HTTP (или HTTPS) в формате JSON или multipart/form-data (для файлов).

Адрес Swagger UI приложения, развёрнутого в Cloud:

https://let-squad.ru/swagger-ui

### Инструкция по сборке и запуску

#### Сборка приложения

Команда для сборки:

`mvn clean install`

В случае успешной сборки приложения в рабочей директории будет создана поддиректория target с приложением, а в лог будет выведено сообщение:

`BUILD SUCCESS`

#### Запуск приложения

Для настройки подключения к базе данных и к почтовому серверу в рабочей директории необходимо создать файл application.yml со следующим содержимым:

```yaml
spring:
  datasource:
    url: 'jdbc:postgresql://<DATABASE_HOST>/<DATABASE_NAME>'
    username: <DATABASE_USERNAME>
    password: <DATABASE_PASSWORD>
  mail:
    username: <MAIL_USERNAME>
    password: <MAIL_PASSWORD>
```

Команда для запуска (из директории target):

`java -jar aki-backend.jar`

При первом запуске приложения автоматически будет создана структура таблиц в базе данных при помощи Liquibase

После успешной инициализации базы данных в лог будут выведены сообщения вида:

`ChangeSet db/changelog/sql/*.sql::raw::includeAll ran successfully`

После успешного подключения к базе данных и запуска приложения в лог будут выведены сообщения вида:

```
Tomcat started on port(s): 8080 (http) with context path ''
Started AkiApplicationKt in 6.946 seconds (JVM running for 7.586)
```

По умолчанию приложение запускается на 8080 порту, при необходимости переопределить порт нужно в файл application.yml добавить значение:

```yaml
server:
  port: <SERVER_PORT>
```