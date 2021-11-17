# Permissions

## Required

- android.permission.PACKAGE_USAGE_STATS (android version >= 8)

# Classes

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
    - type:

- **Return:**
    - Flow\<List\<RunningAppInfo\>\>:

# Usage Example
