gradle init \

--type java-application \

--dsl kotlin \

--test-framework junit-jupiter \

--package my.project \

--project-name my project \

--no-split-project \

--java-version 17

plugins {
    // ここは空でもOK（ルートでは基本的に依存解決だけ）
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
