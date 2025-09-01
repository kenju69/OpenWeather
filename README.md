# OpenWeather Exam App

This is an Android application built as part of a coding assessment. The app connects to the **OpenWeather API** to fetch and display weather information for the user’s current location. It includes user registration and sign-in, and displays both current weather and a history of weather searches.

---

## Features

- User **Registration** and **Sign In**
- Displays **current location** (City and Country)
- Shows **current temperature** in Celsius
- Displays **Sunrise and Sunset** times
- Shows an **icon for current weather**
- **Two tabs**:
  - **Current Weather**: Shows the weather for the current location
  - **Weather History**: Shows a list of previous weather searches, fetched each time the app opens
- Uses **Google Jetpack components** (ViewModel, LiveData, Room, etc.)
- **Clean architecture** for maintainability and testability

---

## Getting Started

### Prerequisites

- Android Studio Meerkat or later
- Android SDK 31+
- Internet connection
- OpenWeather API Key (free registration required)

### API Key Setup

Add your API key in `res/values/strings.xml`:

```xml
<string name="api_key">YOUR_API_KEY_HERE</string>
