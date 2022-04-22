# atcoder-codes-kotlin

KotlinでAtCoderを快適に解くためのProjectです。  
Mac + IntelliJで動くことを確認しています。

<img width="1197" alt="atcoder-codes-kotlin-screen" src="https://user-images.githubusercontent.com/14136581/164579413-123ce8b2-18c1-4606-b430-ac924cae99d8.png">

## できること

Gradleを用いて以下のことが行えます。

- oj(online-judge-tools)を使ったtest caseのdownload
- test caseを使ってJUnitでテストを実行できる、ソースコードの雛形の作成
- ojを使った問題の提出

## セットアップ

```shell
$ pip3 install online-judge-tools
$ oj login https://atcoder.jp/
```

## 問題のダウンロード

```shell
$ ./gradlew download -P task=abc247_a

# 複数まとめて
$ ./gradlew download -P task=abc247_a,b,c
```

## 問題の提出

```shell
$ ./gradlew submit -P task=abc247_a

# 提出前にテストを実行する
$ ./gradlew submit -P task=abc247_a -P with-test
```

## Gradle Taskのカスタマイズ

`/buildSrc` にTaskの実装と問題のテンプレートがあるので、自由にカスタマイズできます。
