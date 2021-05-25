package hlc.realtime.location.data.api

// 定義請求的 資料格式，格式為 JSON { "變數名稱" , "變數的值" }
data class WebRequest(
    var count: Int?=null
)