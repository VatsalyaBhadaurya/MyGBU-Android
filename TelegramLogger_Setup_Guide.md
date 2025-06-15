# TelegramLogger Setup Guide 📱💬

## Overview
The TelegramLogger utility class allows you to send logs, error messages, and crash reports directly to a Telegram chat using your bot. This is extremely useful for real-time monitoring of your app in production.

## 🔧 What's Already Set Up

✅ **TelegramLogger.kt** - Complete utility class in `utils/` folder  
✅ **OkHttp dependencies** - Already in your build.gradle.kts  
✅ **INTERNET permission** - Added to AndroidManifest.xml  
✅ **Bot Token** - Already configured with your MyGBU_bot  
✅ **Test code** - Added to FacultyDashboardActivity  

## 📋 Step-by-Step Setup

### Step 1: Get Your Chat ID

You need to get the chat ID where you want to receive the logs. Here are several ways to do it:

#### Method 1: Using @userinfobot (Recommended)
1. Open Telegram and search for `@userinfobot`
2. Start a conversation with the bot
3. Send any message (like "hi")
4. The bot will reply with your user info including your **User ID**
5. Use this User ID as your Chat ID

#### Method 2: Using @RawDataBot
1. Search for `@RawDataBot` in Telegram
2. Start a conversation and send any message
3. Look for `"from":{"id":123456789` in the response
4. The number after `"id":` is your Chat ID

#### Method 3: Send a test message to your bot first
1. Go to https://t.me/MyGBU_bot (your bot)
2. Click "Start" or send any message
3. Then go to: `https://api.telegram.org/bot7589439541:AAF79-w4LZ0KO1V6jAgYmO2GUe-5reFy0Rg/getUpdates`
4. Look for `"chat":{"id":123456789` in the JSON response
5. That number is your Chat ID

#### Method 4: Create a Group (for team monitoring)
1. Create a new Telegram group
2. Add your MyGBU_bot to the group
3. Make the bot admin (optional but recommended)
4. Send a message to the group
5. Use Method 3 above but the chat ID will be negative for groups (like `-123456789`)

### Step 2: Update the Chat ID in TelegramLogger

1. Open `app/src/main/java/com/vatty/mygbu/utils/TelegramLogger.kt`
2. Find this line:
   ```kotlin
   private const val CHAT_ID = "YOUR_CHAT_ID_HERE"
   ```
3. Replace `"YOUR_CHAT_ID_HERE"` with your actual chat ID:
   ```kotlin
   private const val CHAT_ID = "123456789"  // Your actual chat ID
   ```

### Step 3: Test the Logger

The test code is already added to `FacultyDashboardActivity`. When you run the app and open the Faculty Dashboard, it will automatically send test messages.

You can also test manually from any activity:

```kotlin
// Import the logger
import com.vatty.mygbu.utils.TelegramLogger

// Test different log levels
TelegramLogger.log("This is a basic log message")
TelegramLogger.logDebug("Debug information")
TelegramLogger.logWarning("Warning message")
TelegramLogger.logError("Error occurred", someException)
TelegramLogger.testConnection()
```

## 🚀 Usage Examples

### Basic Logging
```kotlin
TelegramLogger.log("User logged in successfully")
TelegramLogger.log("Assignment submitted", "AssignmentActivity")
```

### Error Logging
```kotlin
try {
    // Some risky operation
    processData()
} catch (e: Exception) {
    TelegramLogger.logError("Failed to process data", e)
}
```

### Debug Logging (Only in debug builds)
```kotlin
TelegramLogger.logDebug("API response: $responseBody")
TelegramLogger.logDebug("User clicked button: ${button.id}")
```

### Crash Reports (Always sent, even in release)
```kotlin
Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
    TelegramLogger.logCrash("App crashed unexpectedly", throwable)
}
```

### Custom Tags
```kotlin
TelegramLogger.log("Payment processed successfully", "PaymentService")
TelegramLogger.logError("Network timeout", networkException, "NetworkManager")
```

## ⚙️ Configuration Options

### Enable/Disable Logging
```kotlin
// Disable logging temporarily
TelegramLogger.setEnabled(false)

// Re-enable logging
TelegramLogger.setEnabled(true)
```

### Include/Exclude Device Information
```kotlin
// Exclude device info for privacy
TelegramLogger.setIncludeDeviceInfo(false)

// Include device info (default)
TelegramLogger.setIncludeDeviceInfo(true)
```

## 📱 Expected Message Format

When you receive messages in Telegram, they will look like this:

```
ℹ️ [INFO] FacultyDashboardActivity
📅 2024-01-15 14:30:25
📱 MyGBU v1.0 (1)
Device: Samsung Galaxy S21 (API 33)

📝 Message:
Faculty Dashboard Activity started successfully!
```

## 🔒 Security Considerations

1. **Bot Token**: Your bot token is hardcoded. For production apps, consider:
   - Moving it to BuildConfig or environment variables
   - Using different bots for debug/release builds

2. **Debug Only**: By default, logs are only sent in debug builds. For production monitoring:
   ```kotlin
   // Enable for release builds too (be careful!)
   TelegramLogger.setEnabled(true)
   ```

3. **Sensitive Data**: Never log sensitive information like passwords, API keys, or personal data.

## 🛠️ Troubleshooting

### "Chat ID not set" warning
- Make sure you replaced `"YOUR_CHAT_ID_HERE"` with your actual chat ID
- Check Android Studio Logcat for warnings

### Messages not appearing in Telegram
1. Verify your chat ID is correct
2. Check if you started a conversation with the bot
3. Ensure the bot isn't blocked
4. Check Android Studio Logcat for network errors
5. Verify internet connection

### Network errors
- Messages are sent asynchronously, so they won't block your UI
- Network failures are logged to Android Studio Logcat
- Consider implementing retry logic for critical logs

## 🔧 Advanced Features

### Custom Message Formatting
Modify the `formatMessage()` function in TelegramLogger.kt to change how messages appear.

### Different Bots for Different Environments
```kotlin
private const val BOT_TOKEN = if (BuildConfig.DEBUG) {
    "DEBUG_BOT_TOKEN"
} else {
    "PRODUCTION_BOT_TOKEN"
}
```

### Batch Logging
For high-frequency logs, consider implementing a queue system to batch messages and avoid hitting Telegram's rate limits.

## 📞 Support

If you encounter any issues:
1. Check Android Studio Logcat for error messages
2. Verify your bot token and chat ID
3. Test with a simple message first
4. Ensure your device has internet connectivity

---

**Remember to remove the test code from FacultyDashboardActivity after confirming everything works!**

Happy logging! 🎉 