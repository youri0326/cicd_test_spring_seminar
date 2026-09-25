package com.example.demo.service;

import com.example.demo.entity.User;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();
    
    //コンストラクタで、配列(findAllメソッド)用の初期データをセット
    public UserService() {
        users.add(new User(1, "Taro Yamada", "taro@example.com"));
        users.add(new User(2, "Hanako Suzuki", "hanako@example.com"));
    }    

    // 全ユーザーの取得
    public List<User> findAll() {
        return users;
    }

    //ユーザー登録
    public User registerUser(String name, String email) { // 引数から int id を削除

        // 名前チェック
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("名前は必須です");
        }

        if (name.length() > 20) {
            throw new IllegalArgumentException("名前は20文字以内です");
        }

        // メールチェック
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("メール形式が不正です");
        }

        // --- 自動採番ロジック ---
        // 現在のリスト内の最大IDを取得して +1 する。リストが空なら 1 にする。
        int newId = users.stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0) + 1;

        User newUser = new User(newId, name, email);
        users.add(newUser);
        
        return newUser; // 登録したユーザー情報を返すようにするとテストがしやすい
    }


    // 特定のユーザー取得
    public User findUser(int id) {
        return users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが存在しません"));
    }

    public int getUserCount() {
        return users.size();
    }
}