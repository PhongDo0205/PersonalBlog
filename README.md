# PersonalBlog
PersonalBlog is a personal blog application built with Spring MVC, Thymeleaf, and Spring Security. The application allows users to register, log in, write blog posts, and manage their accounts.

## Description
PersonalBlog provides basic features of a personal blog application, including:
- User registration and login
- Writing, editing, and deleting posts
- Managing personal information

## Installation

### Prerequisites
- Intellij IDEA
- Java 8 or higher
- Maven
- MySQL

### Steps

1. Clone the repository to your local machine:
    ```sh
    git clone git@github.com:PhongDo0205/PersonalBlog.git
    ```

3. Configure the database in `WebConfig`:
    ```sh
    dataSource.setUrl("jdbc:mysql://localhost:3306/{your_database_name}?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC");
    dataSource.setUsername("{your_database_username}");
    dataSource.setPassword("your_database_password");
    ```

4. Run the application:
    ```sh
    mvn spring-boot:run
    ```

## Usage
1. Open your browser and go to `http://localhost:8080`
2. Register a new account or log in if you already have one
3. Start writing and managing your blog posts

