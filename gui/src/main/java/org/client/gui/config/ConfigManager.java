package org.client.gui.config;

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigManager {

    private Map<String, String> map = new HashMap<>();

    private String configPath = "config.properties";
    private String userPreferencePath = "userPreference.json";

    private UserPreference userPreference;
    private Properties properties;


    public void loadConfig() throws IOException {
        File file = new File(DataManager.getDataPath(configPath));
        if (file.exists()){
            InputStream inputStream = new FileInputStream(file);
            properties = new Properties();
            properties.load(inputStream);
            inputStream.close();
        }

        File jsonFile = new File(DataManager.getDataPath(userPreferencePath));
        if (jsonFile.exists()){
            InputStream jsonInputStream = new FileInputStream(jsonFile);
            userPreference = JSON.parseObject(jsonInputStream, UserPreference.class);
            jsonInputStream.close();
        }
    }

    public UserPreference getUserPreference() {
        return userPreference;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setUserPreference(UserPreference userPreference) {
        this.userPreference = userPreference;
        try{
            File file = new File(DataManager.getDataPath(userPreferencePath));
            if (file.exists()){
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            writer.write(JSON.toJSONString(userPreference));
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }



    }
}

