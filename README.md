# Permissions

- **android.permission.WRITE_EXTERNAL_STORAGE**

# DuplicatePhotoManager

## Constructor

```kotlin
DuplicatePhotoManager(private val context: Context, private val scope: CoroutineScope)

```

## Properties

```kotlin
val duplicatePhotos: Flow<ResultOrProgress<List<List<Photo>>, Int>>
```

Flow of scanning duplicate photos progress and duplicate photo list

## Public methods

```kotlin
suspend fun delete(activity: AppCompatActivity, ids: List<String>)
```

Delete duplicate photos

- **Parameters:**
  - activity: current visible activity.
  - ids: list path or list uri of photo that will be deleted.

# Usage Example

```kotlin
// scan duplicate photos & get duplicate photo list
viewModelScope.launch {
    //DuplicatePhotoManager will start scanning when flow duplicatePhotos start collect the first time.
    duplicatePhotoManager.duplicatePhotos.collect { resultOrProgress ->
        if (resultOrProgress is ResultOrProgress.Result) {
            val duplicatePhotos = resultOrProgress.result
            //duplicatePhotos: [[Photo(id=1,...), Photo(id=2,...)], [Photo(id=3,...), Photo(id=4,...)],...]
            //update result
        } else {
            val progress = (resultOrProgress as ResultOrProgress.Progress).progress
            //progress: 10
            //update scanning progress
        }
    }
}

//delete duplicate photos
viewModelScope.launch {
    duplicatePhotoManager.delete(activity, ids)
}
```