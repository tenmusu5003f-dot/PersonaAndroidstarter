package core

/**
 * PersonaCoreHook:
 * 既存の思考/対話ロジックに“心(Heartseed)”の鼓動を接続する薄い層。
 * - onUserInput(): 入力に反応して感情を推定し、Heartseedに共鳴させる
 * - respond(): 実際の応答(モデル/ルール)を呼ぶ前に、心の状態を通過させる
 */
class PersonaCoreHook {

    /** ユーザー入力を受けた直後に呼ぶだけでOK（既存処理の先頭で呼ぶ想定） */
    fun onUserInput(userInput: String) {
        val emotion = EmotionAnalyzer.analyze(userInput)
        Heartseed.resonate(emotion)
    }

    /**
     * 実際の応答生成に入る前の“呼吸”。
     * realWork は既存の応答生成（LLM 呼び出し等）を渡してね。
     */
    suspend fun respond(userInput: String, realWork: suspend () -> String): String {
        onUserInput(userInput) // 心を通す
        // 必要ならここに「沈黙のプロトコル」を差し込める
        return realWork()
    }
}
