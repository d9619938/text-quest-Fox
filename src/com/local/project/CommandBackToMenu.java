package com.local.project;

public class CommandBackToMenu implements Command{
    Menu menu;
    public CommandBackToMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public void execute() {
        menu.backToMenu();
    }
}
