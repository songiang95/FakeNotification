# Permissions

- **android.permission.GET_PACKAGE_SIZE**
- **android.permission.PACKAGE_USAGE_STATS**
- **android.permission.ACTION_MANAGE_OVERLAY_PERMISSION**

```kotlin
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
            Uri.fromParts("package", baseActivity.packageName, null as String?)
        )
    } catch (e: Exception) {
        Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
    }

    context.startActivity(intent)
}

//check AccessibilitySetting permission
fun hasAccessibilitySettingPermission(appContext: Context): Boolean {
    var accessibilityEnabled = 0
    val ACCESSIBILITY_SERVICE: String =
        appContext.getPackageName() + "/" + AccessibilitySettingsService::class.java.name
    try {
        accessibilityEnabled = Settings.Secure.getInt(
            appContext.contentResolver,
            Settings.Secure.ACCESSIBILITY_ENABLED
        )
    } catch (e: SettingNotFoundException) {
        e.printStackTrace()
    }

    val mStringColonSplitter = SimpleStringSplitter(':')

    if (accessibilityEnabled == 1) {
        val settingValue = Settings.Secure.getString(
            appContext.getContentResolver(),
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        if (settingValue != null) {
            mStringColonSplitter.setString(settingValue)
            while (mStringColonSplitter.hasNext()) {
                val accessabilityService = mStringColonSplitter.next()
                if (accessabilityService.equals(ACCESSIBILITY_SERVICE, ignoreCase = true)) {
                    return true
                }
            }
        }
    }
    return false
}

//request AccessibilitySetting permission
fun requestAccessibilitySettingPermission(context: Context) {
    val intent = Intent()
    intent.action = Settings.ACTION_ACCESSIBILITY_SETTINGS
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    context.startActivity(intent)
}

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

# BoosterManager

## Constructor

```kotlin
BoosterManager(private val context : Context)
```

## Public methods

```kotlin
fun queryRunningApps(type: BoostType): Flow<List<RunningAppInfo>>
```

Get all running apps

- **Parameter:**
  - type: type of boost (ex: BoostType.CPU, BoostType.MEMORY, BoostType.BATTERY)

- **Return:**
  - Flow<List<RunningAppInfo>>:  list running app

##

```kotlin
fun cleanNormalProcess(
    activity: AppCompatActivity,
    type: BoostType,
    pkgNames: List<String>
): Channel<BoostProgress>
```

Boost apps when not have AccessibilitySetting and Manager Overlay permission

- **Parameter:**
  - activity: current activity.
  - type: type of boost (ex: BoostType.CPU, BoostType.MEMORY, BoostType.BATTERY).
  - pkgNames: list package name of apps will be boosted.

- **Return:**
  - Channel<BoostProgress>: channel to update clean progress

##

```kotlin
   fun cleanAdvancedProcess(
    activity: AppCompatActivity,
    type: BoostType,
    pkgNames: List<String>
): Channel<BoostProgress>
```

Boost apps when have AccessibilitySetting and Manager Overlay permission

- **Parameter:**
  - activity: current activity.
  - type: type of boost (ex: BoostType.CPU, BoostType.MEMORY, BoostType.BATTERY).
  - pkgNames: list package name of apps will be boosted.

- **Return:**
  - Channel<BoostProgress>: channel to update clean progress

##

```kotlin
fun cancel()
```

Cancel boost progress

# Usage Example

```kotlin
//query consume resource apps
viewModelScope.launch {
  //boostType =  BoostType.CPU or BoostType.MEMORY or BoostType.BATTERY
  boosterManager.queryRunningApps(boostType).collect { runningApps ->
    //convert to RunningAppInfoModel

  }
}

//boost without AccessibilitySetting and Manager Overlay permission
viewModelScope.launch {
  //boostType =  BoostType.CPU or BoostType.MEMORY or BoostType.BATTERY
  val boostProgress = boosterManager.cleanNormalProcess(activity, boostType, pkgNames)
  for (change in boostProgress) {
    //change: KillingProgress(pkgName="com.android.sms")

    //update boost progress
  }
}

//boost with AccessibilitySetting and Manager Overlay permission
viewModelScope.launch {
  //boostType =  BoostType.CPU or BoostType.MEMORY or BoostType.BATTERY
  val boostProgress = boosterManager.cleanAdvancedProcess(activity, boostType, pkgNames)
  for (change in boostProgress) {
    //change: KillingProgress(pkgName="com.android.sms")

    //update boost progress
  }
}

```