package com.example.FishBot.model;

import com.example.FishBot.config.BotConfig;
import com.example.FishBot.service.FishingPlaceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final FishingPlaceService fishingPlaceService;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if (messageText.equals("/start")) {
                startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            } else if (messageText.equals("/place")) {
                handlePlaceCommand(chatId);
            } else {
                sendMessage(chatId, "Введите корректную команду");
            }
        }
    }


    private void startCommandReceived(Long chatId, String name) {
        String answer = "Здравствуйте, используйте команду /place для получения случайного рыболовного места.";
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void handleLocationCommand(long chatId, String messageText) {
        try {
            String[] parts = messageText.split(" ");
            if (parts.length == 3) {
                float latitude = Float.parseFloat(parts[1]);
                float longitude = Float.parseFloat(parts[2]);

                sendLocation(chatId, latitude, longitude);
            } else {
                sendMessage(chatId, "Введите команду в формате: /location <широта> <долгота>");
            }
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Неверный формат координат. Попробуйте снова.");
        }
    }
    private void handlePlaceCommand(long chatId) {
        FishingPlace place = fishingPlaceService.getRandomFishingPlace();
        sendPlaceInfo(chatId, place);
    }

    private void sendLocation(long chatId, double latitude, double longitude) {
        SendLocation locationMessage = new SendLocation();
        locationMessage.setChatId(chatId);
        locationMessage.setLatitude(latitude);
        locationMessage.setLongitude(longitude);
        try {
            execute(locationMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void sendPlaceInfo(long chatId, FishingPlace place) {
        sendMessage(chatId, "Название: " + place.getName() + "\nОписание: " + place.getDescription() + "\nКоординаты: " + place.getCoordinates());

        String[] coords = place.getCoordinates().split(",");
        double latitude = Double.parseDouble(coords[0].trim());
        double longitude = Double.parseDouble(coords[1].trim());

        sendLocation(chatId, latitude, longitude);
    }

}