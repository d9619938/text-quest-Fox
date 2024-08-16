package com.local.project;

import java.io.*;
import java.util.*;

public class Game {
    private User user;
    private List<User> userList;
    private static Map<String, List<String>> textGame;
    private final File pathToUserList;
    public Game() {
        pathToUserList = new File("/Users/dmitrijbogdanov/IdeaProjects/itmo/" +
                "text-quest-Fox/src/com/local/project/save_archive/UserList.txt");
        loadUserList();
    }


    public void go(Game game) {
        user = game.registrationUser(game); // регистрация игрока
        game.setTextGame();  // загрузка квеста с вариантами ответов
        user.backToMenu(); // вызов главного меню
    }

    protected void loadUserList() {  // загрузка пользователей из файла РАБОТАЕТ ВЕРНО
        this.userList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToUserList))) {
            String str;
            while ((str = reader.readLine()) !=null) {
                if(str.isEmpty()) return;
                this.userList.add(new User(this, str));
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Ошибка доступа к файлу " + pathToUserList.getName());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Ошибка во время записи в файл " + pathToUserList.getName());
        }
    }

    protected List<User> getUserList() {
        return userList;
    }

    protected void addUserToList(User user) {
        userList.add(user);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathToUserList))) {
            for (User u : userList) {
                bufferedWriter.write(u.getLogin() + "\n");
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Ошибка во время записи в фйл " + pathToUserList.getName());
        }
    }



    private User registrationUser(Game game) {
        System.out.println("Добро пожаловать в игру \"Лисенок\"");
        System.out.printf("\nВыберите один из вариантов ответов и ведите на клавиатуре целое число от 1 до 2\n");
        System.out.println("1. войти в игру под своим логином");
        System.out.println("2. зарегистрировать нового игрока");
        Scanner scanner = new Scanner(System.in);
        int number;
        while (true) {
            if (scanner.hasNextInt()){
                number = scanner.nextInt();
                if (number < 1 || number > 2)
                    System.out.println("\nПопробуйте снова\n");
                else
                    switch (number) {
                        case 1 -> {return user = game.loadUser();}
                        case 2 -> {return user = game.registrationNewUser();}
                    }
            }
            else {
                scanner.next();
                System.out.println("Ведите на клавиатуре целое число от 1 до 2\n");
            }
        }
    }

    private User loadUser() {
        System.out.println("Количество зарегистрированных игроков: " + userList.size());
        System.out.println("Список зарегистрированных игроков: " + userList);
        if (userList.isEmpty()) {
            System.out.println("В игре не зарегистрированы игроки, Вы первый");
            return user = registrationNewUser();
        }
        System.out.println("Вход в игру под своим логином");
        userList = getUserList(); // получаем список пользователей
        System.out.println("Введите Login из списка зарегистрированных игроков выше, он должен содержать не более 10 символов");
        String login;
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while ((login = reader.readLine()) != null) {
                if (login.isEmpty()) throw new IllegalArgumentException("login не может быть пустым");
                if (login.length() > 10) throw new IllegalArgumentException("Login должен быть не более 10 символов");
                final String str = login;
                if (userList != null) {
                    boolean loginIsAlreadyRegistered = userList.stream().map(User::getLogin).anyMatch(x -> x.equals(str));
                    if (loginIsAlreadyRegistered) {
                        System.out.println("Вход выполнен");
                        printStarsSting();
                        user = new User(this, login);
                        return user;
                    } else {
                        System.out.println("Такого логина не существует, попробуйте другой");
                    }
                } else {
                    System.out.println("В игре не зарегистрированы игроки, Вы первый");
                    return user = registrationNewUser();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private User registrationNewUser() {
        System.out.println("Регистрация пользователя игры");
        userList = getUserList(); // получаем список пользователей
        System.out.println("Введите Login, он должен содержать не более 10 символов");
        String login;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while ((login = reader.readLine()) != null) {
                if (login.isEmpty()) throw new IllegalArgumentException("login не может быть пустым");
                if (login.length() > 10) throw new IllegalArgumentException("Login должен быть не более 10 символов");
                final String str = login;
                if (userList != null) {
                    boolean loginIsAlreadyRegistered = userList.stream().map(User::getLogin).noneMatch(x -> x.equals(str));
                    if (!loginIsAlreadyRegistered) {
                        System.out.println("Login занят, попробуйте другой");
                    } else {
                        setUser(new User(this, login));
                        addUserToList(user);
                        System.out.printf("Пользователь %s успешно зарегистрирован\n", user);
                        printStarsSting();
                        return user;
                    }
                } else {
                    user = new User(this, login);
                    addUserToList(user);
                    System.out.printf("Пользователь %s успешно зарегистрирован\n", user);
                    printStarsSting();
                    return user;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("ошибка чтения из консоли");
        }
        return null;
    }



    protected User getUser() {
        return user;
    }

    protected void setUser(User user) {
        this.user = user;
    }

    private void setTextGame() {
        System.out.println("\nЗагрузка игры \"Лисенок\"");
        textGame = new HashMap<>();
        textGame.put("Лисенок", Arrays.asList("Каждое утро Лисёнок просыпался, завтракал и шёл увидеться с Бельчонком. Это утро не было исключением. Лисёнок пришёл на" +
                        " их обычное место встречи, но Бельчонка там не было. Лисёнок ждал, ждал, но так и не смог дождаться своего друга. \"" +
                        "Бельчонок не пропустил еще ни одной встречи, вдруг он попал в беду.\" " +
                        "- подумал Лисёнок. Как поступить Лисенку?\n",
                "Вернуться домой",
                "Отправиться на поиски",
                "Выйти в главное меню"));
        textGame.put("Вернуться домой", Arrays.asList("Вернувшись домой, Лисёнок нашёл там Бельчонка. Оказалось, что Бельчонок пришёл на место встречи раньше и увидел там рой\n" +
                        "злобных пчел. Он поспешил предупредить об этом Лисёнка, но они разминулись. Наконец-то друзья нашли друг друга! Игра " +
                        "завершилась успехом\n",
                "Выйти в главное меню"));
        textGame.put("Отправиться на поиски", Arrays.asList(
                "Все лесные жители были заняты своими делами и не обращали внимания на Лисёнка и его проблему. Но вдруг кто нибудь видел\n" +
                        "Бельчонка... Лисёнок не знал, что ему делать. Помогите ему.\n",
                "Попытаться разузнать о Бельчонке у лесных жителей",
                "Искать Бельчонка в одиночку",
                "Выйти в главное меню"));
        textGame.put("Попытаться разузнать о Бельчонке у лесных жителей", Arrays.asList("Пока Лисёнок принимал решение, лесные жители разошлись кто куда. Остались только Сова и Волк. Но у Совы бывают проблемы\n" +
                        "с памятью, а Волк может сильно разозлиться из-за расспросов. Кого выбрать?\n",
                "Расспросить Сову",
                "Расспросить Волка",
                "Выйти в главное меню"));
        textGame.put("Искать Бельчонка в одиночку", Arrays.asList(
                "Лисёнок слишком долго плутал по лесу в поисках друга и сам не заметил, как заблудился. Теперь его самого нужно искать.\n" +
                        "Игра завершилась неудачей\n",
                "Выйти в главное меню"));
        textGame.put("Расспросить Сову", Arrays.asList(
                "Сова долго не хотела говорить, но в итоге сказала, что видела испуганного Бельчонка, бежавшего вглубь леса. Все лесные\n" +
                        "жители знают, что в глубине леса опасно, если Бельчонок там, ему срочно нужна помощь.\n",
                "Поверить Сове и отправиться вглубь леса",
                "Сове не стоит верить -> Искать Бельчонка в одиночку",
                "Выйти в главное меню"));
        textGame.put("Расспросить Волка", Arrays.asList(
                "Волк оказался вполне дружелюбным, но помочь не смог. Лишь сказал, что маленькому лисенку не стоит бродить по лесу\n" +
                        "одному. И как теперь поступить?\n",
                "Волк прав -> Вернуться домой",
                "-> Искать Бельчонка в одиночку",
                "Выйти в главное меню"));
        textGame.put("Поверить Сове и отправиться вглубь леса", Arrays.asList(
                "В глубине леса Лисёнок встретил Медвежонка. Ленивый Медвежонок был готов рассказать все, что знает, если Лисёнок\n" +
                        "принесёт ему мёда.\n",
                "Нет, потрачено слишком много времени, нужно идти дальше -> Искать Бельчонка в одиночку",
                "Нужно воспользоваться шансом и раздобыть мёд",
                "Выйти в главное меню"));
        textGame.put("Нужно воспользоваться шансом и раздобыть мёд", Arrays.asList(
                "Лисёнок быстро нашёл мёд, но вокруг летал рой злобных пчел. Лисёнок всегда боялся пчёл, но и не найти друга он тоже\n" +
                        "боялся.\n",
                "Подождать, вдруг пчёлы улетят",
                "Нужно попытаться выкрасть мёд немедленно",
                "Выйти в главное меню"));
        textGame.put("Подождать, вдруг пчёлы улетят", Arrays.asList(
                "Лисёнок подождал немного, и пчёлы разлетелись. Лисёнок без проблем набрал мёда. Вдруг он понял, что очень голоден. Что\n" +
                        "же делать?\n",
                "Поесть немного и передохнуть",
                "Скорее отнести мёд Медвежонку",
                "Выйти в главное меню"));
        textGame.put("Нужно попытаться выкрасть мёд немедленно", Arrays.asList(
                "Это была не лучшая идея. Пчёлы покусали Лисёнка, теперь ему самому нужна помощь. <b>Игра завершилась неудачей</b>\n",
                "Выйти в главное меню"));
        textGame.put("Поесть немного и передохнуть", Arrays.asList(
                "Пока Лисёнок ел, злобные пчёлы вернулись и покусали его. Лисёнку нужна помощь, он не сможет продолжить поиски. <b>Игра\n" +
                        "завершилась неудачей</b>\n",
                "Выйти в главное меню"));
        textGame.put("Скорее отнести мёд Медвежонку", Arrays.asList(
                "Довольный Медвежонок рассказал Лисёнку, что очень хорошо знает семью Белок и уверен, что Бельчонок никогда не пошёл бы в\n" +
                        "глубь леса. Он заверял Лисёнка, что Белки не попадают в неприятности, и что Совам нельзя верить, он также уговаривал\n" +
                        "Лисёнка вернуться домой.\n",
                "Медвежонок ничего не знает, нужно продолжить поиски -> Искать Бельчонка в одиночку",
                "Может быть он прав и Лисёнок просто паникует -> Вернуться домой",
                "Выйти в главное меню"));

        try {
            for (int i = 0; i < 100; i++) {
                Thread.sleep(20);
                System.out.print(".");
            }
            printFox();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            System.out.println("Ошибка во время паузы");
        }

        System.out.println("\nИгра загружена\n");

    }


    protected void setFirstParagraph() {
        user.setParagraph("Лисенок");
    }

    protected void guessGenerated(String paragraph) {
//        textGame.get(paragraph).stream().forEach(System.out::println); // через стрим не смог вывести доп. строки с номерами пунктов
        for (int i = 0; i < textGame.get(paragraph).size(); i++) {
            if (i == 0) {
                System.out.println(textGame.get(paragraph).get(i));
                System.out.println("Варианты ответов:");
            } else System.out.printf("%d. %s\n", i, textGame.get(paragraph).get(i));
        }
        int answers = textGame.get(paragraph).size() - 1;
        System.out.printf("\nВыберите один из вариантов ответов и ведите на клавиатуре целое число от 1 до %d\n", answers);
        Scanner scanner = new Scanner(System.in);
        int number;
        while (scanner.hasNextInt()) {
            number = scanner.nextInt();
            if (number < 1 || number > answers) {
                System.out.println("\nПопробуйте снова\n");
                guessGenerated(paragraph);
            } else {
                String nextMove = textGame.get(paragraph).get(number);
                printStarsSting();  // разделитель
                System.out.printf("\n* Выбран вариант ответа №%d - %s *\n", number, nextMove);
                if (nextMove.equals("Выйти в главное меню")) {
                    user.backToMenu();
                } else if (nextMove.contains("-> ")) {
                    String[] nextMoves = nextMove.split("-> ");
                    nextMove = nextMoves[1];
                    user.setParagraph(nextMove);   // запоминаем параграф для возможности возврата к игре, без сохранения
                    guessGenerated(nextMove);
                } else {
                    user.setParagraph(nextMove);   // запоминаем параграф для возможности возврата к игре, без сохранения
                    guessGenerated(nextMove);
                }
                return;
            }
        }
    }

    protected void printStarsSting() {
        for (int i = 0; i < 100; i++) {
            System.out.print("*");
            ;
        }
    }

    private void printFox() {
        System.out.println(".\n" +
                "____11________________1___________________________\n" +
                "___¶¶¶¶_____________¶¶¶¶__________________________\n" +
                "___¶¶_¶¶¶1_______1¶¶¶1¶¶__________________________\n" +
                "___¶¶__¶¶¶¶¶¶¶¶¶¶¶¶¶__¶¶__________________________\n" +
                "___¶¶_1¶¶¶¶¶¶¶¶¶¶¶¶¶1_¶¶__________________________\n" +
                "____1¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶__________________________\n" +
                "____¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶___________________________\n" +
                "____¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶___________________________\n" +
                "____¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶__________________________\n" +
                "_____¶¶¶11¶¶¶¶¶¶¶¶¶¶_¶¶¶__________________________\n" +
                "____¶¶_¶1___¶¶¶¶¶__1¶1_¶¶¶________________________\n" +
                "____¶¶__1¶¶11¶¶¶¶1¶¶___1¶¶¶1______________________\n" +
                "____¶¶_____¶¶¶¶¶¶¶1____1¶¶¶¶¶¶1___________________\n" +
                "____¶¶_______11_¶______¶¶¶¶¶¶¶¶¶__________________\n" +
                "____1¶1_______________¶¶¶¶¶¶¶¶¶¶¶¶________________\n" +
                "_____¶¶_______________¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶_____________\n" +
                "______1¶_____________¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶1___________\n" +
                "_______1¶1_______11_¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶__________\n" +
                "_________¶1_____¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶_________\n" +
                "_________¶¶__¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶1________\n" +
                "_________¶1_¶¶1¶¶¶¶__¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶________\n" +
                "_________¶1_¶¶1¶¶¶¶¶1_¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶1______\n" +
                "________¶¶¶_¶1¶¶¶¶¶_¶¶_¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶1______\n" +
                "________¶¶¶_¶_¶¶¶¶¶1_¶¶_1¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶1_____\n" +
                "________¶¶¶_¶_¶¶¶¶¶1__11_1¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶_____\n" +
                "________1¶¶_¶_¶¶¶¶¶_¶¶¶¶¶¶¶¶11¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶____\n" +
                "_______¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶1¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶____\n" +
                "_______¶¶¶¶¶1¶¶¶¶¶1¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶1_¶¶¶¶¶¶____\n" +
                "_____________________________________¶¶¶¶¶¶¶¶¶____\n" +
                "______________________¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶_____\n" +
                "___________________¶¶¶1¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶______\n" +
                "_________________¶¶1__1¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶_________\n" +
                "_________________1¶¶11¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶1____________\n" +
                "____________________1¶¶¶¶¶¶¶¶¶1___________________\n"
        );
    }

}