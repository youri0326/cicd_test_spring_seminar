package com.example.demo.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repo; // 依存先をモック（身代わり）にする

    @InjectMocks
    private UserService service; // モックを注入した状態でテスト対象を初期化

    // =========================
    // 正常系
    // =========================

    @Test
    void ユーザー登録が成功する() {
        // 1. 実行
        service.registerUser("jiro suzuki", "jiro@example.com");

        // 2. 検証：リポジトリのsaveメソッドが「1回」呼ばれたかを確認する
        // 第3章のようにリストのサイズを数える必要はありません
        verify(repo, times(1)).save(any(User.class));
    }

    @Test
    void ユーザー取得ができる() {
        // 1. 前準備：1番を検索したらこのUserを返す、というルールを決める
        User mockUser = new User(1, "Taro Yamada", "taro@example.com");
        when(repo.findById(1)).thenReturn(Optional.of(mockUser));

        // 2. 実行
        User user = service.findUser(1);

        // 3. 検証
        assertEquals("Taro Yamada", user.getName());
        assertEquals("taro@example.com", user.getEmail());
    }

    // =========================
    // 異常系（ここ重要）
    // =========================

    @Test
    void 名前が空の場合エラー() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.registerUser("", "test@example.com");
        });
        // 異常系では「リポジトリの保存処理が一度も呼ばれていないこと」まで確認するのがプロ
        verify(repo, never()).save(any(User.class));
    }

    @Test
    void 名前がnullの場合エラー() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.registerUser(null, "test@example.com");
        });
        verify(repo, never()).save(any(User.class));
    }

    @Test
    void メール形式不正でエラー() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.registerUser("Taro", "aaa");
        });
        verify(repo, never()).save(any(User.class));
    }

    // =========================
    // 境界値
    // =========================

    @Test
    void 名前が20文字ならOK() {
        service.registerUser("abcdefghijklmnopqrst", "test@example.com");
        // 20文字なら保存処理が走るはず
        verify(repo, times(1)).save(any(User.class));
    }

    @Test
    void 名前が21文字ならエラー() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.registerUser("abcdefghijklmnopqrstu", "test@example.com");
        });
        // 21文字なら保存される前に例外が出るので、saveは呼ばれない
        verify(repo, never()).save(any(User.class));
    }

    @Test
    void 存在しないユーザー取得でエラー() {
        // 1. 前準備：999番を検索したら「空(Optional.empty)」を返す
        when(repo.findById(999)).thenReturn(Optional.empty());

        // 2. 検証
        assertThrows(IllegalArgumentException.class, () -> {
            service.findUser(999);
        });
    }
}