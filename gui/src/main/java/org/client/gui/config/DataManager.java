package org.client.gui.config;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.client.datasources.serial.UserData;

import java.io.File;
import java.io.IOException;

public class DataManager {


    private static String userDataPath = "user.data";

    private static String mysqlPath = "MySQLDataGrip";


    public static String getDataPath(String fileName){
        String userHome = System.getProperty("user.home");
        String path = userHome + File.separator +  mysqlPath;
        File dir = new File(path);
        if (dir.isDirectory()){
            dir.mkdir();
        }
        return path + File.separator + fileName;
    }



    public UserData loadData(){
        File file = new File(getDataPath(userDataPath));
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
        FileUtils.writeStringToFile(new File(getDataPath(userDataPath)), JSON.toJSONString(userData), "utf8");
    }
}
