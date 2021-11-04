# PrivateBrowserManager

## Permission

- **android.permission.INTERNET**

## Constructor

- ```PrivateBrowserManager(private val context: Context, scope: CoroutineScope)```

## Properties

- ```bookmarks: Flow<List<Bookmark>>```\
  List bookmark

## Public methods

- ```suspend addBookmark(url: String, title: String)```\
  Add bookmark
    - **Parameters:**
        - url: link of bookmark. (vd: https://www.google.com, google.com)
        - title: title of bookmark.

- ```suspend removeBookmark(bookmark: Bookmark)```\
  Delete bookmark
    - **Parameters:**
        - bookmark: bookmark will be deleted.

- ```suspend suggestKeyword(text: String): List<String>```\
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



```


