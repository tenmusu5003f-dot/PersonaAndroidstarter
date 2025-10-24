- name: Gradle に実行権限を与える
  run: chmod +x gradlew
  working-directory: gradlew

- name: Gradle でビルドする
  run: ./gradlew build
  working-directory: gradlew
