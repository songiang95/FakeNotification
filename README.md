# Permissions

- **android.permission.BIND_NOTIFICATION_LISTENER_SERVICE**

```kotlin
//check notification listener permission
fun hasPermission(context: Context): Boolean {
    return NotificationManagerCompat.getEnabledListenerPackages(context)
        .contains(context.packageName)
}

//request notification listener permission
fun requestPermission(context: Context) {
    val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
    context.startActivity(intent)
}
```

# LockNotificationManager

## Constructor

```kotlin
LockNotificationManager(private val context : Context, scope: CoroutineScope)
```

## Properties

```kotlin
val ignoreApps: Flow<List<NotificationApp>>
```

A list of all the apps in the phone where notifications can be locked

##

```kotlin
val lockedNotifications: Flow<List<LockedNotification>>
```

List of all locked notifications

## Public methods

```kotlin
static fun start(context: Context)
```

Start Notification listener service when the service not auto start

##

```kotlin
static fun stop()
```

Stop lock notifications

##

```kotlin
suspend fun addIgnoreApp(pkgName: String)
```

Add 1 app to the list of apps whose notifications can be locked

- **Parameters:**
  - pkgName: package name of the app

##

```kotlin
suspend fun addIgnoreApps(pkgNames: List<String>)
```

Add a list of apps whose notifications can be locked

- **Parameters:**
  - pkgNames: list package name of apps

##

```kotlin
suspend fun removeIgnoreApp(pkgName: String)
```

Remove 1 app from the list of apps whose notifications can be locked

- **Parameters:**
  - pkgName: package name of the app

##

```kotlin
suspend fun removeIgnoreApps(pkgNames: List<String>)
```

Remove list of apps whose notifications can be locked

- **Parameters:**
  - pkgNames: list package name of apps

##

```kotlin
suspend fun removeNotifications(ids: List<Long>)
```

Remove notifications has been locked

- **Parameters:**
  - ids: List id of the locked notifications

##

```kotlin
fun open(id: Long, pkgName: String)
```

Launch action that attach with the locked notification or open the app send that notification

- **Parameters:**
  - id: id of the locked notification
  - pkgName: package name of the app where it's notification has been locked

# Usage Example

```kotlin
//ignore apps
viewModelScope.launch {
    lockNotificationManager.ignoreApps.collect { apps ->
        //apps: [NotificationApp(), NotificationApp(),...]

        //convert to list NotificationAppModel
    }
}

//locked notifications
viewModelScope.launch {
    lockNotificationManager.lockedNotifications.collect { notifications ->
        //notifications: [LockedNotification(id=1,...), LockedNotification(id=2,...),...]

        //convert to LockedNotificationModel
    }
}

//add ignore app
viewModelScope.launch {
    lockNotificationManager.addIgnoreApp("com.android.chrome")
}

//add ignore apps
viewModelScope.launch {
    //listPkgName: ["com.android.chrome", "com.android.sms",...]

    lockNotificationManager.addIgnoreApps(listPkgName)
}

//remove ignore app
viewModelScope.launch {
    lockNotificationManager.removeIgnoreApp("com.android.chrome")
}

//remove ignore apps
viewModelScope.launch {
    //listPkgName: ["com.android.chrome", "com.android.sms",...]

    lockNotificationManager.removeIgnoreApps(listPkgName)
}

//remove locked notifications
viewModelScope.launch {
    //ids: [1,2,3,4,...]
    //ids: [1]
    lockNotificationManager.removeNotifications(ids)
}

//open notification
lockNotificationManager.open(1, "com.android.sms")

//start notification listener service, call when start application.
if (hasPermission && lock_notification_enable && notification_listener_service_not_running) {
    LockNotificationManager.start(context)
}

//stop lock notification, call when start application
if (hasPermission && lock_notification_disable) {
    LockNotificationManager.stop()
}

```

