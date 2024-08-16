package com.local.project;

public class CommandBackToGame implements Command {
    Menu menu;

    public CommandBackToGame(Menu menu) {
        this.menu = menu;
    }

    public void execute() {
        menu.backToGame();
    }
}
