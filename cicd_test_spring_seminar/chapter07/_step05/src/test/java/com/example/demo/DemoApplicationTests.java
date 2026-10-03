package com.example.demo; 

import static org.assertj.core.api.Assertions.*; 
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

@SpringBootTest
@ActiveProfiles("test") // application-test.yml を使うために追加
class DemoApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("コンテキストが正しく読み込まれること")
    void コンテキスト読み込み確認() {
        // アプリが正常に起動し、UserServiceが準備されていることを確認
        assertThat(userService).isNotNull();
    }

    @Test
    @DisplayName("ユーザー登録から取得までの一連の動作が成功すること")
    void ユーザー登録の結合テスト() {
        // 1. 実行
        User result = userService.registerUser("山田 太郎", "taro@example.com");

        // 2. 検証
        assertThat(result.getId()).isNotZero();
        
        // 3. DBから再取得して確認
        User found = userService.findUser(result.getId());
        assertThat(found.getName()).isEqualTo("山田 太郎");
    }
}