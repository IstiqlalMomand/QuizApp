# QuizApp - Software Engineering Project

A comprehensive Java Swing Quiz Application developed for the "Software Engineering I" course (Wintersemester 2025/26). This application features a robust MVC architecture, real-time data persistence, and multiple game modes.

## 📋 Features

### 🎮 Game Modes
* **Classic Quiz:** Answer 10 randomly selected questions from the database. Get immediate feedback (Red/Green) and earn points for correct answers.
* **Time Mode:** A fast-paced challenge with a timer. Earn bonus points for answering quickly. Includes "Jokers" like **50:50** and **Skip**.

### 🏆 Highscores
* **Global Leaderboard:** Scores are automatically saved to a persistent JSON database.
* **Details:** Tracks Player Name, Score, and Date of the game.
* **Sorting:** Automatically ranks players from highest to lowest score.

### 🛠️ Admin Panel (Verwaltung)
* **Add Questions:** An intuitive interface to add new questions to the game database.
* **Live Preview:** See a list of all currently saved questions immediately.
* **Input Validation:** Ensures no empty questions are saved.

### 💾 Data Persistence
* **JSON Storage:** Uses `Gson` to store questions and highscores in local files (`questions.json` and `highscores.json`).
* **Dynamic Loading:** The game automatically shuffles and picks 10 random questions from the pool every time a new game starts.

---

## 🏗️ Architecture

The project follows the strict **Model-View-Controller (MVC)** pattern to ensure separation of concerns and maintainability.

* **`model`**: Contains data classes (`Question`, `HighscoreEntry`, `GameLogic`) that define the data structure and business logic.
* **`view`**: Contains all Swing UI components (`Login`, `Quiz`, `TimeMode`, `Admin`, `Highscores`). These classes handle only the display.
* **`data`**: Handles file I/O operations (`DataManager`) using the Singleton-like pattern for static access to JSON files.

---

## 🚀 Getting Started

### Prerequisites
* **Java Development Kit (JDK):** Version 17 or higher.
* **Maven:** For dependency management and building.
* **IDE:** IntelliJ IDEA (recommended), Eclipse, or VS Code.

### Installation

1.  **Clone the repository:**
    ```bash
    git clone https://gitup.uni-potsdam.de/momand1/quizapp.git
    cd quizapp
    ```

2.  **Build the project:**
    ```bash
    mvn clean install
    ```
    *This will download necessary dependencies like JUnit 5 and Google Gson.*

3.  **Run the Application:**
    * Navigate to `src/main/java/com/quizapp/Main.java`.
    * Run the `main` method.

---

## 🧪 Testing

The project includes a comprehensive JUnit 5 test suite covering both Unit and Integration tests, satisfying the project requirements.

### Running Tests
You can run tests via your IDE or using Maven:
```bash
mvn test
```


## Test Coverage

1. **Whitebox Test (`GameLogic`):**
   - Tests complex branching logic for score calculation (Base points + Speed bonus).
   - Verifies correct point allocation based on time remaining.

2. **Blackbox Test (`DataManager`):**
   - Tests the `getUserHighscores` method using Equivalence Class Partitioning.
   - Ensures valid users, non-existent users, and null inputs are handled correctly.

3. **Integration Test:**
   - Verifies that questions can be saved to and loaded from the JSON file system.
---
## 📁 Project Structure

```text
src
├── main
│   ├── java
│   │   └── com.quizapp
│   │       ├── Main.java                 # Entry point
│   │       ├── data
│   │       │   └── DataManager.java      # JSON handling
│   │       ├── model
│   │       │   ├── Question.java
│   │       │   ├── HighscoreEntry.java
│   │       │   └── GameLogic.java
│   │       └── view
│   │           ├── Login.java
│   │           ├── MainMenu.java
│   │           ├── Quiz.java
│   │           ├── TimeMode.java
│   │           ├── Admin.java
│   │           └── Highscores.java
│   └── resources
│       └── (images / icons if any)
│
└── test
    └── java
        └── com.quizapp
            ├── data
            │   └── DataManagerTest.java  # JUnit tests
            └── model
                └── QuestionTest.java
```
---
## 👥 Authors

- Istiqlal Momand
- Helal Storany

---

## 📄 License

This project is created for educational purposes at the **Universität Potsdam**.
