# Permissions

## Required

- android.permission.READ_EXTERNAL_STORAGE
- android.permission.PACKAGE_USAGE_STATS (android version >= 8.0)
- android.permission.MANAGE_EXTERNAL_STORAGE (android version >= 11)

# Classes

## Enum JunkType

```kotlin
enum class JunkType {
    TYPE_APP,
    TYPE_EMPTY_FOLDER,
    TYPE_DOWNLOADED,
    TYPE_DOCUMENT,
    TYPE_OTHER
}
```

Type of junk, TYPE_APP is app cache, the rest are junk files

## JunkScanningInfo

```kotlin
data class JunkScanningInfo(
    val id: String,
    val isCacheApp: Boolean,
    val size: Long,
    val percent: Int
)
```

### Properties

- id: package name for app cache and file path or uri for junk file
- isCacheApp: true if scanning cache app, false if scanning junk files
- size (byte): total size of app cache and junk file
- percent: scanning junk progress

## JunkInfo

```kotlin
data class JunkInfo(
    val id: String,
    val name: String,
    val type: JunkType,
    val size: Long
)
```

### Properties

- id: package name for app cache and file path or uri for junk file
- name: app name for app cache and file name for junk file
- type: type of junk (app cache or file)
- size (byte): size of app cache or size of a junk file

## JunkManager

```kotlin
class JunkManager(private val application: Application, private val scope: CoroutineScope)
```

### Properties

```kotlin
val ignoringList: Flow<List<JunkInfo>>
```

Contains list junk files which are ignored by user

##

```kotlin
val junkInfos: Flow<ResultOrProgress<List<JunkInfo>, JunkScanningInfo>>
```

Contains list junk files found on device and info of scanning progress

### Public methods

```kotlin
suspend fun clean(junks: List<JunkInfo>, progress: (Int) -> Unit)
```

Clean junk files are selected

- **Parameters:**
    - junk: list junk file selected to delete
    - progress: lambda function return clean progress

##

```kotlin
suspend fun addIgnoring(junkInfos: List<JunkInfo>)
```

To ignore junk files which are moved to ignoring list

- **Parameters:**
    - junkInfos: list junk file to add to ignore list

##

```kotlin
suspend fun removeIgnoring(ids: List<String>)
```

To remove junk files out of ignoring list

- **Parameters:**
    - ids: list id of junk files to remove off the ignoring list

# Usage Example

## Scanning Screen

```kotlin
//JunkFileViewModel.kt
val _scanCachePercent = MutableLiveData<Int>()
val _scanFilePercent = MutableLiveData<Int>()
val _totalJunkSize = MutableLiveData<Long>()
...
fun scan() {
    coroutineScope.launch {
        junkManager.junkInfos.collect {
            if (it is ResultOrProgress.Progress) {
                if (it.progress.isCacheApp) {
                    _scanCacheProgress.value = it.progress.percent
                } else {
                    _scanFileProgress.value = it.progress.percent
                }
                _totalJunkSize.value = it.progress.size

            } else {
                ...
            }
        }
    }
}
```

## Junk File Result Screen

```kotlin
//JunkFileViewModel.kt
//to get junks info
val _junks = MutableLiveData<JunkInfo>()
...
fun scan() {

    coroutineScope.launch {
        junkManager.junkInfos.collect {
            if (it is ResultOrProgress.Progress) {
                ...
            } else {
                _junks.value = (it as ResultOrProgress.Result).result
            }
        }
    }
}

//to ignore junk files
coroutineScope.launch {
    junkManager.addIgnoring(junkInfos: List< JunkInfo >)
}

```

## Ignoring Screen

```kotlin
//to get list ignoring junk files
coroutineScope.launch {
    junkManager.ignoringList.collect { ignoreFiles ->
        ...
    }
}

//to remove an ignoring file or files
coroutineScope.launch {
    junkManager.removeIgnoring(ids: List< String >)
}

```

## Cleaning Junk Screen

```kotlin
//to clean files which are selected
coroutineScope.launch {
    junkManager.clean(junks: List< JunkInfo >) { cleanProgress ->
            //update cleaning progress
        }
}
```
