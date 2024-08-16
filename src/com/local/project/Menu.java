package com.local.project;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Menu {
    private Game game;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    public Menu(Game game) {
        if (game == null) throw new IllegalArgumentException("menu not null");
        this.game = game;
    }

    private MenuItems MENU_ITEMS;

    public void startNewGame() {
        game.printStarsSting();
        System.out.println("\n* Начало игры *");
        game.setFirstParagraph();
        game.guessGenerated(game.getUser().getParagraph());
    }

    public void backToGame() {
        game.printStarsSting();
        System.out.println("\n* Выбран пункт - " + MenuItems.BACK_TO_GAME + " *");
        game.guessGenerated(game.getUser().getParagraph());
    }

    public void exitGame() {
        game.printStarsSting();
        System.out.println("\nВы уверены что хотите " + MenuItems.EXIT_GAME.toString().toLowerCase() + "?");
        System.out.printf("\nВыберите один из вариантов ответов и ведите на клавиатуре целое число от 1 до 2\n");
        System.out.println("1. " + MenuItems.EXIT_GAME);
        System.out.println("2. Вернуться в основное меню");
        Scanner scanner = new Scanner(System.in);
        int number;
        while (scanner.hasNextInt()) {
            number = scanner.nextInt();
            if (number < 1 || number > 2)
                System.out.println("\nПопробуйте снова\n");
            else
                switch (number) {
                    case 1 -> {
                        System.out.println("*** ИГРА ЗАВЕРШЕНА ***");
                        System.exit(1);
                    }
                    case 2 -> backToMenu();
                }
        }

    }

    public void saveGame() {
        game.printStarsSting();
        System.out.println("\n* Выбран пункт - " + MenuItems.SAVE_GAME + " *");
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(game.getUser().getPathUserFile()));
            bufferedWriter.write(game.getUser().getParagraph());
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Ошибка сохранения игры");
        }
        System.out.println("Игра сохранена под параграфом - " + game.getUser().getParagraph());
        backToMenu();
    }

    public void goToSaveGame() {
        game.printStarsSting();
        System.out.println("\n* Выбран пункт - " + MenuItems.GO_TO_SAVE_GAME + " *");

        try {
            bufferedReader = new BufferedReader(new FileReader(game.getUser().getPathUserFile()));
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                game.getUser().setParagraph(str);
            }
            System.out.println("Загружен параграф - " + game.getUser().getParagraph());
            backToMenu();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("файл не найден");
            backToMenu();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("ошибка чтения сохраненных данных игрока из файла");
        }
    }

    public void backToMenu() {
        System.out.println("\n\nПУНКТЫ ОТНОВНОГО МЕНЮ:\n");

        AtomicInteger menuLine = new AtomicInteger();
        Scanner scanner = new Scanner(System.in);
        int number;

        // новый или зарегистрированный игрок, который не успел начать игру.
        if (game.getUser().getParagraph() == null && !game.getUser().getPathUserFile().exists()) {
            Arrays.stream(MenuItems.values())
                    .filter(x -> !x.equals(MenuItems.BACK_TO_GAME) && !x.equals(MenuItems.SAVE_GAME) && !x.equals(MenuItems.GO_TO_SAVE_GAME))
                    .forEach(x -> System.out.printf("%d. %s\n", menuLine.incrementAndGet(), x));
            System.out.printf("\nВыберите один из вариантов ответов и ведите на клавиатуре целое число от 1 до %d\n", menuLine.get());
            while (scanner.hasNextInt()) {
                number = scanner.nextInt();
                if (number < 1 || number > menuLine.get()) {
                    System.out.println("\nПопробуйте снова\n");
                } else {
                    switch (number) {
                        case 1 -> game.getUser().StartGame();
                        case 2 -> game.getUser().exitGame();
                    }
                }
            }


            // игрок есть в списке игроков и есть сохраненная игра. Это первый вход после сохранения.
        }else if (game.getUser().getParagraph() == null && game.getUser().getPathUserFile().exists()) {
            Arrays.stream(MenuItems.values())
                    .filter(x ->
                            !x.equals(MenuItems.BACK_TO_GAME) &&
                                    !x.equals(MenuItems.SAVE_GAME)
                    )
                    .forEach(x -> System.out.printf("%d. %s\n", menuLine.incrementAndGet(), x));
            System.out.printf("\nВыберите один из вариантов ответов и ведите на клавиатуре целое число от 1 до %d\n", menuLine.get());
            while (true) {
                if (scanner.hasNextInt()) {
                    number = scanner.nextInt();
                    if (number < 1 || number > menuLine.get()) {
                        System.out.println("\nПопробуйте снова\n");
                    } else {
                        switch (number) {
                            case 1 -> game.getUser().StartGame();
                            case 2 -> game.getUser().exitGame();
                            case 3 -> game.getUser().goToSaveGame();
                        }
                    }
                } else {
                    scanner.next();
                    System.out.println("\nПопробуйте снова\n");
                }
            }


            // игрок есть в списке игроков и есть сохраненная игра, есть прогресс
        }else if (game.getUser().getParagraph() != null && game.getUser().getPathUserFile().exists()) {
            Arrays.stream(MenuItems.values())
                    .forEach(x -> System.out.printf("%d. %s\n", menuLine.incrementAndGet(), x));
            System.out.printf("\nВыберите один из вариантов ответов и ведите на клавиатуре целое число от 1 до %d\n", menuLine.get());
            while (true) {
                if (scanner.hasNextInt()) {
                    number = scanner.nextInt();
                    if (number < 1 || number > menuLine.get()) {
                        System.out.println("\nПопробуйте снова\n");
                    } else {
                        switch (number) {
                            case 1 -> game.getUser().StartGame();
                            case 2 -> game.getUser().beckToGame();
                            case 3 -> game.getUser().exitGame();
                            case 4 -> game.getUser().saveGame();
                            case 5 -> game.getUser().goToSaveGame();
                        }
                    }
                } else {
                    scanner.next();
                    System.out.println("\nПопробуйте снова\n");
                }
            }


            // игрок есть в списке игроков, но сохранений нет
        } else if (game.getUser().getParagraph() != null && !game.getUser().getPathUserFile().exists()) {
            Arrays.stream(MenuItems.values())
                    .filter(x-> !x.equals(MenuItems.GO_TO_SAVE_GAME))
                    .forEach(x -> System.out.printf("%d. %s\n", menuLine.incrementAndGet(), x));
            System.out.printf("\nВыберите один из вариантов ответов и ведите на клавиатуре целое число от 1 до %d\n", menuLine.get());
            while (true) {
                if (scanner.hasNextInt()) {
                    number = scanner.nextInt();
                    if (number < 1 || number > menuLine.get()) {
                        System.out.println("\nПопробуйте снова\n");
                    } else {
                        switch (number) {
                            case 1 -> game.getUser().StartGame();
                            case 2 -> game.getUser().beckToGame();
                            case 3 -> game.getUser().exitGame();
                            case 4 -> game.getUser().saveGame();
                        }
                    }
                } else {
                    scanner.next();
                    System.out.println("\nПопробуйте снова\n");
                }
            }
        }
    }
}