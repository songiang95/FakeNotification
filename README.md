# Permissions

- **android.permission.INTERNET**
- **android.permission.ACCESS_WIFI_STATE**
- **android.permission.CHANGE_WIFI_STATE**
- **android.permission.ACCESS_NETWORK_STATE**
- **android.permission.ACCESS_COARSE_LOCATION**
- **android.permission.ACCESS_FINE_LOCATION**

# WifiSecurity

## Constructor

```kotlin
WifiSecurity(context: Context, val scope : CoroutineScope)
```

## Public methods

```kotlin
fun scan() = flowProgress<List<WifiIssue>, WifiScanningState>
```

- **Return:**
  - Flow<ResultOrProgress<List\<WifiIssue\>, WifiScanningState>>: flow of wifi scanning state or
    list of wifi issues

##

```kotlin
static fun findCurrentWifi(context: Context): MyWifiInfo?
```

Find current connected wifi

- **Parameters:**
  - context: any context.
- **Return:**
  - MyWifiInfo: the current connected wifi, if null then wifi is not connected yet.

## Enum ScanningType

```kotlin
enum class ScanningType {
  DNS,
  SSL,
  ARP,
  ENCRYPTION
}
```

## Enum IssueCode

```kotlin
enum class IssueCode {
  NO_INTERNET,
  ARP_DUPLICATE_MAC,
  SSL_MITM,
  DNS_INVALID,
  WIFI_NO_PASSWORD,
}
```

# Usage Example

```kotlin
//find current connected wifi. Check wifi is connected or not before scan wifi.
val myWifiInfo = Utils.findCurrentWifi(context)
val isWifiConnected = myWifiInfo != null
if (isWifiConnected) {
  //start scan wifi
} else {
  //turn on wifi
}

// scan wifi & get scan result
viewModelScope.launch {
  wifiScanner.scan().collect {
    if (it is ResultOrProgress.Progress) {
      val progress = it.progress
      //update scanning progress

      if (progress is WifiScanning) {
        //wifi is scanning
        //progress: WifiScanning(type = ScanningType.DNS)
      } else if (progress is WifiScanningDone) {
        //wifi is scan done
        //progress: WifiScanningDone(type = ScanningType.DNS, issue = null)
      }

    } else {
      val wifiIssues = (it as ResultOrProgress.Result).result
      // update wifi scan result
    }
  }
}

```