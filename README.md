# Spiteful Reminder - A Firebase Reminder App


## Overview

This is a simple reminder application that integrates with Firebase Realtime Database to display and manage reminders. The app fetches reminders, sorts them by date and time, and updates their status when completed. The status is managed through a checkbox, allowing users to mark tasks as completed.


## Features

- Fetches reminders from Firebase Realtime Database.
- Displays reminders with their associated memo text.
- Reminders are sorted by date and time.
- Users can mark reminders as "Completed" by checking a checkbox.
- Real-time updates in Firebase when a task is marked as completed.
- Custom notification channel for notifications.


## Table of Contents

- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Firebase Setup](#firebase-setup)
  - [How It Works](#how-it-works)
  - [Key Classes](#key-classes)
  - [Installation](#installation)
  - [Contributing](#contributing)

## Getting Started

### Prerequisites

- Android Studio
- Firebase Realtime Database setup
- Google Services JSON added to your project
- All of the installed dependencies can be seen in the Gradel script.


## Firebase Setup
1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Create a new project or use an existing one.
3. Add Firebase to your Android app by registering the app and downloading the `google-services.json` file.
4. Place the `google-services.json` file in the `app/` directory of your project.
5. Enable Firebase Realtime Database from the Firebase console.
6. Add data structure under `helper` node in Firebase Realtime Database:


## How It Works
1. Fetching Reminders:
  - The app listens to the helper node in the Firebase Realtime Database and fetches all reminders with the status "Pending".
2.  Displaying Reminders:
   - The reminders are displayed in a ListView, sorted by date and time using a custom comparator.
3.  Marking as Completed:
   - When the user checks the checkbox next to a reminder, its status is updated in Firebase to "Completed", and the app refreshes the view.
4.  Sorting:
   - The reminders are sorted by the date and time of the task, ensuring the tasks are displayed in order of their due times.


## Key Classes
- MainActivity.java: The main activity that handles fetching reminders from Firebase, displaying them in a ListView, and marking them as completed.
- helper.java: Model class that maps to a reminder object in Firebase.
- HelperWithKey.java: Custom class to hold both the reminder (helper object) and its Firebase key.


## Installation

Feel free to clone the repository and explore the code.

```bash
# Clone the repository
$ git clone https://github.com/Sudhanshu-Marudgan/Spiteful-Reminder.git
```


### Contributing
Pull requests are welcome! If you'd like to contribute to this project, please fork the repository and submit a pull request.
1. Fork the repository.
2. Create a new branch (git checkout -b feature-branch).
3. Commit your changes (git commit -am 'Add new feature').
4. Push to the branch (git push origin feature-branch).
5. Create a new Pull Request.
