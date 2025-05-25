# API Automation Demo

This project is an API automation test suite built with **Java**, **JUnit 5**, and **RestAssured**.  
It validates the functionality of the public REST API service [reqres.in](https://reqres.in/) and was developed as part of a QA automation challenge.

---

## ✅ Technologies Used

- Java 11  
- Maven  
- JUnit 5  
- RestAssured  
- AssertJ  

---

## ⚙️ Project Setup

### 1. Clone the repository

```bash
git clone https://github.com/martiness/api-automation-demo.git
cd api-automation-demo
```

### 2. Install dependencies

```bash
mvn clean install
```

### 3. Run all tests

```bash
mvn clean test
```

---

## 🔧 Configuration

The project loads configuration values from a `config.properties` file located at:

```
src/test/resources/config.properties
```

Example content:

```properties
base.uri=https://reqres.in
api.key=reqres-free-v1
```

These values are used by the `BaseTest` class via the `Config` utility:

```java
.setBaseUri(Config.getBaseUri())
.addHeader("x-api-key", Config.getApiKey())
```

You can override the base URI via a system property (optional):

```bash
mvn test -DbaseUrl=https://your-env-url.com
```

---

## 🧪 Test Structure

Tests are organized into separate classes by use case:

### `UserTests.java`
- List users with pagination
- Extract fields from JSON and assert values
- Sort users by first name

### `UserDetailsTests.java`
- Fetch a single user by ID
- Validate fields in user response
- Handle and assert 404 for non-existent user

### `UserOperationsTests.java`
- Create user via `POST`
- Delete user via `DELETE`

---

## ✅ Test Scenarios Covered

| Endpoint                  | Method | Description                           |
|--------------------------|--------|---------------------------------------|
| `/api/users?page=1`       | GET    | List users                            |
| `/api/users/{id}`         | GET    | Get user details                      |
| `/api/users`              | POST   | Create new user                       |
| `/api/users/{id}`         | DELETE | Delete newly created user             |
| `/api/users/{invalid-id}` | GET    | Assert 404 on invalid user ID         |

---

## 📃 Sample Output

Example console output when sorting users:

```
Sorted Users by First Name:
 - Byron Fields | byron.fields@reqres.in
 - Charles Morris | charles.morris@reqres.in
...
```

---

## 🚀 Future Improvements

- Integrate Allure for test reporting
- Use environment-based configuration profiles
- Add mocking layer for isolated test scenarios
- Add CI pipeline (e.g. GitHub Actions)

---

## © License

MIT License

---

## ✉️ Author

Martin Kenov

---

Happy Testing! 🚀
