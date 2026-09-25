package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void ユーザー登録画面が表示される() throws Exception {
        System.out.println("#####ユーザー登録画面が表示される#####");
        mockMvc.perform(get("/register"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 正しい入力ならリダイレクトされる() throws Exception {
        // 準備：Serviceがユーザーを返すように設定（NullPointerException防止）
        System.out.println("#####正しい入力ならリダイレクトされる#####");
        User mockUser = new User();
        mockUser.setId(1);
        when(userService.registerUser(anyString(), anyString())).thenReturn(mockUser);

        mockMvc.perform(post("/register")
                .param("name", "Jiro Sato")
                .param("email", "jiro@example.com"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/")); 
    }

    @Test
    void 名前が空なら入力画面にリダイレクトされる() throws Exception {
        System.out.println("#####名前が空なら入力画面にリダイレクトされる#####");
        // 準備：Serviceが例外を投げるシミュレーション
        when(userService.registerUser("", "taro@example.com"))
                .thenThrow(new IllegalArgumentException("名前を入力してください"));

        // 修正：コントローラーが catch して "/register" へリダイレクトする実装なので、
        // isOk(200) ではなく is3xxRedirection(302) を期待する
        mockMvc.perform(post("/register")
                .param("name", "")
                .param("email", "taro@example.com"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/register"));
    }
}