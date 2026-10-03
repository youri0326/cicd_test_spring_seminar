package com.example.demo.repository;

import com.example.demo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest // JPA関連のコンポーネントのみを起動する軽量なテスト
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 保存したユーザーがIDで検索できること() {
        System.out.println("##### UserRepositoryTest: 保存と検索のテスト #####");

        // 1. 準備：テスト用データの作成
        User user = new User();
        user.setName("山田 太郎");
        user.setEmail("taro@example.com");

        // 2. 実行：EntityManager経由で保存（IDが自動採番される）
        User savedUser = entityManager.persistFlushFind(user);

        // 3. 検証：Repository経由でID検索し、中身が一致するか
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("山田 太郎");
        assertThat(foundUser.get().getEmail()).isEqualTo("taro@example.com");
    }
}