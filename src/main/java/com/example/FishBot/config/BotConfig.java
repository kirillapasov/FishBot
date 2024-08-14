package com.example.FishBot.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@Component
@Data
@PropertySource("application.properties")

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
