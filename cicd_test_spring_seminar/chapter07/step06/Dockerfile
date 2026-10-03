# -----------------------------
# Mavenによるビルド
# -----------------------------
# Maven + Java17 が入ったイメージ
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# コンテナ内部の作業ディレクトリ
WORKDIR /app

# ソースコードをコピー(ローカル環境からコンテナ環境へコピー)
COPY . .

# Spring Bootをビルド(jarファイル生成)
RUN ./mvnw package -DskipTests

# -----------------------------
# Javaの実行環境
# -----------------------------
# 軽量Javaイメージ (開発・テスト両用)
FROM eclipse-temurin:17-jdk-alpine

# コンテナ内部の作業ディレクトリ
WORKDIR /app

#Maven環境からJavaの実行環境へ関連ファイルをコピー
#(ブラウザからの確認に必要な本番用の)jarファイル、
## (テストに必要な)Maven関連ファイル(mvnw)、ソースコードなど関連ファイルを全てコピー
### これにより、ブラウザ確認とコンテナ内テストが両立できる
COPY --from=builder /app .

# Spring Boot起動
## java -jar target/*.jar(CMD ["java", "-jar", "target/*.jar"]) と直接書くと、
# Javaは律儀に「*.jarという名前のファイル」を探しに行ってしまい、
# 見つからずにエラーになります。
# これを解決するには、
# 一度シェル（sh）を介して、
# ワイルドカードを展開させてからJavaに渡す必要があります。
CMD ["sh", "-c", "java -jar target/*.jar"]