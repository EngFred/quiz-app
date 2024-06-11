# Quiz Quest - Engage and Challenge Your Knowledge

Quiz Quest is an innovative Android application designed to facilitate learning and assessment for students. It features a dual-role system where an admin can set questions, and students can challenge themselves with quizzes in English and Mathematics. The app ensures a structured learning experience through different difficulty levels, allowing students to track and review their performance.

## Features

- **Dual User Roles:**
  - **Admin:** Create and manage quiz questions for different categories and levels.
  - **Students:** Participate in quizzes and review performance.

- **Categories and Levels:**
  - **Categories:** English and Mathematics.
  - **Levels:** Beginner, Intermediate, and Advanced.
  - **Progression:** Students must complete the current level before advancing to the next.

- **Performance Review:**
  - Students can review their answers and performance metrics after completing quizzes.

- **Clean Architecture:**
  - Follows clean architecture principles for a maintainable and scalable codebase.
  - Implements the MVVM (Model-View-ViewModel) pattern to separate concerns and enhance testability.

## Architecture

Quiz Quest is built using the MVVM clean architecture pattern. This approach divides the application into three primary layers, ensuring a robust and maintainable structure:

- **Model:** Manages data and business logic.
- **View:** Handles the UI components and user interactions.
- **ViewModel:** Bridges the Model and View, managing UI-related data and business logic.

## Technologies Used

- **Kotlin:** Utilized for its expressive syntax and modern features in Android development.
- **Jetpack Compose:** Used for building a reactive and efficient UI.
- **Firebase:** Handles all backend services including authentication, database, and storage.

## Setup and Installation

To get started with Quiz Quest, follow these steps:

1. **Clone the Repository:**
    ```sh
    git clone https://github.com/EngFred/quiz-quest.git
    ```

2. **Open the Project:**
    Open the project in Android Studio.

3. **Configure Firebase:**
    - Add your `google-services.json` file to the `app` directory.
    - Ensure you have configured Firebase Authentication, Firestore, and any other required Firebase services.

4. **Build and Run:**
    - Sync the project with Gradle files.
    - Run the app on an emulator or a physical device.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

Special thanks to the Android development community for their contributions to open-source tools and libraries, and to Firebase for providing robust backend services.

---

Engage in a fun and educational journey with Quiz Quest! Whether you're an admin setting up challenges or a student rising through the levels, Quiz Quest offers a structured and enjoyable learning experience.

