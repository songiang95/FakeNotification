# Permissions

- **android.permission.USE_FINGERPRINT**
- **android.permission.USE_BIOMETRIC**
- **android.permission.VIBRATE**
- **android.permission.SYSTEM_ALERT_WINDOW**
- **android.permission.ACTION_MANAGE_OVERLAY_PERMISSION**
- **android.permission.CAMERA**
- **android.permission.PACKAGE_USAGE_STATS**

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

//check manager overlay permission
fun hasManagerOverlayPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        true
    else
        Settings.canDrawOverlays(context)
}

//request manager overlay permission
fun requestManagerOverlayPermission(context: Context) {
    val intent = try {
        Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.fromParts("package", context.packageName, null as String?)
        )
    } catch (e: Exception) {
        Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
    }

    context.startActivity(intent)
}

//check camera permission
fun checkCameraPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

//request camera permission
fun requestCameraPermission() {
    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
}

```

# AppLockManager

## Constructor

```kotlin
AppLockManagerImpl(val context : Context)
```

## Properties

```kotlin
val suggestApps: Flow<List<AppLockInfo>>
```

List all apps can be lock with some apps suggest to lock

##

```kotlin
val appLockInfo: Flow<List<AppLockInfo>>
```

List all apps can be locked

##

```kotlin
val intruders: Flow<List<AppLockIntruder>>

```

List intruder has been captured

##

```kotlin
val settings: Flow<AppLockSetting>
```

App lock setting

## Public methods

```kotlin
suspend fun lock(pkgName: String)
```

Lock an app

- **Parameters:**
  - pkgName: package name of app will be locked.

##

```kotlin
suspend fun lock(pkgNames: List<String>)
```

Lock a list of app

- **Parameters:**
  - pkgNames: list package name of apps will be locked.

##                                                                                                                                                                                                                                                              

```kotlin
suspend fun unlock(pkgName: String)
```

Unlock an app

- **Parameters:**
  - pkgName: package name of app will be unlocked

##

```kotlin
suspend fun unlock(pkgNames: List<String>)
```

Unlock a list of app

- **Parameters:**
  - pkgName: package name of apps will be unlocked

##

```kotlin
suspend fun clearAllLockedApp()
```

All apps will be unlocked

##

```kotlin
suspend fun isLock(pkgName: String): Boolean
```

Check if the app is locked or not

- **Parameters:**
  - pkgName: package name of app to check.

##

```kotlin
fun setPassword(password: String, pattern: Boolean)
```

Set password for lock screen

- **Parameters:**
  - password: new password, password is a String of 4 number
  - pattern: set true to use lock pattern, false to use lock pin

##

```kotlin
suspend fun deleteIntruder(intruder: Intruder)
```

Delete intruder photo

- **Parameters:**
  - intruder: intruder to delete

##

```kotlin
suspend fun deleteIntruders(intruders: List<AppLockIntruder>)
```

Delete intruders photo

- **Parameters:**
  - intruders: list of intruder will be deleted

##

```kotlin
suspend fun queryIntruder(id: Int): AppLockIntruder?
```

Find intruder by id

- **Parameters:**
  - id: id of intruder

- **Return:**
  - AppLockIntruder: intruder was found in database by id, could be null

##

```kotlin
fun updateAppLockSetting(setting: AppLockSetting)
```

Change app lock setting

- **Parameters:**
  - setting: new applock setting

##

```kotlin
fun setCustomAppLockScreen(layoutId: Int)
```

- **Parameters:**
  - layoutId: custom layout id for lock screen (custom layout must have the save structure and id
    as R.layout.activity_app_lock_screen.xml in module applock)

##

```kotlin
fun hasPassword(): Boolean
```

Check password has been set or not

- **Return:**
  - Boolean: true if password has been set, false otherwise

# Usage Example

```kotlin

//get list app with suggest lock
viewModelScope.launch {
  appLockManager.suggestApps.collect { apps ->
    //convert to AppLockInfoModel
  }
}

//get apps lock
viewModelScope.launch {
  appLockManager.appLockInfo.collect { apps ->
    //convert to AppLockInfoModel
  }
}

//get intruders
viewModelScope.launch {
  appLockManager.intruders.collect { intruders ->
    //convert to IntruderModel
  }
}

//get settings
viewModelScope.launch {
  appLockManager.settings.collect {
    //update setting
  }
}

//lock an app
viewModelScope.launch {
  //pkgName: "com.android.contact"
  appLockManager.lock(pkgName)
}

//lock a list of app
viewModelScope.launch {
  //pkgNames: [com.android.sms, com.android.chrome,...]
  appLockManager.lock(pkgNames)
}


//unlock an app
viewModelScope.launch {
  //pkgName: "com.android.contact"
  appLockManager.unlock(pkgName)
}

//unlock a list of app
viewModelScope.launch {
  //pkgNames: [com.android.sms, com.android.chrome,...]
  appLockManager.unlock(pkgNames)
}

//unlock all apps
viewModelScope.launch {
  appLockManager.clearAllLockedApp()
}

//set password
//set valid password and use lock pattern
appLockManager.setPassword("1234", true)
//set valid password and use lock pin
appLockManager.setPassword("1234", false)
//set invalid password, password can only contain number
appLockManager.setPassword("123a", false)

//delete intruder
viewModelScope.launch {
  appLockManager.deleteIntruder(intruder)
}

//delete a list of intruders
viewModelScope.launch {
  appLockManager.deleteIntruders(intruders)
}

//query one intruder
viewModelScope.launch {
  val intruder = appLockManager.queryIntruder(id)
  //intruder can be null
}

```