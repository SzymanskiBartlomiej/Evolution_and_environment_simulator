package com.project;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.gui.App;
import com.project.gui.Menu;
import javafx.application.Application;

import java.io.File;
import java.util.HashMap;

public class World {
    public static void main(String[] args) {
        Application.launch(Menu.class,args);
    }
}
