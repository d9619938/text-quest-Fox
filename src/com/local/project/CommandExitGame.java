package com.local.project;


public class CommandExitGame implements Command{
    Menu menu;

    public CommandExitGame(Menu menu) {
        this.menu = menu;
    }
    @Override
    public void execute() {
        menu.exitGame();
    }
}
