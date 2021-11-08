# Permissions

- **android.permission.GET_PACKAGE_SIZE**
- **android.permission.PACKAGE_USAGE_STATS**
- **android.permission.READ_EXTERNAL_STORAGE**

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

# JunkManager

## Constructor

```kotlin
JunkManager(private val application : Application,
private val scope: CoroutineScope)
```

## Properties

```kotlin
val ignoringList: Flow<List<JunkInfo>>
```

List junk has been ignored.

##

```kotlin
val junkInfos: Flow<ResultOrProgress<List<JunkInfo>, JunkScanningInfo>>
```

Flow of scanning junks progress or list junk was found after scan

## Public methods

```kotlin
suspend fun clean(junks: List<JunkInfo>, progress: (Int) -> Unit)
```

Clean junks and update clean progress

- **Parameters:**
  - junks: List junk will be deleted.
  - progress: function to update clean progress.

##

```kotlin
suspend fun addIgnoring(junkInfos: List<JunkInfo>)
```

Add list of junk to ignore list

- **Parameter:**
  - junkInfos: list of junk will be ignored.

##

```kotlin
suspend fun removeIgnoring(ids: List<String>) 
```

Remove list of junk from ignore list

- **Parameters:**
  - ids: list id of junks that will be removed from ignore list

# Usage Example

```kotlin
//scan & get list of junks
viewModelScope.launch {
  // JunkManager will start scan junks when start collect junkInfos flow
  junkManager.junkInfos.collect {
    if (it is ResultOrProgress.Progress) {
      val progress = it.progress
      //update scanning progress
    } else {
      val result = (it as ResultOrProgress.Result).result
      //update scan result
    }
  }
}

//get ignore junks
viewModelScope.launch {
  junkManager.ignoringList.collect { ignoreList ->
    //ignoreList: [JunkInfo(id=com.android.youtube), JunkInfo(id=/download/temp.doc),...]
  }
}

//clean junks
viewModelScope.launch {
  //junks: [JunkInfo(id=com.android.youtube), JunkInfo(id=/download/temp.doc),...]
  junkManager.clean(junks) { cleanProgress ->
    //update clean progress
  }
}

//add junks to ignore list
viewModelScope.launch {
  //junks: [JunkInfo(id=com.android.youtube), JunkInfo(id=/download/temp.doc),...]
  junkManager.addIgnoring(junks)
}

//remove junks from ignore list
viewModelScope.launch {
  //ids: ["com.android.sms", "download/temp.doc"]
  junkManager.removeIgnoring(ids)
}

```