package com.example.demo.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository repo;

    // Repositoryをインジェクション（これがMockitoでモック化する対象）
    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    // 全件取得（Repositoryに丸投げ）
    public List<User> findAll() {
        return repo.findAll();
    }

    // ユーザー登録（ここで前章のバリデーションを復活させる！）
    public User registerUser(String name, String email) {

        // --- バリデーション（ビジネスロジック） ---
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("名前は必須です");
        }

        if (name.length() > 20) {
            throw new IllegalArgumentException("名前は20文字以内です");
        }

        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("メール形式が不正です");
        }
        if (repo.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("このメールアドレスは既に登録されています");
        }

        // DBに保存するUserオブジェクトを作成
        // ※ IDはDB側で自動採番される想定なので一旦0などでOK
        User newUser = new User(0, name, email);
        
        // --- Repository（DB）へ保存して、結果を返す ---
        // テスト時はここをMockitoで「保存したふり」をさせます
        return repo.save(newUser);
    }
    public User findUser(int id) {
        return repo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません ID:" + id));
    }  
    public User findUserByEmail(String email) {
        return repo.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません Email:" + email));
    }    
    
}