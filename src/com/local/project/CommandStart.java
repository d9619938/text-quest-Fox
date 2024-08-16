package com.local.project;

public class CommandStart implements Command{
    Menu menu;
    public CommandStart(Menu menu){
        this.menu = menu;
    }
    @Override
    public void execute() {
        menu.startNewGame();
    }
}
