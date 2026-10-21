# BLOCKED.exe - The Block Burst Challenge

Welcome to **BLOCKED.exe**! A fast-paced desktop block puzzle game inspired by *Block Blast*, developed in Java using **Java Swing** and **Java 2D** rendering for the Software Construction and Development (SE3005) course at FAST School of Computing.

---

## 🎮 Features Overview

The player places polyomino block shapes onto an 8×8 grid to clear completed horizontal rows and vertical columns to earn points and trigger visual combos.

### Core Gameplay & Systems
- **8×8 Grid Board**: 64-cell board with collision detection, boundary validation, and real-time state tracking.
- **Dynamic 3-Piece Tray**: Holds 3 randomly generated pieces; automatically refills with a fresh set of 3 when empty.
- **20 Polyomino Shapes**: Single square (1×1), lines (1×2, 2×1, 1×3, 3×1, 1×4, 4×1), square (2×2), rectangles (3×2, 2×3), L-shapes (4 rotations), T-shapes (4 rotations), and S-shapes (2 rotations).
- **Placement Validation**: Prevents placements that overlap occupied cells or extend beyond board boundaries without consuming the piece.
- **Simultaneous Row & Column Clearing**: Detects all completed rows and columns simultaneously; cells at intersections are cleared only once.
- **Two Game Modes**:
  - **Classic Mode**: Play until no available tray piece fits on the board; tracks elapsed time.
  - **Time Attack Mode**: 180-second countdown with a target score of 500 points. Player wins upon reaching the target before time runs out.
- **Polymorphic Animations**:
  - Row & column clear flash/burst effect.
  - Placement pop and bounce effect.
  - Floating score popup animation (+100, +300).
- **Audio System (Java Clip)**:
  - Sound effects for selection, placement, line clear, and invalid move / game over.
  - Interactive Mute / Unmute toggle.
- **Data Persistence & Logging**:
  - Best score saved locally to `data/best_score.txt`.
  - Game session state serialization to `data/saved_game.txt` allowing resume from last game state.
  - Timestamped event logging to `logs/app.log`.

---

## 🕹️ Controls

| Control | Mouse Action | Keyboard Action |
| :--- | :--- | :--- |
| **Select Piece** | Click and drag piece from tray | Keys `1`, `2`, or `3` |
| **Move Preview** | Drag across board | Arrow Keys (`↑`, `↓`, `←`, `→`) |
| **Place Piece** | Release mouse button on board | `Enter` or `Space` |
| **Pause / Resume** | Click Pause button | Key `P` |
| **Restart Game** | Click Restart button | Key `R` |
| **Mute / Unmute** | Click Sound button | Key `M` |

---

## 👥 Team & Work Division

### Team Members
- **Abdul Ahad** (24L-3029)
- **M. Anas** (24L-3004)
- **M. Babar Shahzad** (24L-3047)

### Domain-Balanced Work Division
To ensure high cohesion, loose coupling, and equal difficulty, each team member is assigned responsibilities across all three primary domains: **Domain Logic**, **UI / Graphics / Animation**, and **Controls / Media / Persistence**.

| Domain | Abdul Ahad (24L-3029) | M. Anas (24L-3004) | M. Babar Shahzad (24L-3047) |
| :--- | :--- | :--- | :--- |
| **1. Domain Logic & Mechanics** | • `Board.java` (8x8 grid algorithms)<br>• `Cell.java` (Grid cell model) | • `BlockPiece.java` (Geometry & math)<br>• `PieceGenerator.java` (20 shapes)<br>• `ShapeType.java` (Shapes catalog) | • `Tray.java` (3-slot tray logic)<br>• `Game.java` (Game engine & modes)<br>• `GameMode.java` & `GameState.java` |
| **2. UI, Graphics & Animations** | • `Drawable.java` (Interface)<br>• `Animation.java` (Abstract base)<br>• `RowClearAnimation.java`<br>• `GameOverOverlay.java` (Modal) | • `PlacementPopAnimation.java`<br>• `MainMenuPanel.java` (Start screen)<br>• `InstructionsPanel.java` (Rules UI) | • `ScorePopupAnimation.java`<br>• `AnimationManager.java`<br>• `GameFrame.java` (Main window)<br>• `GamePanel.java` (2D Board & HUD)<br>• `PauseOverlay.java` (Modal) |
| **3. Controls, Media & System** | • `MouseInputHandler.java` (Drag/drop)<br>• `InvalidPlacementException.java`<br>• `ScorePersistence.java` (Highscore) | • `SoundManager.java` (Java Clip audio)<br>• `SoundEffectType.java`<br>• `Logger.java` (`logs/app.log`)<br>• `InvalidPieceException.java`<br>• `AudioException.java` | • `KeyboardInputHandler.java` (Keys 1-2-3/Arrows)<br>• `GameStateSerializer.java` (Resume session)<br>• `DataPersistenceManager.java`<br>• `ScoreManager.java` (Combo scoring)<br>• `GameException.java` & `PersistenceException.java`<br>• `Main.java` (Wiring & assembly) |

### Member Breakdown & Responsibility Matrix

| Team Member | Roll Number | Key Functional Deliverables | Assigned Classes |
| :--- | :--- | :--- | :--- |
| **Abdul Ahad** | 24L-3029 | • 8×8 grid boundary validation & collision detection<br>• Simultaneous row & column clearing (intersection cleared once)<br>• Mouse drag-and-drop piece movement & preview<br>• Row/column clear burst & flash visual animations<br>• High score file persistence (`data/best_score.txt`) | `Board`, `Cell`, `Drawable`, `Animation`, `RowClearAnimation`, `GameOverOverlay`, `MouseInputHandler`, `InvalidPlacementException`, `ScorePersistence` |
| **M. Anas** | 24L-3004 | • Complete 20-polyomino shape matrix catalog & factory<br>• Random piece generation for tray refilling<br>• Java Clip WAV sound effects & mute toggle<br>• Start menu & Instructions UI panels<br>• Block placement pop/bounce spring animation<br>• Application event and error logger (`logs/app.log`) | `BlockPiece`, `PieceGenerator`, `ShapeType`, `PlacementPopAnimation`, `MainMenuPanel`, `InstructionsPanel`, `SoundManager`, `SoundEffectType`, `Logger`, `InvalidPieceException`, `AudioException` |
| **M. Babar Shahzad** | 24L-3047 | • 3-slot piece tray management, consumption & auto-refill<br>• Game loop, Classic (elapsed time) & Time Attack (180s/500pts)<br>• Full game state session serialization to resume last state<br>• Keyboard piece selection (`1`,`2`,`3`), arrows & placement<br>• Score computation with line clear combos<br>• Central JFrame / JPanel 2D rendering & Main assembly | `Tray`, `Game`, `GameMode`, `GameState`, `ScorePopupAnimation`, `AnimationManager`, `GameFrame`, `GamePanel`, `PauseOverlay`, `KeyboardInputHandler`, `GameStateSerializer`, `DataPersistenceManager`, `ScoreManager`, `GameException`, `PersistenceException`, `Main` |

---

## 📁 Project Structure

```
F26_SCD_Assignment-2/
├── src/
│   ├── Main.java                          → Application entry point & module wiring
│   ├── model/                             → Core domain models
│   │   ├── Board.java                     → 8x8 grid & placement logic
│   │   ├── Cell.java                      → Individual cell representation
│   │   ├── BlockPiece.java                → Polyomino shape definition
│   │   ├── PieceGenerator.java            → Factory for all 20 piece shapes
│   │   ├── Tray.java                      → 3-slot piece tray management
│   │   └── Game.java                      → Central game state machine & mode logic
│   ├── enums/                             → Domain enumerations
│   │   ├── GameMode.java                  → CLASSIC, TIME_ATTACK
│   │   ├── GameState.java                 → MENU, PLAYING, PAUSED, etc.
│   │   ├── ShapeType.java                 → Catalog of all 20 shapes
│   │   └── SoundEffectType.java           → Sound event types
│   ├── exceptions/                        → Custom exception hierarchy
│   │   ├── GameException.java             → Base game exception
│   │   ├── InvalidPlacementException.java → Collision & boundary violation
│   │   ├── InvalidPieceException.java     → Malformed piece or slot exception
│   │   ├── PersistenceException.java      → File read/write failure
│   │   └── AudioException.java            → Audio clip failure
│   ├── persistence/                       → File persistence subsystem
│   │   ├── DataPersistenceManager.java    → Central persistence coordinator
│   │   ├── GameStateSerializer.java       → Session serializer for resume capability
│   │   └── ScorePersistence.java          → High score text file storage
│   ├── manager/                           → Scoring & combo calculations
│   │   └── ScoreManager.java              → Score calculations and tracking
│   ├── animation/                         → Polymorphic visual animation engine
│   │   ├── Drawable.java                  → Common rendering interface
│   │   ├── Animation.java                 → Abstract animation base class
│   │   ├── RowClearAnimation.java         → Line clear burst & flash effect
│   │   ├── PlacementPopAnimation.java     → Piece drop bounce/pop effect
│   │   ├── ScorePopupAnimation.java       → Floating score text effect
│   │   └── AnimationManager.java          → Animation update & rendering manager
│   ├── audio/                             → Audio subsystem
│   │   └── SoundManager.java              → Java Clip audio manager & mute toggle
│   ├── view/                              → Java Swing & Java 2D presentation
│   │   ├── GameFrame.java                 → Main JFrame application window
│   │   ├── GamePanel.java                 → Custom 2D game board & HUD canvas
│   │   ├── MainMenuPanel.java             → Start menu panel
│   │   ├── InstructionsPanel.java         → How-to-play & controls panel
│   │   ├── PauseOverlay.java              → In-game pause modal overlay
│   │   └── GameOverOverlay.java           → Game over & victory modal overlay
│   ├── controller/                        → User interaction controllers
│   │   ├── MouseInputHandler.java         → Drag-and-drop piece interaction
│   │   └── KeyboardInputHandler.java      → Keyboard selection, arrows, & hotkeys
│   └── util/                              → System utilities
│       └── Logger.java                    → Event & error logging to logs/app.log
├── data/                                  → Text data storage (best_score.txt, saved_game.txt)
├── logs/                                  → Application execution logs (logs/app.log)
├── resources/                             → Sound WAV files & UI image assets
├── .gitignore                             → Git ignore configuration
└── README.md                              → Project documentation
```

---

## 🚀 Installation & Execution

### Prerequisites
- **Java Development Kit (JDK)**: Version 17 or higher.

### Compilation
From the project root directory, run:

```bash
javac -d bin -sourcepath src (Get-ChildItem -Recurse -Filter *.java src).FullName
```

*(On Linux / macOS / Bash: `javac -d bin -sourcepath src $(find src -name "*.java")`)*

### Execution
Run the compiled application:

```bash
java -cp bin Main
```

---

## 👥 Team

Made with ❤️ by **Team Yeagerists++**
- **Abdul Ahad** (24L-3029)
- **M. Anas** (24L-3004)
- **M. Babar Shahzad** (24L-3047)