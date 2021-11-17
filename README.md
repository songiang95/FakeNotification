# Permissions

### Required

- android.permission.VIBRATE
- android.permission.PACKAGE_USAGE_STATS
- android.permission.SYSTEM_ALERT_WINDOW
- android.permission.ACTION_MANAGE_OVERLAY_PERMISSION

### Fingerprint

- android.permission.USE_FINGERPRINT
- android.permission.USE_BIOMETRIC

### Intruder

- android.permission.CAMERA

# Classes

## AppLockInfo

```kotlin
data class AppLockInfo(val pkgName: String, val isLocked: Boolean)
```

### Properties

- pkgName: String used to identify an app
- isLocked: Boolean true if app is locked by user
- AppLockInfo.descriptionId: In resource id for AppLockInfo description

## AppLockSetting

```kotlin
data class AppLockSetting(
    val patternVisible: Boolean,
    val fingerprintEnable: Boolean,
    val intruderEnable: Boolean,
    val intruderTimes: Int,
    val lockTimeOut: Long
)
```

### Properties

- patternVisible: Boolean true if lines are visible in locked screen
- fingerprintEnable: Boolean true to enable fingerprint feature
- intruderEnable : Boolean true to enable to capture image of intruders
- intruderTimes : Int if times of failed attempts are more than the intruderTimes them camera will
  be take an picture
- lockTimeOut: Long is an time(ms) threshold to an unlocked state back to locked state

## AppLockIntruder

```kotlin
data class AppLockIntruder(
    val id: Int,
    val pkgName: String,
    val created: Long,
    val imagePath: String
)
```

### Properties

- id: Int identify of intruder
- pkgName: String package name of the app that intruder tries to unlock
- created: Long datetime when camera captured an image
- imagePath: String where contains image on device

## AppLockManager

```kotlin
class AppLockManagerImpl(val context: Context) : IAppLockManager
```

### Properties

```kotlin
val suggestApps: Flow<List<AppLockInfo>>
```

List installed apps which used to setup app lock function

```kotlin
val appLockInfo: Flow<List<AppLockInfo>>
```

List installed apps which includes locked apps and unlocked apps

```kotlin
val intruders: Flow<List<AppLockIntruder>>
```

List intruders who unlock failed times >= AppLockSetting.intruderTimes

#### Note: in a session, at most one photo taken

- ```val settings: Flow<AppLockSetting>```
    - where contains setting parameters of user
    - Note
        - lockTimeOut is starting when the app is closed.
        - if set lockTimeOut = LOCK_AFTER_SCREEN_OFF then an unlocked state back to locked state
          when the screen off

### Public methods

```kotlin
suspend fun lock(pkgName: String)
```

Use to lock an app

- **Parameters:**
    - pkgName: String identify of app that will be locked

```kotlin

suspend fun lock(pkgNames: List<String>)

```

Use to lock a list app

- **Parameters:**
    - pkgNames: List<String> identify of apps that will be locked

```kotlin
suspend fun unlock(pkgName: String)
```

Use to unlock an app

- **Parameters:**
    - pkgName: String identify of app that will be unlocked

```kotlin
suspend fun lock(pkgNames: List<String>)
```

- Use to unlock a list app - **Parameters:**
- pkgNames: List<String> identify of apps that will be unlocked

```kotlin
fun setPassword(password: String, pattern: Boolean)
```

# UsageExample
