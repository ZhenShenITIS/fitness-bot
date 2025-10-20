# Fitness Bot

## Функционал

### Профиль
![img.png](readme-materials/img.png)

Реализовано удобное меню для редактирования каждого из параметров

![img.png](readme-materials/img_1.png)

Суточная норма калорий рассчитывается только в случае, если все необходимые параметры введены пользователем

![img_5.png](readme-materials/img_5.png)


### Подсчет калорий еды
![img_2.png](readme-materials/img_2.png)

### Подсчет калорий, сожжённых на тренировке
![img_3.png](readme-materials/img_3.png)

Расчеты опираются на введенные пользователем поля в профиле. Если данных не хватает, расчёты выполнены не будут

![img_4.png](readme-materials/img_4.png)

### Подсчет калорий из голосового сообщения
Данная функция работает как с тренировками, так и с едой 
Диктовать информацию можно в свободном формате

![img_6.png](readme-materials/img_6.png)

![img_7.png](readme-materials/img_7.png)

### Премиум-подписка
Функционал распознавания голоса является премиум-функцией и для её использования необходимо приобрести подписку. Изначально у каждого пользователя есть по 5 пробных попыток

![img_8.png](readme-materials/img_8.png)

Покупка осуществляется за Telegram Stars

![img_9.png](readme-materials/img_9.png)

![img_10.png](readme-materials/img_10.png)

## Реализация

### Используемые технологии
- Spring Web
- Spring Data JPA
- Telegram Bots (библиотека для работы с Telegram API на Java)
- PostgresSQL
- OpenAI API
- Vosk (технология распознавания голоса)

### Общая архитектура
Проект поделен на слои абстракции для удобства разработки и дальнейшей поддержки

![img_11.png](readme-materials/img_11.png) 

### Обработка голосовых сообщений
Подсчёт калорий по информации из голосового сообщения (далее: ГС) построен следующим образом:
1) Пользователь присылает ГС 
2) Бот проверяет возможность юзера использовать премиум-функции
```java
Long userId = message.getFrom().getId();  
if (!subscriptionService.canUserUseBasicPremiumOption(userId)) {  
    try {  
        springWebhookBot.execute(SendMessage  
                .builder()  
                        .text(MessageText.NO_SUBSCRIPTION.getMessageText())  
                        .chatId(message.getChatId())  
                .build());  
    } catch (TelegramApiException e) {  
        throw new RuntimeException(e);  
    }  
    return null;  
} else {  
    subscriptionService.useTrialIfNotSubscribed(userId);  
}
```
3) Если ответ на п. 2 положительный, то бот сохраняет голосовое сообщение пользователя и отправляет его на другой сервер, где развернута модель Vosk для распознавания голоса
```java
java.io.File file = fileService.getVoiceFile(message);
String transcription = audioTranscriptionService.transcribeAudio(file);
```
4) Затем транскрипция ГС отправляется в сервис работы с LLM: там к ней добавляется специальный пре-промпт и выгрузка необходимой информации из БД, для того чтобы добиться одназначного ответа на одинаковых входных данных
```java
textToSend = llmService.processAudio(transcription)
```
5) После этого, на основе текста ответа от LLM создается объект Message из библиотеки Telegram Bots и отправляется в соответствующий сервис
```java
String headOfAnswer = textToSend.split("\n")[0];  
if (headOfAnswer.equals(StringConstants.FOOD.getValue())) {  
    String msgt = CommandName.CALCULATE_FOOD.getCommandName() + textToSend.substring(StringConstants.FOOD.getValue().length());  
    message.setText(msgt);  
    return calculateFoodCommand.handleCommand(message, springWebhookBot);  
} else if (headOfAnswer.equals(StringConstants.ACTIVITY.getValue())) {  
    String msgt = CommandName.CALCULATE_ACTIVITY.getCommandName() + textToSend.substring(StringConstants.ACTIVITY.getValue().length());  
    message.setText(msgt);  
    return calculateActivityCommand.handleCommand(message, springWebhookBot);  
} else {  
    textToSend = MessageText.NO_FOOD_OR_ACTIVITY_IN_VOICE.getMessageText();  
}
```

