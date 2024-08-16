package com.local.project;

public class CommandSaveGame implements Command {
    Menu menu;
    public CommandSaveGame(Menu menu) {
        this.menu = menu;
    }
    public void execute() {
        menu.saveGame();
    }
}
