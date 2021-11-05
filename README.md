# Permission

- **android.permission.GET_PACKAGE_SIZE**
- **android.permission.PACKAGE_USAGE_STATS**
- **android.permission.REQUEST_INSTALL_PACKAGES**
- **android.permission.REQUEST_DELETE_PACKAGES**

# InstalledAppManager

## Constructor

````kotlin
InstalledAppManager(private val context : Context, private val scope: CoroutineScope)
````

## Properties

```kotlin
val appFiles: Flow<List<AppFile>>
```

All apps installed in the phone

## Public methods

```kotlin
suspend fun clean(activity: AppCompatActivity, ids: List<String>)
```

Remove apps from the phone

- **Parameter:**
  - activity:
  - ids: list package name of the apps that will be removed.

# CacheAppManager

## Constructor

```kotlin
CacheAppManager(private val context : Context, private val scope: CoroutineScope)
```

## Properties

```kotlin
val appFiles: Flow<List<AppFile>>
```

All apps have uncleaned cache

## Public methods

```kotlin
suspend fun clean(activity: AppCompatActivity, ids: List<String>)

```

Clear apps cache

- **Parameter:**
  - activity:
  - ids: list package name of the apps that cache will be cleaned.

# ApkFileManager

## Constructor

```kotlin
ApkFileManager(private val context : Context, private val scope: CoroutineScope)
```

## Properties

```kotlin
val appFiles: Flow<List<AppFile>>
```

List file apk in storage

## Public methods

```kotlin
suspend fun clean(activity: AppCompatActivity, paths: List<String>)
```

Delete apk file from storage.

- **Parameter:**
  - activity:
  - ids: list file path of the apk files that will be deleted.

# Usage Example