# Permissions

## Required

- android.permission.READ_EXTERNAL_STORAGE

# Classes

## Photo

```kotlin
data class Photo(val id: String, val name: String, val date: Long, val size: Long)
```

### Properties

- id: image file path or uri
- name: image file name without extension
- data (ms): datetime when image file is created
- size (byte): size of image file

## DuplicatePhotoManager

### Properties

```kotlin
val duplicatePhotos: Flow<ResultOrProgress<List<List<Photo>>, Int>>
```

Grouping duplicate photos and sorting photos by DESC quality in each group

#### Note: only querying images which exist in media storage of android framework

### Public methods

```kotlin
 suspend fun delete(activity: AppCompatActivity, ids: List<String>)
```

Remove photos out of duplicate photo group and from device

- **Parameters:**
    - activity: AppCompatActivity
    - ids: id are extracts form id field in list selected photos

# Usage Example

## Scanning Duplicate Photos Screen

```kotlin
//DuplicatePhotoViewModel.kt
val _scanningPercent = MutableLiveData<Int>()
...
fun scan() {
    coroutineScope.launch {
        duplicatePhotoManager.duplicatePhotos.collect {
            if (it is ResultOrProgress.Progress) {
                _scanningPercent.value = it.progress
            } else {
                ...
            }
        }
    }
}

```

## Duplicate Photos Screen

```kotlin
//DuplicatePhotoViewModel.kt
val _duplicatePhotos = MutableLiveData<List<List<Photo>>>
...
fun scan() {
    coroutineScope.launch {
        duplicatePhotoManager.duplicatePhotos.collect {
            if (it is ResultOrProgress.Progress) {
                ...
            } else {
                _duplicatePhotos.value = (it as ResultOrProgress.Result).result
            }
        }
    }
}

//remove duplicate photos
coroutineScope.launch {
    duplicatePhotoManager.delete(activity: AppCompatActivity, ids: List< String >)
}

```
