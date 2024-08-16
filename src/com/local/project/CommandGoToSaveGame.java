package com.local.project;

public class CommandGoToSaveGame implements Command{
    Menu menu;
    public CommandGoToSaveGame(Menu menu) {
        this.menu = menu;
    }
    public void execute() {
        menu.goToSaveGame();
    }
 }
