package com.local.project;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private Game game;
    private String login;
    private String paragraph;
    private File pathUserFile;
    Command startGame;
    Command backToGame;
    Command exitGame;
    Command saveGame;
    Command goToSaveGame;
    Command backToMenu;

    public User(Game game, String login){
        this.game = game;
        Menu menu = new Menu(game);
        setLogin(login);
        startGame = new CommandStart(menu);
        backToGame = new CommandBackToGame(menu);
        exitGame = new CommandExitGame(menu);
        saveGame = new CommandSaveGame(menu);
        goToSaveGame = new CommandGoToSaveGame(menu);
        backToMenu = new CommandBackToMenu(menu);

    }

    protected void setLogin(String login){
            this.login = Objects.requireNonNull(login, "login не может быть null");
            pathUserFile = new File("/Users/dmitrijbogdanov/IdeaProjects/itmo/text-quest-Fox/" +
                    "src/com/local/project/save_archive/"+ login +".txt");
    }
    protected String getLogin() {
        return login;
    }
    protected String getParagraph()
    {
       return paragraph;
    }
    protected File getPathUserFile() {
        return pathUserFile;
    }
    protected void setParagraph(String paragraph) {
        if (paragraph == null) throw new IllegalArgumentException("paragraph not null");
        this.paragraph = paragraph;
    }
    protected void StartGame(){
        startGame.execute();
    }
    protected void beckToGame(){
        backToGame.execute();
    }
    protected void exitGame(){
        exitGame.execute();
    }
    protected void saveGame(){
        saveGame.execute();
    }
    protected void goToSaveGame(){ goToSaveGame.execute(); }
    protected void backToMenu(){ backToMenu.execute(); }


    @Override
    public String toString() {
        return "'" + login + "'";
    }
}
