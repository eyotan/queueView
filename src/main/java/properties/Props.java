package properties;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class Props {
    public static String url;
    public static byte hourStart;
    public static byte hourEnd;

    public Props(){
        try{
            File configFile = new File("./config.properties");
            if (configFile.exists()) {
                Properties prop = new Properties();
                prop.load(new FileReader(configFile));
                url = prop.getProperty("url");
                hourStart = Byte.parseByte(prop.getProperty("hourStart"));
                hourEnd = Byte.parseByte(prop.getProperty("hourEnd"));
            }else{
                Properties props = new Properties();
                props.setProperty("url", "SET url from Queue");
                props.setProperty("hourStart", "SET Hour to START");
                props.setProperty("hourEnd", "SET Hour to END");
                FileWriter writer = new FileWriter(new File("config.properties"));
                props.store(writer, "url" + "hourStart" + "hourEnd");
                writer.close();
            }
        }catch (IOException ioe){
            Logger.getLogger(Props.class.getName()).warning("ERROR PROPERTIES FILE "+ioe);
        }
    }

}
