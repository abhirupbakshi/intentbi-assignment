# Intentbi Assignment

### Description

This application takes XLSX file and saves its contents into the database,
while also checking for errors in the given data.
The app also supports individual data CRUD opinions and authentication.

## Table of Contents

- Features
- Tech Stack
- Getting Started
    - Prerequisites
    - Installation
- API Documentation
- Contributing

## Features

- **Add New User** : User can create an account.

- **Delete User** : User can be removed from the database if they are no longer relevant.

## Tech Stack

    - Spring Framework
    - Spring Security
    - Spring Data
    - Spring Boot
    - Java 17
    - Apache POI
    - Apache Tika
    - PostgresSQL

## Getting Started

- Follow these instructions to set up and run the project on your local machine.

- **Prerequisites**

    - Java 17
    - PostgresSQL

- **Installation**

    - Clone the repository: ***git clone https://github.com/abhirupbakshi/intentbi-assignment***
    - Navigate to the project directory: **intentbi-backend**
    - Configure the database connection in the server configuration files.
    - Run the server locally :
        - BackEnd: ***./mvnw spring-boot:run (Linux and MacOS)***
        - BackEnd: ***./mvnw.cmd spring-boot:run (Windows)***

# API Documentation

| Endpoint       | Method | Description                                                    | Request Payload                                                             | Response            |
|----------------|--------|----------------------------------------------------------------|-----------------------------------------------------------------------------|---------------------|
| `/user`        | POST   | Create a new user                                              | User object                                                                 | Created user object |
| `/user`        | DELETE | Delete user by AUTH Token                                      | None                                                                        | Deleted user object |
| `/auth/login`  | POST   | Needs Username and Password as 'Basic Auth'                    | None                                                                        | Returns JWT Token   |
| `/auth/logout` | POST   | Logout user by AUTH Token                                      | None                                                                        | None                |
| `/data`        | GET    | Get data                                                       | None                                                                        | Data object         |
| `/data`        | POST   | Create a single data                                           | Data object                                                                 | Saved data object   |
| `/data/<id>`   | POST   | Updates and existing data. Need to pass all attributes of data | Updated data object                                                         | Updated data object |
| `/data/<id>`   | DELETE | Delete data by its id                                          | None                                                                        | Deleted data        |
| `/data/xlsx`   | POST   | Persists all data in XLSX file                                 | Takes FormData where the key '_file' contains the actual file as byte array | Array of saved data |

## Contributing

- Contributions are welcome! Feel free to enhance our project by submitting your ideas, bug fixes, or new features
  through pull requests.

<h3 align="center">Thank You ♥️ </h3>
