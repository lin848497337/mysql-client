package org.client.bootstrap;

import org.client.datasources.MultiDataSourcesManager;
import org.client.gui.controller.UIController;

import java.io.IOException;

public class Bootstrap {

    public static void main(String[] args) throws ClassNotFoundException, IOException {

        MultiDataSourcesManager.getInstance().init();
        UIController UIController = new UIController();

        UIController.loadConfig();
        UIController.initUI();
        UIController.afterInit();
    }
}
