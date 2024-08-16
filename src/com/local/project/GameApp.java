package com.local.project;

public class GameApp {
    public static void main(String[] args) {
        Game game = new Game();
        game.go(game);
    }
}