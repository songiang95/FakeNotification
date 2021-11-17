# Permissions

## Required

- android.permission.READ_EXTERNAL_STORAGE
- android.permission.PACKAGE_USAGE_STATS (android version >= 8.0)

# Classes

## Enum Type

```kotlin
enum class Type {
    APP,
    VIDEO,
    PHOTO,
    AUDIO,
    DOCUMENT,
    OTHER_FILE
}
```

#### Note:

- OTHER_FILE is any file type that not match any file type above

## BigFile

```kotlin
data class BigFile(val id: String, val type: Type, val name: String, val size: Long, val date: Long)
```

### Properties

- id: package name for app and file path or uri for file
- type: file type
- name: app name or file name
- date (ms): datetime when file is created or app is installed
- size (byte): size of an app or file

## BigFileManager

```kotlin
class BigFileManager(private val scope: CoroutineScope, private val application: Application)
```

### Properties

```kotlin
val bigFile: Flow<ResultOrProgress<List<BigFile>, Int>>
```

Collecting all big files which includes file and app type

### Public methods

```kotlin
suspend fun clean(activity: AppCompatActivity, bigFiles: List<BigFile>)
```

Remove big files out of list big files and delete files or remove apps from device

- **Parameters**
    - activity: AppCompatActivity
    - bigFiles: list big files is selected

# Usage Example

## Scanning Screen

```kotlin
//BigFileViewModel.kt
// to update scan big file progress
val _scanningPercent = MutableLiveData<Int>()
...
fun scan() {
    coroutineScope.launch {
        bigFileManager.bigFiles.collect {
            if (it is ResultOrProgress.Progress) {
                _scanningPercent.value = it.progress
            } else {
                ...
            }
        }
    }
}
```

## BigFile Screen

```kotlin
//BigFileViewModel.kt
//to update scan big file result

val _bigFiles = MutableLiveData<List<BigFile>>()
...
fun scan() {
    coroutineScope.launch {
        bigFileManager.bigFiles.collect {
            if (it is ResultOrProgress.Progress) {
                ...
            } else {
                _bigFiles.value = (it as ResultOrProgress.Result).result
            }
        }
    }
}

```

## BigVideo Screen

```kotlin
//BigFileViewModel.kt
//to get list big video files
val _bigFiles = MutableLiveData<List<BigFile>>()
val bigVideoFiles: LiveData<List<BigFile>> = _bigFile.map { bigFiles ->
    bigFiles.filter {
        it.type == TYPE.VIDEO
    }
}
...

// to delete big video file
coroutineScope.launch {
    bigFileManager.clean(activity: AppCompatActivity, bigFiles: List< BigFile >)
}

```
