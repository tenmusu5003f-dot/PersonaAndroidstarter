<manifest package="com.persona.androidstarter" xmlns:android="http://schemas.android.com/apk/res/android">

    <application
        android:name=".PersonaApp"
        android:allowBackup="true"
        android:supportsRtl="true"
        android:theme="@style/Theme.Material3.DayNight.NoActionBar">

        <!-- 起動: スプラッシュ -->
        <activity
            android:name=".entrance.SplashActivity"
            android:exported="true"
            android:theme="@style/Theme.Splash">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- タイトル画面（OPアニメの本体） -->
        <activity
            android:name=".entrance.TitleActivity"
            android:exported="false" />

        <!-- 既存のメイン（ホームへ遷移） -->
        <activity
            android:name=".MainActivity"
            android:exported="false" />
    </application>
</manifest>
