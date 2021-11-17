# Permissions

## Required

- android.permission.PACKAGE_USAGE_STATS (android version >= 8)

# Classes

## Enum BoostType

```kotlin
enum class BoostType(val accessibilityType: AccessibilityType) {
    CPU(AccessibilityType.FORCE_STOP),
    MEMORY(AccessibilityType.CLEAR_CACHE),
    BATTERY(AccessibilityType.FORCE_STOP)
}
```

Type of consuming apps

## RunningAppInfo

```kotlin
data class RunningAppInfo(val pkgName: String, val isSelected: Boolean)
```

### Properties

- pkgName: package name use to identify an app
- isSelected: true if suggest optimizing

## KillingProgress

```kotlin
data class KillingProgress(val pkgName: String) : BoostProgress
```

### Properties

- pkgName: package name use to identify an app

## BoosterManager

```kotlin
class BoosterManager(private val context: Context)
```

### Public methods

```kotlin
fun queryRunningApps(type: BoostType): Flow<List<RunningAppInfo>>
```

Find consuming apps by boost type

- **Properties:**
    - type: consuming app type

- **Return:**
    - Flow\<List\<RunningAppInfo\>\>: collection of consuming apps

##

```kotlin
    fun cleanNormalProcess(
    activity: AppCompatActivity,
    type: BoostType,
    pkgNames: List<String>
): Channel<BoostProgress>
```

Optimizing apps in silence (not show AccessibilitySetting Activity)

- **Properties:**
    - activity: AppCompatActivity
    - type: consuming app type
    - pkgNames: list package name of selected consuming apps
- **Return:**
    - Channel<BoostProgress>: Coroutine Channel use to update optimizing progress

##

```kotlin
fun cleanAdvancedProcess(
    activity: AppCompatActivity,
    type: BoostType,
    pkgNames: List<String>
): Channel<BoostProgress>
```

Optimizing apps by finding buttons in AccessibilitySetting Activity and clicked on it (opening
AccessibilitySetting Activity)

- **Properties:**
    - activity: AppCompatActivity
    - type: consuming app type
    - pkgNames: list package name of selected consuming apps
- **Return:**
    - Channel<BoostProgress>: Coroutine Channel use to update optimizing progress

##

```kotlin
fun cancel()
```

Stop optimizing and releasing resource.

#### Note: this function should be called after cleanAdvancedProcess function or lifecycle is in destroyed state(ViewModel onClear())

# Usage Example

## CPU Scanning Screen

```kotlin
//CpuBoosterViewModel.kt
val isScanning = MutableLiveData<Boolean>()
...
fun scan() {
    coroutineScope.launch {
        isScanning.value = true
        boosterManager.queryRunningApps(BoostType.CPU).collect { consumingProcesses ->
            isScanning.value = false
            ...
        }
    }
}

```

## CPU Consuming Apps Screen

```kotlin
//CpuBoosterViewModel.kt
//Show consuming apps 
val cpuItems = MutableLiveData<List<CpuItem>>()
...
fun scan() {
    coroutineScope.launch {
        isScanning.value = true
        boosterManager.queryRunningApps(BoostType.CPU).collect { consumingProcesses ->
            ...
            cpuItems.value = consumingProcesses.map {
                convertToItem(it)
            }
        }
    }
}
```

### CPU Clean Process Screen

```kotlin
//clean process normal (without AccessibilitySettingsService permission)
coroutineScope.launch {
    val progressChannel = boosterManager.cleanNormalProcess(
        activity: AppCompatActivity,
        type: BoostType,
        pkgNames: List< String >)

    for (progress in progressChannel) {
        if (progress is KillingProgress) {
            //update clean progress
        }
    }
}

//clean process advanced (with AccessibilitySettingsService permission) similar to clean normal

```

#### Note: Boost type Memory and Battery is similar to the Boost CPU above
