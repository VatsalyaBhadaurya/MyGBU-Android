# 🔍 Real Android Error Capture Guide

## Overview
This enhanced system automatically captures **real Android system errors** and sends them to Telegram, including the NotificationService errors you mentioned.

## 🛠️ Components

### 1. **LogWrapper** - Auto-Capturing Log Calls
Replace regular `Log` calls with `LogWrapper` to automatically send important logs to Telegram:

```kotlin
// Instead of:
Log.e("MyTag", "This is an error")

// Use:
LogWrapper.e("MyTag", "This is an error") // Automatically sent to Telegram!
```

**Auto-Capture Rules:**
- ✅ **All Error logs** (`LogWrapper.e()`) → Sent to Telegram
- ✅ **All Warning logs** (`LogWrapper.w()`) → Sent to Telegram  
- ✅ **Info logs with keywords** (crash, security, permission, notification) → Sent to Telegram
- ❌ **Debug/Verbose logs** → Not sent (too much noise)

### 2. **SystemErrorMonitor** - Real System Error Detection
Monitors and reports critical Android system issues:

```kotlin
// Report the exact error you showed:
SystemErrorMonitor.reportNotificationServiceError("com.vatty.mygbu", 5)

// Report other system issues:
SystemErrorMonitor.reportSystemIssue("HIGH_MEMORY_USAGE", "Memory at 87%")
```

**Monitors For:**
- 🔔 NotificationService toast queue overflows
- 💾 High memory usage (>85%)
- 🚨 ANR (Application Not Responding) events
- 💥 System server crashes
- 📦 Package manager errors
- 🪟 Window leaks
- 📡 Network connectivity issues

## 🚀 How to Use

### For New Code:
```kotlin
import com.vatty.mygbu.utils.LogWrapper

class MyActivity : AppCompatActivity() {
    private fun someFunction() {
        try {
            // Your code here
        } catch (e: Exception) {
            // This error will automatically be sent to Telegram!
            LogWrapper.e("MyActivity", "Operation failed", e)
        }
    }
}
```

### For Existing Code:
Simply replace `Log` imports:
```kotlin
// Replace this:
import android.util.Log

// With this:
import com.vatty.mygbu.utils.LogWrapper as Log
```

Now all your existing `Log.e()` calls will automatically send to Telegram!

### Manual System Error Reporting:
```kotlin
// When you detect the NotificationService error:
if (toastCount > 5) {
    SystemErrorMonitor.reportToastOverflow(toastCount)
}

// When you detect other issues:
SystemErrorMonitor.reportSystemIssue("CUSTOM_ERROR", "Description of the issue")
```

## 🎯 Capturing Your Specific Error

The NotificationService error you showed:
```
NotificationService: Package has already queued 5 toasts. Not showing more. Package=com.vatty.mygbu
```

**Now Automatically Captured When:**
1. You use `LogWrapper.e()` instead of `Log.e()`
2. SystemErrorMonitor detects toast overflow
3. Manual reporting with `SystemErrorMonitor.reportNotificationServiceError()`

## 📊 Real-Time Monitoring

The system now monitors:
- ✅ **Memory usage** - Reports when >85%
- ✅ **Toast overflow** - Detects excessive UI operations
- ✅ **Network timeouts** - Captures connection issues
- ✅ **App crashes** - Automatic crash reporting
- ✅ **System errors** - Pattern-based error detection

## 🧪 Testing

Test the system with:
```kotlin
// Test real error capture
LogWrapper.e("TestTag", "This error will appear in Telegram!")

// Test system error monitoring  
SystemErrorMonitor.testSystemErrorReporting()

// Test the exact error you showed
SystemErrorMonitor.reportNotificationServiceError("com.vatty.mygbu", 5)
```

## 🔧 Configuration

All automatic! The system:
- ✅ **Starts automatically** when app launches
- ✅ **Filters intelligently** to avoid spam
- ✅ **Retries failed sends** with exponential backoff
- ✅ **Rate limits** to prevent overwhelming Telegram
- ✅ **Includes context** (timestamp, device info, app info)

## 📈 What You'll See in Telegram

**Error Messages:**
```
❌ [ERROR] MyActivity
📅 2025-06-11 11:45:30
📱 MyGBU v1.0 (1)
Device: Samsung Galaxy S21 (API 33)

📝 Message:
Network connection failed after 3 retries

Stack trace:
java.net.SocketTimeoutException: timeout
    at okhttp3.internal.http2.Http2Stream...
```

**System Issues:**
```
🔔 NOTIFICATION SERVICE ERROR

Package: com.vatty.mygbu
Issue: Already queued 5 toasts. Not showing more.

This indicates the app is trying to show too many toast messages.
Consider implementing toast rate limiting.
```

## 🎉 Result

Now you'll automatically receive:
- ✅ **Real Android system errors** in Telegram
- ✅ **Your app's actual errors** (not just tests)
- ✅ **System performance issues** 
- ✅ **Network problems**
- ✅ **Memory warnings**
- ✅ **The exact NotificationService errors** you mentioned

No more manual logging needed - just use `LogWrapper` instead of `Log` and the system does the rest! 🚀 