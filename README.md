# HinaCraft Login

![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/xRy0/hclogin/release.yml) ![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/xRy0/hclogin) ![GitHub License](https://img.shields.io/github/license/xRy0/hclogin)


**HinaCraft Login** - это плагин для Velocity, который реализует систему авторизации с использованием QR-кодов и Apache Pulsar. Плагин позволяет игрокам пройти авторизацию через внешний веб-сайт, сканируя QR-код, и автоматически переносит их на игровой сервер после успешной авторизации.

## Возможности

- Генерация уникальных QR-кодов для каждого игрока при входе.
- Авторизация через внешний веб-сайт.
- Интеграция с Apache Pulsar для получения данных об авторизации.
- Автоматическое изменение никнейма и перенаправление игрока на игровой сервер после успешной авторизации.

## Требования

- Velocity 3.3.0 или выше
- Java 8 или выше
- Apache Pulsar сервер
- Maven для сборки проекта

Pulsar брокер должен отправлять данные вида:
```json
{
  "session": "9eOXg6WQn0qRfQbsJxh66wa",
  "token": "1-******-******-******-******-******-jill",
  "user": {
    "id": 3,
    "name": "Ruby Rose"
  }
}
```

## Лицензия
Этот проект лицензирован под лицензией MIT. Подробности можно найти в файле LICENSE.

