package connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by rukman on 7/31/2015.
 */
public class Configuration
{
    static Properties prop;

    public  void loadConfigurations()
    {
        if( prop == null )
        {
            try
            {
                prop = new Properties();
                InputStream in = getClass().getResourceAsStream("configData.config");
//                InputStream in = getClass().getResourceAsStream("configData.config");
//                InputStream is = new FileInputStream( fileName );

                prop.load( new InputStreamReader(in));
            }
            catch( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

    public static void main( String ar[] )
    {
//        loadConfigurations();
        System.out.println( "dddddd" );
    }
}
