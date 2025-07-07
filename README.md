# cloude-web-back-side

## 프로젝트 개요

이 프로젝트는 Spring Boot를 사용하여 구축된 웹 애플리케이션 백엔드입니다. 파일 업로드 및 테스트용 CRUD API를 제공합니다.

## 주요 기능

- **파일 업로드**: Base64로 인코딩된 파일을 업로드하고 처리합니다.
- **테스트 API**: 사용자 정보 및 폴더를 생성, 조회, 수정, 삭제하는 RESTful API를 제공합니다.

## 기술 스택

- **언어**: Java 24
- **프레임워크**: Spring Boot 3.5.0
- **빌드 도구**: Gradle
- **데이터베이스**: PostgreSQL
- **의존성**:
  - Spring Web
  - Spring Data JPA
  - Lombok
  - Thymeleaf
  - Spring Boot Validation

## API 엔드포인트

### File API

- `POST /api/files`: 파일을 업로드합니다.
  - **Request Body**: `FileUploadRequest` DTO
    ```json
    {
      "fileName": "test.txt",
      "fileContent": "aGVsbG8gd29ybGQ="
    }
    ```
  - **Response**: `FileUploadResponse` DTO

### Test API

- `GET /api/test`: 테스트 문자열을 반환합니다.
- `GET /api/test-gin`: Gin 관련 나이 정보를 반환합니다.
- `POST /api/test-user`: 테스트 사용자를 생성합니다.
  - **Request Body**: `TestUserRequest` DTO
- `GET /api/test-foler`: 테스트 폴더를 생성하고 관련 응답을 반환합니다.
- `DELETE /api/test-delete/{id}`: ID로 테스트 엔티티를 삭제합니다.
- `PUT /api/test-update/{id}`: ID로 테스트 엔티티를 수정합니다.
- `DELETE /api/test-delete-all`: 모든 테스트 엔티티를 삭제합니다.

## 실행 방법

### 1. 데이터베이스 설정

`src/main/resources/application.yaml` 파일에서 PostgreSQL 데이터베이스 연결 정보를 수정합니다.

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/postgres
    driver-class-name: org.postgresql.Driver
    username: your_username
    password: your_password
```

### 2. 프로젝트 빌드

다음 명령어를 사용하여 프로젝트를 빌드합니다.

```bash
./gradlew build
```

### 3. 애플리케이션 실행

다음 명령어를 사용하여 애플리케이션을 실행합니다.

```bash
./gradlew bootRun
```

애플리케이션은 기본적으로 8080 포트에서 실행됩니다.

## 참고

- **H2 데이터베이스**: `application.yaml` 파일에서 주석 처리된 H2 데이터베이스 설정을 사용하여 인메모리 데이터베이스로 테스트할 수 있습니다.
- **디버깅**: `bootRun` task에 JVM 인수가 설정되어 있어, 5005 포트로 원격 디버깅이 가능합니다.
