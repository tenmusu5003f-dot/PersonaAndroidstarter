gradle init \

--type java-application \

--dsl kotlin \

--test-framework junit-jupiter \

--package my.project \

--project-name my project \

--no-split-project \

--java-version 17

> Task :tasks

------------------------------------------------------------
Tasks runnable from root project 'myTutorial'
------------------------------------------------------------

Build Setup tasks
-----------------
init - Initializes a new Gradle build.
wrapper - Generates Gradle wrapper files.

Help tasks
----------
buildEnvironment - Displays all buildscript dependencies declared in root project 'myTutorial'.
...

33920 JPS
27171 グラドルデーモン
22792

plugins {
    // ここは空でもOK（ルートでは基本的に依存解決だけ）
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
