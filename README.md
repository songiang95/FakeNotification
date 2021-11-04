# PrivateBrowserManager

## Permission

- **android.permission.INTERNET**

## Constructor

```kotlin
PrivateBrowserManager(private val context: Context, scope: CoroutineScope)
```

## Properties

 ```kotlin
val bookmarks: Flow<List<Bookmark>>
```
Bookmark list

## Public methods

```kotlin
suspend fun addBookmark(url: String, title: String)
```
Add a bookmark
- **Parameters:**
    - url: link of bookmark. (eg: https://www.google.com, google.com)
    - title: title of bookmark.
##
```kotlin
suspend fun removeBookmark(bookmark: Bookmark)
```
Delete a bookmark
- **Parameters:**
    - bookmark: bookmark will be deleted.

```kotlin
suspend fun suggestKeyword(text: String): List<String>
```
Search for keywords suggested by Google search
- **Parameters:**
    - text: keyword to search.
- **Return:**
    - List\<String\>: list of keywords suggested by Google.

## Usage Example

```kotlin
//*** add bookmark
viewModelScope.launch {
    //valid
    bookmarkMananger.addBookmark("google.com", "google")
    bookmarkMananger.addBookmark("https://www.google.com", "google")
    
    //invalid url format 
    bookmarkMananger.addBookmark("google", "google")
}

//*** remove bookmark
viewModelScope.launch {
    //valid
    bookmarkMananger.removeBookmark(deleteBookmark)
}

//*** search keyword
viewModelScope.launch {
    //valid
    val searchResult = bookmarkMananger.suggestKeyword("thoi tiet")
    
    //invalid blank keyword
    val searchResult = bookmarkMananger.suggestKeyword("      ")
}

```
