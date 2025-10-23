package ui

import core.CapturePolicy
import core.PersonaCoreHook

/**
 * UIの「カメラ/スクショ」ボタンが押されたときの振る舞い例。
 * 実際のOSスクショを止めたりはしない。“歓迎/確認/控えて”の表示だけ行う。
 */
class CaptureButtonHandler(
    private val askUser: suspend (String) -> Boolean, // 「撮っていい？」ダイアログ
    private val showToast: (String) -> Unit,
    private val showBadge: (String) -> Unit // 撮影タイム中の表示
) {
    private val core = PersonaCoreHook()

    suspend fun onPressed() {
        when (core.decideCaptureAction()) {
            CapturePolicy.Action.ALLOW -> {
                showToast("スクショ歓迎だよ。いいの撮ってね！")
            }
            CapturePolicy.Action.ALLOW_WITH_WATERMARK -> {
                showBadge("Shooting Time") // 画面に小さなバッジや演出を出す
                showToast("いま撮影タイム中！")
            }
            CapturePolicy.Action.SHOW_PROMPT -> {
                val ok = askUser("このシーン、撮ってもいい？")
                if (ok) showToast("ありがとう、どうぞ！") else showToast("今日は控えておこう。")
            }
            CapturePolicy.Action.SUGGEST_AVOID -> {
                showToast("このシーンは控えめにしてくれると嬉しいな。")
            }
        }
    }
}
