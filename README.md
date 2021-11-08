# Permissions

- **android.permission.GET_PACKAGE_SIZE**
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

Boost apps when not have Accessibility permission

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

Boost apps when have Accessibility permission

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

//boost without Accessibility permission
viewModelScope.launch {
  //boostType =  BoostType.CPU or BoostType.MEMORY or BoostType.BATTERY
  val boostProgress = boosterManager.cleanNormalProcess(activity, boostType, pkgNames)
  for (change in boostProgress) {
    //change: KillingProgress(pkgName="com.android.sms")

    //update boost progress
  }
}

//boost with Accessibility permission
viewModelScope.launch {
  //boostType =  BoostType.CPU or BoostType.MEMORY or BoostType.BATTERY
  val boostProgress = boosterManager.cleanAdvancedProcess(activity, boostType, pkgNames)
  for (change in boostProgress) {
    //change: KillingProgress(pkgName="com.android.sms")

    //update boost progress
  }
}

//cancel boost progress
boosterManager.cancel()

```