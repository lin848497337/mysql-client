package org.client.gui.config;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.client.datasources.serial.UserData;

import java.io.File;
import java.io.IOException;

public class DataManager {

    private String userDataPath = "user.data";

    public UserData loadData(){
        File file = new File(userDataPath);
        if (file.exists()){
            try {
                String data = FileUtils.readFileToString(file, "utf8");
                UserData userData = JSON.parseObject(data, UserData.class);
                return userData;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void saveData(UserData userData) throws IOException {
        FileUtils.writeStringToFile(new File(userDataPath), JSON.toJSONString(userData), "utf8");
    }
}
