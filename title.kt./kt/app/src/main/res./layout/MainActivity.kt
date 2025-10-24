<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/root"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/black">

    <ImageView
        android:id="@+id/logo"
        android:layout_width="180dp"
        android:layout_height="180dp"
        android:layout_gravity="center"
        android:alpha="0"
        android:src="@mipmap/ic_launcher" />

    <!-- 右下に”Tap to Start” -->
    <TextView
        android:id="@+id/tap"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        android:layout_margin="24dp"
        android:text="Tap to Start"
        android:textColor="@android:color/white"
        android:alpha="0.0"
        android:textSize="16sp"/>
</FrameLayout>
