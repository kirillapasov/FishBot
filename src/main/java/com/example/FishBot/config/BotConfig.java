package com.example.FishBot.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

//Todo Перенести конфиг в проперти
@Configuration
@Component
@Data


public class BotConfig {
    private  String botName = "CarpFishing36Bot";
    private  String token = "7401769728:AAFordBFp-ENJ_kUSZVg0PKPt0o9eIWX65k";

    public String getBotName() {
        return botName;
    }

    public String getToken() {
        return token;
    }
}
