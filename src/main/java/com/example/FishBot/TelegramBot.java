package com.example.FishBot;

import com.example.FishBot.config.BotConfig;
import com.example.FishBot.model.FishingPlace;
import com.example.FishBot.model.UserState;
import com.example.FishBot.service.FishingPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Todo добавить возможность поиска мест по типу ловли (плат/обыч) по местоположению
@Component

public class TelegramBot extends TelegramLongPollingBot {
    private Map<Long, UserState> userStates = new HashMap<>();
    private Map<Long, FishingPlace> tempFishingPlaces = new HashMap<>();
    private final BotConfig botConfig;
    private final FishingPlaceService fishingPlaceService;

    @Autowired
    public TelegramBot(BotConfig botConfig, FishingPlaceService fishingPlaceService) {
        this.botConfig = botConfig;
        this.fishingPlaceService = fishingPlaceService;
    }
    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }


    //Todo добавить исключения
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if (messageText.equals("✅ Добавить рыболовное место")) {
                userStates.put(chatId, UserState.AWAITING_NAME);
                tempFishingPlaces.put(chatId, new FishingPlace());
                sendMessage(chatId, "Введите название рыболовного места:");
            } else if (messageText.equals("ℹ️ Информация")) {
                handleInfoCommand(chatId);
                startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            } else if (messageText.equals("🎣 Рыболовное место")) {
                handlePlaceCommand(chatId);
                startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            }  else {
                // Проверяем текущее состояние пользователя
                UserState state = userStates.getOrDefault(chatId, UserState.DEFAULT);

                switch (state) {
                    case AWAITING_NAME:
                        handleNameInput(chatId, messageText);
                        break;
                    case AWAITING_COORDINATES:
                        handleCoordinatesInput(chatId, messageText);
                        break;
                    case AWAITING_DESCRIPTION:
                        handleDescriptionInput(chatId, messageText);
                        break;
                    default:
                        sendMessage(chatId, "Используйте доступные команды.");
                }
            }
        }
    }




    private void startCommandReceived(Long chatId, String name) {
        String answer = "Используйте команды ниже, чтобы продолжить работу";
        sendMessageWithKeyboard(chatId, answer);
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
    private void sendMessageWithKeyboard(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);

        // Создаем клавиатуру
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первый ряд клавиатуры с двумя кнопками
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("🎣 Рыболовное место"));
        row.add(new KeyboardButton("ℹ️ Информация"));
        row.add(new KeyboardButton("✅ Добавить рыболовное место"));


        // Добавляем ряд в клавиатуру
        keyboard.add(row);

        // Устанавливаем клавиатуру в сообщение
        keyboardMarkup.setKeyboard(keyboard);

        // Настраиваем клавиатуру
        keyboardMarkup.setResizeKeyboard(true); // Клавиатура автоматически изменяет размер в зависимости от текста
        keyboardMarkup.setOneTimeKeyboard(true); // Клавиатура исчезает после нажатия

        sendMessage.setReplyMarkup(keyboardMarkup);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleInfoCommand(long chatId) {
        String infoMessage = "Этот бот помогает находить лучшие рыболовные места в Воронежской области." +
                " Вы можете добавлять новые рыболовные места, или получать координаты случайного места";
        sendMessage(chatId, infoMessage);
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
    private void handleAddCommand(long chatId) {
        sendMessage(chatId, "вы пытаетесь добавить рыболовное место");
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
        sendMessage(chatId, "Название: " + place.getName() + "\nОписание: "
                + place.getDescription() + "\nКоординаты: " + place.getCoordinates());

        String[] coords = place.getCoordinates().split(",");
        double latitude = Double.parseDouble(coords[0].trim());
        double longitude = Double.parseDouble(coords[1].trim());

        sendLocation(chatId, latitude, longitude);
    }

    private void handleNameInput(Long chatId, String name) {
        FishingPlace place = tempFishingPlaces.get(chatId);
        place.setName(name);
        userStates.put(chatId, UserState.AWAITING_COORDINATES);
        sendMessage(chatId, "Введите координаты (в формате Широта,Долгота):");
    }

    private void handleCoordinatesInput(Long chatId, String coordinates) {
        FishingPlace place = tempFishingPlaces.get(chatId);
        place.setCoordinates(coordinates);
        userStates.put(chatId, UserState.AWAITING_DESCRIPTION);
        sendMessage(chatId, "Введите описание рыболовного места:");
    }

    private void handleDescriptionInput(Long chatId, String description) {
        FishingPlace place = tempFishingPlaces.get(chatId);
        place.setDescription(description);

        // Сохранение места в файл
        saveFishingPlaceToFile(place);

        sendMessage(chatId, "Новое рыболовное место успешно добавлено!");
        userStates.put(chatId, UserState.DEFAULT);
        tempFishingPlaces.remove(chatId);
    }
    private void saveFishingPlaceToFile(FishingPlace place) {
        try {
            // Получаем путь к файлу
            File file = new File("fishing_places.txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            // Открываем поток для записи в файл
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Записываем данные рыболовного места
            bufferedWriter.write("Название: " + place.getName() + "\n");
            bufferedWriter.write("Координаты: " + place.getCoordinates() + "\n");
            bufferedWriter.write("Описание: " + place.getDescription() + "\n");
            bufferedWriter.write("-----------------------------\n");

            // Закрываем поток
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}