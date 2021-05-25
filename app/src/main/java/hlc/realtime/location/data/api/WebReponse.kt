package hlc.realtime.location.data.api

// 定義回傳的 資料格式，格式為 JSON { "變數名稱" , "變數的值" }
data class WebReponse (
    val uid: String,
    val name: String,
    val number: Int
)