# API Automation Demo

This project is an API automation test suite built with **Java**, **JUnit 5**, and **RestAssured**.  
It validates the functionality of the public REST API service [reqres.in](https://reqres.in/)

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

``` java 
.setBaseUri(Config.getBaseUri())
.addHeader("x-api-key", Config.getApiKey()) 
```

You can override the base URI via a system property (optional):

```bash
mvn test -DbaseUrl=https://your-env-url.com
```

---

## 🧪 Test Structure

Tests are organized into packages and classes according to CRUD responsibilities:
📂 com.demo.api.tests

    UserCreateTests.java – POST: Create a new user

    UserReadTests.java – GET: List users, get user by ID, extract/sort/validate user data

    UserDeleteTests.java – DELETE: Delete user by ID

📂 com.demo.api.utilities

    BaseTest.java – common RestAssured setup with request specs

    Config.java – loads API configuration from properties file

    UserApiHelper.java – contains reusable helper methods for API calls

All tests now use UserApiHelper to encapsulate RestAssured request logic and follow clean code and SOLID design principles.

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

## 📂 Logging and Debugging

This project uses SLF4J with Logback for structured logging.

    Logs are printed to the console

    Persisted to target/logs/test-info.log

    Automatically attached to the Allure report after each test

Example output:
```
2025-05-26 12:45:30 INFO  BaseTest - Base URI: https://reqres.in
2025-05-26 12:45:30 INFO  BaseTest - API Key: reqres-free-v1
2025-05-26 12:45:31 INFO  UserApiHelper - Creating user with payload: {name=John_12345, job=QA}
2025-05-26 12:45:31 WARN  UserApiHelper - Create user failed. Status: 400, Body: {...}
```
---

## 📂 Allure Report Integration

The log file is attached to each test case inside the Allure report under Attachments → Execution Log:

```
Allure.addAttachment("Execution Log", "text/plain", is, ".log");
```

This improves traceability, especially when debugging failed test scenarios.

---

## 🧪 Allure Reporting

After running the tests, generate and view the Allure HTML report with:

```bash
mvn clean test
mvn allure:serve

```

## 🚀 Future Improvements

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
