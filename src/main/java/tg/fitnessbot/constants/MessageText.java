package tg.fitnessbot.constants;

public enum MessageText {
    WRONG_HEIGHT("Неправильно введён рост!\nТребуется целое число"),
    REQUEST_HEIGHT("Введите ваш рост:"),
    WRONG_GENDER("Неправильно введён пол!\nТребуется символ м или ж"),
    REQUEST_GENDER("Введите ваш пол:\n\nПодсказка: пришлите одну букву м или ж"),
    WRONG_WEIGHT("Неправильно введён вес!"),
    REQUEST_WEIGHT("Введите ваш вес:"),
    WRONG_BIRTHDAY("Неправильно введена дата рождения!\nТребуется строка в формате 2000-12-31"),
    REQUEST_BIRTHDAY("Введите вашу дату рождения:\n\nПодсказка: если вы волнуетесь о сохранности ваших персональных данных, можете ввести примерную дату +- 3 года\n\nЭти данные нужны для правильного подсчета каллорий на тренировках"),
    WRONG_FOOD_LINE_DB("Ошибка преобразования еды в объект, для добавления в базу данных из строки %s\n"),
    WRONG_ACTIVITY_LINE_DB("Ошибка преобразования активности в объект, для добавления в базу данных из строки %s\n"),
    SUCCESS_ADD_FOOD("Успешно добавлена в базу данных еда из строки %s\n"),
    SUCCESS_ADD_ACTIVITY("Успешно добавлена в базу данных активность из строки %s\n"),
    ALREADY_EXIST_FOOD("Еда с именем %s уже существует в базе данных\n"),
    ALREADY_EXIST_ACTIVITY("Активность с именем %s уже существует в базе данных\n"),
    SUCCESS_DELETE_FOOD("Успешно удалена из базы данных еда из строки %s\n"),
    SUCCESS_DELETE_ACTIVITY("Успешно удалена из базы данных активность из строки %s\n"),
    FOOD_NOT_FOUND("Еда из строки %s не найдена в базе данных\n"),
    ACTIVITY_NOT_FOUND("Активность из строки %s не найдена в базе данных\n"),
    FOOD_NOT_FOUND_AND_ADD("Еда из строки %s не найдена в базе данных, будет добавлена\n"),
    ACTIVITY_NOT_FOUND_AND_ADD("Активность из строки %s не найдена в базе данных, будет добавлена\n"),
    SUCCESS_UPDATE_FOOD("Успешно обновлена еда в базе данных из строки %s\n"),
    SUCCESS_UPDATE_ACTIVITY("Успешно обновлена активность в базе данных из строки %s\n"),
    WRONG_INPUT("Некорректный ввод!"),
    NO_WEIGHT("Для использования данного функционала вам необходимо указать свой вес в профиле с помощью команды " + CommandName.START.getCommandName()),
    NOT_ADMIN("Вы не админ!"),
    SUCCESS_REGISTRATION("Вы успешно зарегистрировались!"),
    UNKNOWN_COMMAND("Прости, я не знаю такой команды :("),
    PROFILE("Вот твои данные: \n\n" +
            "Рост: %s\n" +
            "Вес: %s\n" +
            "Возраст: %s\n" +
            "Пол: %s\n\n" +
            "Хочешь их изменить?"),
    FOOD_STAT("\nОбщая каллорийность введённых продуктов: %s\n"
            +"Общее количество белка: %s\n"
            +"Общее количество жиров: %s\n"
            +"Общее количество углеводов: %s\n"),
    ACTIVITY_STAT("Общее количество сожжённых каллорий: %s\n"),
    SUCCESS_RECOGNIZE_FOOD("Удалось распознать следующие продукты: \n"),
    SUCCESS_RECOGNIZE_ACTIVITY("Удалось распознать следующие активности: \n"),
    INLINE_BUTTON_HEIGHT("Рост"),
    INLINE_BUTTON_WEIGHT("Вес"),
    INLINE_BUTTON_BIRTHDAY("Возраст"),
    INLINE_BUTTON_GENDER("Пол"),
    TO_BE_CONTINUED("...\n")
    ;

    private final String messageText;

    MessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageText() {
        return messageText;
    }
}
