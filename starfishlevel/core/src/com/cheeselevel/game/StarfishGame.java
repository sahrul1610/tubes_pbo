package com.cheeselevel.game;

import com.badlogic.gdx.Game;

public class StarfishGame extends Game {

    public void create()
    {




        StarfishMenu cm = new StarfishMenu(this);
        setScreen( cm );

    }
}
