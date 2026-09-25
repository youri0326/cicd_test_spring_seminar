package com.example.demo.service;

import com.example.demo.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

	private UserService service = new UserService();

	// =========================
	// 正常系
	// =========================

	@Test
	void ユーザー登録が成功する() {
		// 1. 登録前の件数を取得（初期データが2件なら、beforeCountは2になる）
		int beforeCount = service.findAll().size();

		// 2. 実行
		service.registerUser("jiro suzuki", "jiro@example.com");

		// 3. 検証：登録後の件数が「登録前 + 1」になっているか
		assertEquals(beforeCount + 1, service.findAll().size());
	}

	@Test
	void ユーザー取得ができる() {
		User user = service.findUser(1);

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
	}

	@Test
	void 名前がnullの場合エラー() {
		assertThrows(IllegalArgumentException.class, () -> {
			service.registerUser(null, "test@example.com");
		});
	}

	@Test
	void メール形式不正でエラー() {
		assertThrows(IllegalArgumentException.class, () -> {
			service.registerUser("Taro", "aaa");
		});
	}

	// =========================
	// 境界値
	// =========================

	@Test
	void 名前が20文字ならOK() {
		int beforeCount = service.findAll().size();

		service.registerUser("abcdefghijklmnopqrst", "test@example.com");

		assertEquals(beforeCount + 1, service.findAll().size());
	}

	@Test
	void 名前が21文字ならエラー() {
		assertThrows(IllegalArgumentException.class, () -> {
			service.registerUser("abcdefghijklmnopqrstu", "test@example.com");
		});
	}

	@Test
	void 存在しないユーザー取得でエラー() {
		assertThrows(IllegalArgumentException.class, () -> {
			service.findUser(999);
		});
	}
}