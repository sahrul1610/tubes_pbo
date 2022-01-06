package com.cheeselevel.game.desktop;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.cheeselevel.game.StarfishGame;

public class StarfishLauncher
{
    public static void main (String[] args)
    {
        StarfishGame myProgram = new StarfishGame();
        LwjglApplication launcher = new LwjglApplication(
                myProgram );
    }
}

