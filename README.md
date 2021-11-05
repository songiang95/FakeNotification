# Permission

- **android.permission.GET_PACKAGE_SIZE**
- **android.permission.PACKAGE_USAGE_STATS**
- **android.permission.REQUEST_INSTALL_PACKAGES**
- **android.permission.REQUEST_DELETE_PACKAGES**
- **android.permission.READ_EXTERNAL_STORAGE**

```kotlin
//check usage stat permission
fun hasUsageStatPermission(context: Context): Boolean {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode =
      appOps.checkOpNoThrow(
        AppOpsManager.OPSTR_GET_USAGE_STATS,
        Process.myUid(),
        context.packageName
      )
    return mode == AppOpsManager.MODE_ALLOWED
  }

  return true
}

//request usage stat permission
fun requestUsageStatPermission(context: Context) {
  val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
  context.startActivity(intent)
}

```

# InstalledAppManager

## Constructor

````kotlin
InstalledAppManager(private val context : Context,
private val scope: CoroutineScope)
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
  - activity: the current visible Activity
  - ids: list package name of the apps that will be removed.

## Usage Example

```kotlin
//installed apps
viewModelScope.launch {
    installedAppManager.appFiles.collect { appFiles ->
        //convert to InstalledAppModel
    }
}

//remove apps
viewModelScope.launch {
    installedAppManager.clean(activity, ids)
    //ids: ["com.android.sms","com.android.chrome",...]
}

```

# CacheAppManager

## Constructor

```kotlin
CacheAppManager(private val context : Context,
private val scope: CoroutineScope)
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
  - activity: the current visible Activity
  - ids: list package name of the apps that cache will be cleaned.

## Usage Example

```kotlin
// apps cache
viewModelScope.launch {
    appCacheManager.appFiles.collect { appsCache ->
        //convert to AppCacheModel
    }
}

//clear cache
viewModelScope.launch {
    appCacheManager.clean(activity, ids)
    //ids: ["com.android.sms","com.android.chrome",...]
}
```

# ApkFileManager

## Constructor

```kotlin
ApkFileManager(private val context : Context,
private val scope: CoroutineScope)
```

## Properties

```kotlin
val appFiles: Flow<List<AppFile>>
```

List file apk in storage

## Public methods

```kotlin
static suspend fun installApks(activity: AppCompatActivity, paths: List<String>)
```

Install apps by apk files

- **Parameter:**
  - activity: the current visible Activity
  - ids: list path of the apk files that will be installed.

```kotlin
suspend fun clean(activity: AppCompatActivity, paths: List<String>)
```

Delete apk file from storage.

- **Parameter:**
  - activity: the current visible Activity
  - ids: list path of the apk files that will be deleted.

## Usage Example

```kotlin
// apk files
viewModelScope.launch {
  apkFileManager.appFiles.collect { apkFiles ->
    //convert to ApkFileModel
  }
}

//install apps
viewModelScope.launch {
  ApkFileUtils.installApks(activity, paths)
  //paths: ["download/youtube.apk",...]
}

//delete apk files
viewModelScope.launch {
  apkFileManager.clean(activity, paths)
  //paths: ["download/youtube.apk",...]
}

```