package connection;

import com.ap.SMPP.ConfigReaderSMPP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by rukman on 7/31/2015.
 */
public class ConnectionFactory
{

    private static final String URL = "DB_URL";
    private static final String USER = "DB_USER";
    private static final String PASSWORD = "DB_PWD";

    public static Connection getNewConnection()
    {

        try
        {
//            Configuration conf = new Configuration();
//
//            conf.loadConfigurations();

            String url1 = ConfigReaderSMPP.prop.get( URL );
            String user = ConfigReaderSMPP.prop.get( USER );
            String password = ConfigReaderSMPP.prop.get( PASSWORD );

            Connection conn = DriverManager.getConnection( url1, user, password );
            if( conn != null )
            {

            /*PreparedStatement ps = conn1.prepareStatement("select * from city LIMIT 0 , 100");
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                System.out.println(rs.getString("Name"));
            }*/
                System.out.println( "Connected to the database" );
                return conn;
            }


        }
        catch( Exception e )
        {

            e.printStackTrace();
        }
        return null;

    }
}
