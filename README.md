# Permissions

## Required

- android.permission.READ_EXTERNAL_STORAGE
- android.permission.GET_PACKAGE_SIZE
- android.permission.PACKAGE_USAGE_STATS

## Install and Uninstall apps

- android.permission.REQUEST_INSTALL_PACKAGES
- android.permission.REQUEST_DELETE_PACKAGES

# Classes

## AppFile

```kotlin
data class AppFile(
    val id: String,
    val name: String,
    val size: Long,
    val modified: Long,
    val installed: Boolean
)
```

### Properties

- id: package name of an app or path of an apk file
- name: app name or apk file name
- size (byte): size of app or apk file
- modified (ms): datetime when app is installed or when apk file is modified
- installed: true if apk file is installed.

## InstalledAppManager

```kotlin
class InstalledAppManager(private val context: Context, private val scope: CoroutineScope)
```

### Properties

```kotlin
val appFiles: Flow<List<AppFile>>
```

Collecting list installed apps without system apps

### Public methods

```kotlin
suspend fun clean(activity: AppCompatActivity, ids: List<String>)
```

Uninstall selected apps by corresponding ids in sequence

- **Parameters**
    - activity: AppCompatActivity
    - ids: list id are extract from list selected app files

## CacheAppManager

```kotlin
class CacheAppManager(private val context: Context, private val scope: CoroutineScope)
```

### Properties

```kotlin
val appFiles: Flow<List<AppFile>>
```

Collecting list installed apps where cache app size > 0 and not system apps

### Public methods

```kotlin
suspend fun clean(activity: AppCompatActivity, ids: List<String>)
```

Clean cache folder of apps by corresponding ids in sequence

- **Parameters**
    - activity: AppCompatActivity
    - ids: list id are extract from list selected app files

## ApkFileManager

```kotlin
class ApkFileManager(private val context: Context, private val scope: CoroutineScope)
```

### Properties

```kotlin
val appFiles: Flow<List<AppFile>>
```

Collecting list apk files are on device

### Public methods

```kotlin
suspend fun clean(activity: AppCompatActivity, ids: List<String>)
```

Delete selected apk files by corresponding ids in sequence

- **Parameters**
    - activity: AppCompatActivity
    - ids: list id are extract from list selected app files

# Usage Example

## Cache App Screen

```kotlin
// to get list cache apps
coroutineScope.launch {
    cacheAppManager.appFiles.collect { cacheApps ->

    }
}
// to clear cache data of an app
coroutineScope.launch {
    cacheAppManager.clean(activity: AppCompatActivity, ids: List< String >)
}

```

## App Manager Screen

```kotlin
// to get list apps
coroutineScope.launch {
    installedAppManager.appFiles.collect { installedApps ->

    }
}

// to uninstall an app
coroutineScope.launch {
    installedAppManager.clean(activity: AppCompatActivity, ids: List< String >)
}

```

## Apk File Screen

```kotlin
// to get list apk files
coroutineScope.launch {
    apkFileManager.appFiles.collect { apkFiles ->

    }
}
// to delete apk files
coroutineScope.launch {
    apkFileManager.clean(activity: AppCompatActivity, ids: List< String >)
}

```
