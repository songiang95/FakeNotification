

# PrivateBrowserManager

## Permission
- android.permission.INTERNET

## Constructor
- PrivateBrowserManager(private val context: Context, scope: CoroutineScope)

## Properties
- **bookmarks: Flow<List<Bookmark>>**\
Danh sách bookmark lấy từ database
## Public methods
- **addBookmark(url: String, title: String)**\
Thêm 1 bookmark vào database
  
```
Parameters:
    url: đường dẫn của bookmark. (vd: https://www.google.com)
    title: tiêu đề của bookmark.
```

- **removeBookmark(bookmark: Bookmark)**\
Xóa 1 bookmark khỏi database.
```
Parameters:
    bookmark: bookmark cần xóa.
```
- **suggestKeyword(text: String): List<String>**\
Tìm kiếm các từ khóa được gợi ý bởi Google search
  
```
Parameters:
    text: từ khóa cần tìm kiếm.
    
Return:
    List<String>: danh sách các từ khóa được gợi ý bởi Google.
```



