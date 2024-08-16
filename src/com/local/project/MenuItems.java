package com.local.project;

public enum MenuItems {
    START_NEW_GAME("Начать игру"),
    BACK_TO_GAME("Вернуться к игре"),
    EXIT_GAME("Выйти из игры"),
    SAVE_GAME("Сохранить игру"),
    GO_TO_SAVE_GAME("Загрузить иргу");

    private final String name;
    MenuItems(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
