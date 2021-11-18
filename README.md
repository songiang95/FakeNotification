# Permission

## Required

- android.permission.BIND_NOTIFICATION_LISTENER_SERVICE

# Classes

## Notification App

```kotlin
data class NotificationApp(
    val pkgName: String,
    val appName: String,
    val locked: Boolean
)
```

### Properties

- pkgName: package name of app
- appName: app name
- locked: if true then notifications of that app are ignored

## LockedNotification

### Properties

```kotlin
data class LockedNotification(
    val id: Long,
    val notifyId: Int,
    val pkgName: String,
    val title: String,
    val content: String,
    val created: Long
)
```

- id: identify a locked notification
- notifyId: is notify id of the notification
- pkgName: package name of app which send the notification
- title: title of notification
- content: content of notification
- created: datetime when app send the notification

## LockNotificationManager

```kotlin
class LockNotificationManager(private val context: Context, scope: CoroutineScope)
```

### Properties

```kotlin
val ignoreApps: Flow<List<NotificationApp>>
```

List installed apps which include locked apps and unlocked apps ( without system apps)

##

```kotlin
val lockedNotifications: Flow<List<LockedNotification>>
```

List locked notifications which are distinguished by pkgName and notifyId fields

### Public methods

```kotlin
suspend fun addIgnoreApp(pkgName: String)
```

Add an app to the list of apps whose notifications will be locked

- **Parameter:**
    - pkgName: package name of app

##

```kotlin
suspend fun addIgnoreApps(pkgNames: List<String>)
```

Add apps to the list of apps whose notifications will be locked

- **Parameter:**
    - pkgNames: package name of apps

##

```kotlin
suspend fun removeIgnoreApp(pkgName: String)
```

Remove an app from the list of apps whose notifications will be locked

- **Parameter:**
    - pkgName: package name of app

##

```kotlin
suspend fun removeIgnoreApps(pkgNames: List<String>)
```

Remove apps from the list of apps whose notifications will be locked

- **Parameter:**
    - pkgName: package name of app

##

```kotlin
suspend fun removeNotifications(ids: List<Long>)
```

Remove list of locked notifications

- **Parameter:**
    - ids: list id of the LockedNotification

##

```kotlin
fun open(id: Long, pkgName: String)
```

Open a locked notification

- **Parameter:**
    - id: id of the LockedNotification
    - pkgName: package name of the app that notify the LockedNotification

#### Note: if can not open locked notification via pending intent then app which notify the locked notification is opened

##

```kotlin
static fun start(context: Context)
```

Start Notification Listener Service

#### Note: must be called this function as soon as the application is opened to lock notification feature works

##

```kotlin
static fun stop()
```

Disable locking notification feature

# Usage Example

## Locked Notification Screen

```kotlin
//to get list locked notifications

coroutineScope.launch {
    lockNotificationManager.lockedNotifications.collect { lockedNotifications ->
        ...
    }
}
//to clear locked notifications
coroutineScope.launch {
    lockNotificationManager.removeNotifications(ids: List< Long >)
}

//to open a locked notification
lockNotificationManager.open(id: Long, pkgName: String)

```

## Locked Notification App Screen

```kotlin
//to get list of all app in device
coroutineScope.launch {
    lockNotificationManager.ignoreApps.collect { ignoreApps ->
        ...
    }
}

//to lock notification of an app or list of app
coroutineScope.launch {
    lockNotificationManager.addIgnoreApp(pkgName: String) or
            lockNotificationManager.addIgnoreApps(pkgNames: List< String >)
}

//to not lock notification of an app or list of app
coroutineScope.launch {
    lockNotificationManager.removeIgnoreApp(pkgName: String) or
            lockNotificationManager.removeIgnoreApps(pkgNames: List< String >)
}
```
