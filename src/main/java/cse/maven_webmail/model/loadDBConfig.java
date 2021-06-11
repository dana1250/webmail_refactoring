/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.model;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;


/**
 *
 * @author kim-yeonghwa
 */
public class loadDBConfig {
    private static loadDBConfig dbConfig = null;
    private static String path = "config/database.properties";
    private static String id;
    private static String pw;
    private static String url;
    private static String driver;
    
    private loadDBConfig() {
        try {
                Properties props = new Properties();
                Properties session = System.getProperties();
                props.load(this.getClass().getClassLoader().getResourceAsStream(path));
                setId(props.getProperty("id"));
                setPw(props.getProperty("pw"));
                setUrl(url = props.getProperty("url"));
                setDriver(props.getProperty("driver"));
            } catch (IOException e){
                System.out.println(e);
            }
    }
    
    public static loadDBConfig getInstance() {
        if (dbConfig == null) {
            dbConfig = new loadDBConfig();
            
        }
        return dbConfig;
    }
    
    public static String getId() {
        return id;
    }
    
    public static void setId(String id) {
        loadDBConfig.id = id;
    }

    public static String getPw() {
        return pw;
    }

    public static void setPw(String pw) {
        loadDBConfig.pw = pw;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        loadDBConfig.url = url;
    }

    public static String getDriver() {
        System.out.println(driver);
        return driver;
    }

    public static void setDriver(String driver) {
        loadDBConfig.driver = driver;
    }
    
}
