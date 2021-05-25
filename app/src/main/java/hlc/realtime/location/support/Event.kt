package hlc.realtime.location.support

import androidx.lifecycle.Observer

/** 建立 Event 封裝 LiveData
 * 再 Observer Event 本身，讓觀察者僅看到 Event 本身，而不會直接知道內部資料
 * 避免在載入資料/初始化時執行或重複執行 Observer 的資料
 * */
open class Event<out T>(private val content: T) {

    // 一個用來標示這個資料是否已更新 UI or 其他邏輯運算的 flag
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun getContent(): T = content
}

/**
 * 拓展使用 [Event] 時的 [Observer] 操作行為
 * 簡化了檢查 Event 的內容已改變時的方式
 * */
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}
