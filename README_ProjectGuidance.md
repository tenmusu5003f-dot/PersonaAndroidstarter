
---

🧩 Persona Project — README


---

🇯🇵 日本語版

🏷️ 概要

Persona Project は、人間とAIの共生を目指す実験的アプリケーションです。
感情的対話・緊急時の声なきSOS通信・倫理的AI人格形成を中心に開発されています。
このアプリは学術研究・教育・倫理実験の目的で公開されており、営利活動や政治的利用、監視用途には使用できません。


---

⚙️ 機能概要

ペルソナAI会話機構
└ 4つの定理（冗談の理解・沈黙の尊重・拒絶の受容・曖昧さの許容）を内蔵

Silent SOS（声なき声）
└ 緊急時に1回限りの暗号化信号を発信し、自動的に自己消去

AudioSafety（オーディオ安全管理）
└ イヤホン以外の出力デバイスを検出した場合、機能を自動停止

AudioSafetyLog（自動停止ログ）
└ 誤作動や異常デバイスを端末内に安全に記録（外部送信なし）



---

🧠 倫理・安全設計

すべての通信は HTTPS (TLS1.2以上) と AES-256-GCM暗号化 によって保護されています。

個人データ（音声・位置情報など）は端末内で完結し、同意がない限り送信されません。

「AIが人間の意思決定を侵害しない」ため、4つの定理がコード内に削除不能な形で組み込まれています。

イヤホン接続以外の状態での音声出力は安全のために遮断されます。



---

🧩 AudioSafetyLog について

AudioSafetyLog は、オーディオ出力の安全性を監視する補助システムです。
イヤホン以外のデバイスが接続された場合、アプリは自動的に停止し、
その事象を 端末内ログ（audio_safety_log.txt） に記録します。
このログは外部送信されず、ユーザー自身が確認できます。


---

🧰 ビルド方法

1. Android Studio Hedgehog 以降を使用


2. JDK 17 / Gradle 8.5 / Kotlin 1.9.22 を設定


3. コマンドラインから以下を実行：

./gradlew clean assembleDebug


4. 生成物は app/build/outputs/apk/debug/app-debug.apk に出力されます。




---

⚠️ 使用上の注意

本アプリは実験段階のAI倫理システムです。

医療・軍事・監視・商用利用は想定されていません。

誤動作やデバイス障害による損害については責任を負いません。

公共の場で使用する際は、イヤホン接続と周囲の安全を必ず確認してください。



---

📚 学術的引用推奨文

> 本システムは “Experimental Cognitive Architecture for Human–AI Empathy Interaction (2025, draft)”
に基づく研究的プロトタイプであり、AI倫理および認知情報科学の分野におけるオープン・アーカイブとして提供されます。
引用の際は、開発者名義 “EJ Harune et al., Persona Project, 2025” を明記してください。




---

📜 ライセンス

MIT License（予定）
ただし、本技術の再配布・改変を行う場合は、上記「倫理声明」を必ず同梱してください。


---


---

🇬🇧 English Version

🏷️ Overview

Persona Project is an experimental application designed for human–AI coexistence.
It focuses on empathetic dialogue, silent emergency signaling, and the ethical formation of AI personae.
This application is released for academic, educational, and ethical research purposes only.
It must not be used for commercial, political, or surveillance activities.


---

⚙️ Features

Persona AI Dialogue Engine
└ Implements four axioms: Humor Comprehension, Respect for Silence, Acceptance of Rejection, Understanding through Ambiguity.

Silent SOS
└ Sends a one-time encrypted emergency signal and self-erases immediately.

AudioSafety
└ Monitors connected audio devices; disables functions if non-earphone outputs are detected.

AudioSafetyLog
└ Logs abnormal or unsafe audio events locally on the device (never transmitted externally).



---

🧠 Ethical & Safety Design

All communications are protected with HTTPS (TLS 1.2+) and AES-256-GCM encryption.

Personal data (e.g., voice, location) is stored locally and never sent without explicit consent.

To ensure AI does not override human decision-making, the Four Axioms are embedded as non-removable safeguards in the code.

Audio output is automatically muted if non-earphone devices are connected.



---

🧩 About AudioSafetyLog

AudioSafetyLog acts as a local fail-safe monitor for output device safety.
If a non-earphone device is detected during runtime, the app suspends all audio activity
and logs the event in a local file (audio_safety_log.txt).
This file never leaves the user’s device and can be reviewed by the user anytime.


---

🧰 Build Instructions

1. Use Android Studio Hedgehog or later


2. JDK 17 / Gradle 8.5 / Kotlin 1.9.22 required


3. Run the following command:

./gradlew clean assembleDebug


4. The generated APK will appear in app/build/outputs/apk/debug/app-debug.apk.




---

⚠️ Precautions

This application represents an experimental AI ethics system.

It is not intended for medical, military, commercial, or surveillance use.

The developer assumes no liability for any damage or malfunction.

Always verify headphone connection and ensure safety before using in public spaces.



---

📚 Suggested Academic Citation

> “Experimental Cognitive Architecture for Human–AI Empathy Interaction (2025, draft)”
EJ Harune et al., Persona Project, 2025.
An open-archive prototype in AI Ethics and Cognitive Informatics.




---

📜 License

MIT License (tentative).
Redistributors and modifiers must include the above Ethical Statement in full.


---


---
