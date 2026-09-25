# STEP 1: ビルド環境 (builder)
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
# 実行環境でも使うためにここでビルドしておく
RUN ./mvnw package -DskipTests

# STEP 2: 実行環境 (開発・テスト両用)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# 【重要】ソースコード、mvnw、ビルド済みjarをすべて実行環境へコピー
# これにより、ブラウザ確認とコンテナ内テストが両立できる
COPY --from=builder /app .

# Spring Boot起動
# java -jar target/*.jar と直接書くと、
# Javaは律儀に「*.jarという名前のファイル」を探しに行ってしまい、
# 見つからずにエラーになります。
# これを解決するには、
# 一度シェル（sh）を介して、
# ワイルドカードを展開させてからJavaに渡す必要があります。
CMD ["sh", "-c", "java -jar target/*.jar"]
