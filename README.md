# Permissions

## Encryption Scanner

- android.permission.ACCESS_COARSE_LOCATION
- android.permission.ACCESS_FINE_LOCATION

# Classes

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

## WifiIssue

```kotlin
data class WifiIssue(
    val type: ScanningType,
    val issueCode: IssueCode
)
```

### Properties

- type: Scanning type
- issueCode: issue code

## WifiScanning

```kotlin
class WifiScanning(type: ScanningType)
```

### Properties

- type: wifi security type will be scanned

## WifiScanningDone

```kotlin
class WifiScanningDone(type: ScanningType, val issue: WifiIssue?)
```

### Properties

- type: wifi security type has been scanned
- issue: wifi issue after scanned
- isSafe: Boolean true if wifi security type (ScanningType) has no issue

## WifiSecurity

```kotlin
class WifiSecurity(context: Context, val scope: CoroutineScope)
```

### Public methods

```kotlin
fun scan() = flowProgress<List<WifiIssue>, WifiScanningState>
```

Scanning malicious WIFI networks which in sequence ARP, DNS, ENCRYPTION, SSL attacking

- **Return:**
  -Flow\<ResultOrProgress\<List\<WifiIssue\>, WifiScanningState\>\>: list of wifi issue or wifi
  scanning state

##

```kotlin
static fun findCurrentWifi(context: Context): MyWifiInfo?
```

Retrieve current wifiInfo which is connected.

- **Return:**
    - MyWifiInfo: information about the current wifi connected, null if your device is not connected
      to any wifi networks

# Usage Example

## Wifi Scanning Screen

```kotlin
//WifiScannerViewModel.kt
//to get wifi scanning progress
coroutineScope.launch {
    wifiScanner.scan().collect {
        if (it is ResultOrProgress.Progress) {
            val scanningType = it.progress.type
            if (it.progress is WifiScanning) {
                //update scanning state
            } else if (it.progress is WifiScanningDone) {
                val isSafe = it.progress.isSafe
                //update scanning done state
            }
        } else {
            ...
        }
    }
}

```

## Wifi Scanning Result Screen

```kotlin
//WifiScannerViewModel.kt
//to get wifi issues
coroutineScope.launch {
    val _issues = MutableLiveData<List<WifiIssue>>()

    wifiScanner.scan().collect {
        if (it is ResultOrProgress.Progress) {
            ...
        } else {
            _issues.value = (it as ResultOrProgress.Result).result
        }
    }
}

```
