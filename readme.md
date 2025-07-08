# 🌟 Akari Puzzle Game

A sleek, modern implementation of the classic Japanese logic puzzle **Akari** (also known as "Light Up"), built with JavaFX and following the Model-View-Controller design pattern.


## 🎯 What is Akari?

Akari is a captivating logic puzzle where you illuminate a grid by strategically placing lamps. The goal is simple yet challenging: **light up every corridor while satisfying all the clues!**

### 🎮 Game Rules
- **💡 Place lamps** on corridor cells to light up the board
- **🔢 Satisfy clues** - numbered cells show how many lamps should be adjacent to them
- **🚫 No conflicts** - lamps cannot "see" each other in straight lines
- **✨ Complete illumination** - every corridor must be lit to win

## 🚀 Features

### 🎨 Intuitive User Interface
- **Clean, modern design** with distinct visual elements for each cell type
- **Interactive gameplay** - click to place/remove lamps
- **Real-time feedback** - see illegal lamp placements instantly
- **Dynamic lighting** - watch corridors light up as you place lamps

### 🧩 Rich Puzzle Experience
- **5 built-in puzzles** of varying difficulty and size
- **Multiple board sizes** - from compact 5x5 to challenging larger grids
- **Smart clue system** - satisfied clues are visually distinct from unsatisfied ones
- **Instant win detection** - get notified when you solve a puzzle

### 🎛️ Powerful Controls
- **🔄 Reset button** - start fresh anytime
- **⏭️ Next/Previous** - navigate through the puzzle library
- **🎲 Random puzzle** - jump to a surprise challenge
- **📊 Progress tracking** - see which puzzle you're on (e.g., "Puzzle 3 of 5")

## 🛠️ Technical Implementation

Built with **clean architecture** principles:

- **🏗️ Model-View-Controller (MVC)** - well-separated concerns
- **👁️ Observer Pattern** - reactive UI updates
- **🎯 JavaFX** - modern, responsive user interface
- **📦 Maven** - streamlined build and dependency management

## 📋 Prerequisites

- **Java 11 or higher**
- **Maven 3.6+**
- **IntelliJ IDEA** (recommended) or any Java IDE

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/akari-puzzle-game.git
cd akari-puzzle-game
```

### 2. Open in IntelliJ IDEA
- Open IntelliJ IDEA
- Select "Open" and choose the project directory
- Wait for Maven to download dependencies

### 3. Run the Application
**Option A: Using Maven (Recommended)**
1. Click the "Maven" tab on the right side of IntelliJ
2. Expand "Plugins" → "javafx"
3. Double-click "javafx:run"

**Option B: Using Command Line**
```bash
mvn javafx:run
```

**Option C: Run Main Class**
- Navigate to `src/main/java/com/comp301/a09akari/Main.java`
- Right-click and select "Run Main.main()"

## 🎮 How to Play

1. **🎯 Objective**: Light up all corridor cells while satisfying every clue
2. **💡 Place lamps**: Click on corridor cells to toggle lamp placement
3. **🔢 Read clues**: Number cells show how many lamps should be adjacent
4. **🚫 Avoid conflicts**: Lamps cannot "see" each other directly
5. **✅ Win condition**: All corridors lit + all clues satisfied + no illegal lamps

### 🎨 Visual Guide
- **🟫 Wall cells** - Dark brown, block light and lamp placement
- **🟨 Clue cells** - Numbered, show required adjacent lamps
- **⬜ Corridor cells** - White when unlit, yellow when lit
- **💡 Lamps** - Bulb icon on corridor cells
- **🔴 Illegal lamps** - Red highlighting when lamps conflict

## 🏗️ Project Structure

```
src/main/java/com/comp301/a09akari/
├── 📁 model/
│   ├── ModelImpl.java           # Core game logic
│   ├── PuzzleImpl.java          # Puzzle representation
│   └── PuzzleLibraryImpl.java   # Puzzle collection
├── 📁 controller/
│   └── ControllerImpl.java      # MVC controller
├── 📁 view/
│   ├── AppLauncher.java         # JavaFX application launcher
│   └── [UI Components]          # Custom FX components
└── 📁 SamplePuzzles.java        # Built-in puzzle library
```

## 🎯 Key Classes

### 🧠 ModelImpl
- Manages game state and puzzle data
- Implements win condition checking
- Handles lamp placement validation
- Notifies observers of state changes

### 🎮 ControllerImpl
- Bridges model and view
- Handles user interactions
- Manages puzzle navigation
- Processes game events

### 🎨 AppLauncher
- JavaFX application entry point
- Initializes MVC components
- Sets up UI layout and observers
- Handles window configuration

## 🔧 Customization

### 🎨 Adding New Puzzles
1. Create a new puzzle array in `SamplePuzzles.java`
2. Follow the encoding: `0-4` = clues, `5` = wall, `6` = corridor
3. Add to the puzzle library in `AppLauncher.java`

### 🎭 Styling
- Modify JavaFX CSS in your view components
- Customize colors, fonts, and layouts
- Add animations and transitions

## 🐛 Troubleshooting

### Common Issues

**❌ JavaFX runtime not found**
- Ensure JavaFX is included in your classpath
- Use `mvn javafx:run` instead of regular Java run

**❌ Application won't launch**
- Check Java version (must be 11+)
- Verify Maven dependencies are downloaded
- Try cleaning and rebuilding: `mvn clean compile`

**❌ UI not updating**
- Ensure ModelObserver is properly registered
- Check that model notifications are triggered
- Verify controller is calling model methods

## 🤝 Contributing

Feel free to fork this project and submit pull requests! Some ideas for enhancements:

- 🎨 **UI Themes** - Dark mode, colorful themes
- 🧩 **Puzzle Generator** - Procedural puzzle creation
- 💾 **Save System** - Save progress and custom puzzles
- 🏆 **Achievements** - Time tracking, move counting
- 🎵 **Audio** - Sound effects and background music

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **University of North Carolina** - Original assignment framework
- **Akari Puzzle Community** - Inspiration and game mechanics
- **JavaFX Team** - Excellent GUI framework

---

### 🎮 Ready to illuminate? Clone and play today!

**Happy puzzling!** 🧩✨
