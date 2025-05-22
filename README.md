# API Automation Demo

This project is a simple API automation test suite using **Java**, **JUnit 5**, and **RestAssured** to validate the functionality of a public REST API service [reqres.in](https://reqres.in/). It is developed as part of a testing challenge and demonstrates practical REST API testing techniques.

---

## ✅ Technologies Used

* Java 11
* Maven
* JUnit 5
* RestAssured
* AssertJ

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

### 4. Run tests with a different base URL (optional)

```bash
mvn clean test -DbaseUrl=https://reqres.in
```

---

## 📖 Test Structure

The tests are organized by type:

### `UserTests.java`

* Validate list of users with pagination
* Validate fields in the user JSON
* Extract user data and assert values
* Sort all users by first name

### `UserDetailsTests.java`

* Fetch single user details using `/api/users/{id}`
* Assert user data presence
* Handle and assert non-existing users (expect 404)

### `UserOperationsTests.java`

* Create a new user with `POST /api/users`
* Delete user using `DELETE /api/users/{id}`

---

## ✅ Test Scenarios Covered

| Endpoint                  | Method | Description                           |
| ------------------------- | ------ | ------------------------------------- |
| `/api/users?page=1`       | GET    | List users                            |
| `/api/users/{id}`         | GET    | Get details of specific user          |
| `/api/users`              | POST   | Create new user with random name      |
| `/api/users/{id}`         | DELETE | Delete newly created user             |
| `/api/users/{invalid-id}` | GET    | Handle invalid user id and assert 404 |

---

## ⚖️ Configuration

The `BaseTest` class sets up a shared request specification for all tests.

```java
RequestSpecBuilder()
    .setBaseUri("https://reqres.in")
    .addHeader("x-api-key", "reqres-free-v1")
    .setContentType("application/json")
```

The `baseUrl` can be overridden via system property:

```bash
mvn test -DbaseUrl=https://your-env-url.com
```

---

## 📃 Sample Output

Example output when sorting users by first name:

```
Sorted Users by First Name:
 - Byron Fields | byron.fields@reqres.in
 - Charles Morris | charles.morris@reqres.in
 - ...
```

---

## 🚀 Improvements for the Future

* Integrate Allure for HTML reports
* Use environment-specific configuration files
* Mock API requests for isolated testing

---

## © License

This project is licensed under the MIT License.

---

## ✉️ Contact

Martin Kenov

---

Happy Testing! 🚀
