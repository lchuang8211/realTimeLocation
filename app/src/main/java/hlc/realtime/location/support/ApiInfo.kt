package hlc.realtime.location.support

// 放 API 連線用的相關資訊
object ApiInfo {

    const val API_URL = "http://10.0.2.2/"

    /**
     * 在HTTP之中，顯示使用者資訊當作 Header ，用於向瀏覽器提供你的使用者類型/版本/系統...
     * ex: Dalvik/2.1.0 (Linux; U; Android 11; sdk_gphone_x86 Build/RPB2.200611.012)
     * Dalvik: 模擬機 (作業系統; 加密等級; 系統語言; 裝置名稱/版本訊息;)
     * Mozilla: ？？
     * 加密等級: U > N > I
     * 版本細節:
     * */
    val userAgent = StringBuilder().append(System.getProperty("http.agent")).toString()
}
