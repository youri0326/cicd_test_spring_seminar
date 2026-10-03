package com.example.demo; 

import static org.assertj.core.api.Assertions.*; 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc 
@ActiveProfiles("test") 
class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc; 

    @Autowired
    private UserService userService; // 検証（再取得）や初期化で本物を使用

    @Test
    @DisplayName("コンテキストが正しく読み込まれること")
    void コンテキスト読み込み確認() {
        // アプリが正常に起動し、MockMvcやUserServiceが準備されていることを確認
        assertThat(userService).isNotNull();
    }

    @Test
    @DisplayName("ユーザー登録から取得までの一連の動作が成功すること")
    void ユーザー登録の結合テスト() throws Exception {
        
        // 1. 実行：ブラウザの代わりに、MockMvcで登録URLに「POSTリクエスト」を送信する
        // ※ここではフォーム送信（param）で、山田太郎さんのデータを送ったと仮定します
        mockMvc.perform(post("/register")
                .param("name", "佐藤 次郎")
                .param("email", "jiro@example.com"))
                // ➔ Webの検証：リクエストが成功し、登録後の完了画面にリダイレクトされるかなどを確認
                .andExpect(status().is3xxRedirection()) 
                .andExpect(redirectedUrl("/"));

        // 2. 検証：本当にデータベースまで荷物が届いて保存されたか？
        // サービス（本物）を使って、直接データベースの倉庫へ「山田 太郎」を探しに行く
        // ※メールアドレスで検索するメソッド（findByEmail）があると仮定しています
        User foundUser = userService.findUserByEmail("jiro@example.com");
        
        // 3. Javaの中で答え合わせ
        assertThat(foundUser).isNotNull(); // ちゃんとDBに存在すること
        assertThat(foundUser.getId()).isNotZero(); // 自動採番された本物のIDがあること
        assertThat(foundUser.getName()).isEqualTo("佐藤 次郎"); // 名前が一致すること
    }
}