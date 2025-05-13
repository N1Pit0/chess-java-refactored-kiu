# ♟️ Chess Game Refactoring Summary

## 🔧 Overview
This project involved refactoring a legacy chess game codebase that suffered from poor design, bugs, and maintainability issues. The goal was to clean up the code, enhance readability, and make the project modular and testable. The refactoring process focused on decoupling logic, introducing modern design patterns, and adhering to SOLID principles.

---

## ⚠️ Initial Problems

### Mixed Concerns
- Domain classes combined game logic, view rendering, and data management, making debugging and extending the project challenging.

### Overcomplicated Piece Class
- The `Piece` class contained both movement logic and game state management, violating the Single Responsibility Principle (SRP).

### Broken Movement Logic
- Legal move calculations for pieces were hardcoded and buggy.
- Pawns could move two squares forward when not allowed.
- Knights could capture friendly pieces.

### Checkmate Detection
- No proper logic for detecting checkmate, stalemate, or checks.
- Players could ignore checks entirely.

### Hardcoded Values
- Image paths and color values were hardcoded, making the code brittle.

### Poor Project Structure
- Classes were scattered with unclear package organization.
- The project lacked dependency management.

---

## ✅ Refactoring Highlights

### 1. Migrated to Maven Project Structure
- Standardized dependency management and build processes.
- Proper separation of source (`src/main`) and test (`src/test`) directories.
- Replaced hardcoded image paths with a reusable `ImageReaderUtil` class.

### 2. Separation of Concerns
- Decoupled rendering logic from the model:
    - `Board`, `Square`: Dedicated model classes.
    - `BoardService`, `SquareInterface`, `PieceInterface`: Dedicated service classes
    - `view` package: Rendering logic and mouse listeners.
    - `GameController`: Handles inputs and mediates between model and view.
- Follows the Model-View-Controller (MVC) architecture.

### 3. Simplified Piece Class
- Movement logic was moved to a reusable helper class (`MovementUtil`).
- Utility methods like `getDiagonalMoves()` and `getLinearMoves()` extracted.
- Color representation replaced with enums (`BLACK`, `WHITE`).

### 4. Strategy Pattern for Movement Logic
- Introduced the Strategy Pattern for piece movement:
    - `MovementStrategy` interface implemented by each piece (e.g., `Knight`, `Pawn`, `Queen`).
- Decorator Pattern used to add advanced movement rules like castling and en passant.

### 5. Rewritten Checkmate Detection
- Improved logic in `CheckmateDetector`:
    - Added `isInCheck`, `isInCheckmate`, and `isInStalemate` methods.
    - Used a simulation-based approach to validate king safety after moves.
    - Introduced an undo system to safely roll back simulations.

### 6. Unit Testing
- Comprehensive unit tests ensure correctness:
    - **Piece Movement:** Knight, Rook, Queen, Pawn, Bishop, and King.
    - **Checkmate Detection:** Tests for all relevant methods.
    - **Board Service:** Ensures consistent game state management.

### 7. Other Improvements
- Refactored `GameWindowImpl` for readability.
- Temporarily broken mouse dragging due to decoupled view logic.
- Renamed variables and reorganized package structure for clarity.

---

## ✅ Result

- **Code Quality:** Significantly improved, adheres to SOLID principles.
- **Modularity:** Extendable and maintainable; supports future enhancements (AI, multiplayer).
- **Bug Fixes:** Resolved movement and checkmate detection issues.
- **Testing:** Unit tests for critical components increase reliability.

---

## 🧠 Remaining Improvements

- **Mouse Dragging:** Currently broken; needs debugging after view logic decoupling.
- **Image Management:** Move image data out of the `Piece` class to a dedicated manager.
- **DTOs:** Consider using Data Transfer Objects to further decouple model and view.

---

## 🚀 How to Run the Program

### Prerequisites
- **Java 17+**
- **Maven**

### Steps

```bash
# Clone the repository
git clone <repository_url>
cd <repository_directory>

# Build the project
mvn clean install

# Run the program
java -jar target/chess-game.jar
```

## Conclusion
This refactoring project vastly improved the original codebase, making it clean, modular, and maintainable. While some issues remain, the game is now ready for future extensions and enhancements. Enjoy playing chess and exploring the refactored code! ♟️