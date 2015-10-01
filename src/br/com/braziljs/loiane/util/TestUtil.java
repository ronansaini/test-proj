package br.com.braziljs.loiane.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class TestUtil {

	private static final String MIFS_PROP = "/Users/mohit/Google Drive/Spring/Spring Projects/ext4-crud-mvc/WebContent/WEB-INF/spring/database.properties";

	private static Properties mifsProperties;
	private static Properties prop;
	
	private static synchronized void loadMifsProperties() {
		final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
			mifsProperties = new Properties();
			prop = new Properties();
		try {
		//	mifsProperties.load(contextClassLoader.getResourceAsStream(MIFS_PROP));
			prop.load(new FileInputStream("/Users/mohit/Google Drive/Spring/Spring Projects/ext4-crud-mvc/WebContent/WEB-INF/spring/database.properties"));
		} catch (final IOException e) {
			System.out.println("Error loading the database.properties file" + e);
			throw new RuntimeException(e);
		}
	}
	
    public static String getMIFSProperty(final String property) {
        if (mifsProperties == null) {
            try {
                loadMifsProperties();
            } catch (final Exception e) {
            	System.out.println("Error loading the properties file: " + MIFS_PROP + e);
                return "";
            }
        }
       
        System.out.println("Getting property : " + property);

        final String p = prop.getProperty(property); //mifsProperties.getProperty(property); //prop.getProperty(property);//
        if (StringUtils.isBlank(p)) {
            return "";
        }
        return p.trim();
    }
    
    
}
