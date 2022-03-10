package org.jboss.windup.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ThemeProvider {

    private static volatile ThemeProvider instance;
    private final Theme theme;

    private ThemeProvider() {
        try (InputStream input = ThemeProvider.class.getClassLoader().getResourceAsStream("windup-config.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            String brandName = prop.getProperty("distributionBrandName");
            String nameAcronym = prop.getProperty("distributionBrandNameAcronym");
            String documentationUrl = prop.getProperty("distributionBrandDocumentationUrl");
            String cliName = prop.getProperty("distributionBrandCliName");

            theme = new Theme(brandName, nameAcronym, documentationUrl, cliName);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static ThemeProvider getInstance() {
        if (instance == null) {
            synchronized (ThemeProvider.class) {
                if (instance == null) {
                    instance = new ThemeProvider();
                }
            }
        }

        return instance;
    }

    public Theme getTheme() {
        return theme;
    }
}
