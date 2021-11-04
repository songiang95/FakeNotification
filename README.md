## Permissions
- **android.permission.INTERNET**

# PrivateBrowserManager
## Constructor

```kotlin
PrivateBrowserManager(private val context : Context, scope: CoroutineScope)
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
    - url: link of bookmark. (ex: https://www.google.com, google.com)
    - title: title of bookmark.

##

```kotlin
suspend fun removeBookmark(bookmark: Bookmark)
```

Delete a bookmark

- **Parameters:**
    - bookmark: bookmark will be deleted.

##

```kotlin
suspend fun suggestKeyword(text: String): List<String>
```

Search for keywords suggested by Google search

- **Parameters:**
    - text: keyword to search.
- **Return:**
    - List\<String\>: list of keywords suggested by Google.

# Usage Example

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
    bookmarkMananger.removeBookmark(deleteBookmark)
}

//*** search keyword
viewModelScope.launch {
    val searchResult = bookmarkMananger.suggestKeyword("thoi tiet")
  
    //searchResult["thoi tiet hanoi", "thoi tiet saigon", "thoi tiet hom nay",...]
}

viewModelScope.launch {
    bookmarks.collect { result ->
      //result["thoi tiet hanoi", "thoi tiet saigon", "thoi tiet hom nay",...]
      
      //convert to BookmarkModel
    }
}

```
