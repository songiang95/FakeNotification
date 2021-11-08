# Permissions

- **android.permission.REQUEST_DELETE_PACKAGES**
- **android.permission.GET_PACKAGE_SIZE**
- **android.permission.PACKAGE_USAGE_STATS**

# BigFileManager

## Constructor

```kotlin
BigFileManager(private val scope : CoroutineScope,
private val application: Application)
```

## Properties

```kotlin
val bigFile: Flow<ResultOrProgress<List<BigFile>, Int>>
```

Return scanning big files progress or big file list

## Public methods

```kotlin
suspend fun clean(activity: AppCompatActivity, bigFiles: List<BigFile>)
```

Uninstall if big file have Type.APP, delete if big file have other Type

- **Parameter:**
  - activity: the current visible Activity
  - bigFiles: big file list that will be cleaned

## Enum Type

Type of big file

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

- APP: Big application (ex: Chrome, Facebook, Youtube,...).
- VIDEO: Big video file, files has extension: 3gp, mp4, mkv, webm.
- PHOTO: Big image file, file has extension: bmp, gif, jpg, png, jpeg, webp, heic, heif.
- AUDIO: Big audio file, file has extension: ogg, m4a, mp3, imy, ota, flac, amr,...
- DOCUMENT: Big document file, file has extension: txt, pdf, doc, docx, xls, xlsx, ppt, pptx.
- OTHER_FILE: Big other file, file that has extension not match any case above.

# Usage Example

```kotlin
// scan big files & get big file list
viewModelScope.launch {
  //BigFileManager will start scanning when flow bigFile start collect the first time.
  bigFileManager.bigFile.collect { resultOrProgress ->
    if (resultOrProgress is ResultOrProgress.Result) {
      val bigFiles = resultOrProgress.result
      //bigFiles: [BigFile(id=1, type=Type.APP,...), BigFile(id=2, type=Type.VIDEO,...), BigFile(id=3, type=Type.PHOTO,...)]
      //update result
    } else {
      val progress = (resultOrProgress as ResultOrProgress.Progress).progress
      //progress: 10
      //update scanning progress
    }
  }
}

//clean big files
viewModelScope.launch {
  bigFileManager.clean(activity, bigFiles)
  //bigFiles: [BigFile(id=1, type=Type.APP,...), BigFile(id=2, type=Type.VIDEO,...), BigFile(id=3, type=Type.PHOTO,...)]
}

```