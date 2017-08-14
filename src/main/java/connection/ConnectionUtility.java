package connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by rukman on 8/1/2015.
 */
public class ConnectionUtility
{

    static public void close( Connection con )
    {
        if( con != null )
        {
            try
            {
                con.close();
            }
            catch( SQLException e )
            {
                e.printStackTrace();
            }
        }
    }


    static public void close( PreparedStatement ps )
    {
        if( ps != null )
        {
            try
            {
                ps.close();
            }
            catch( SQLException e )
            {
                e.printStackTrace();
            }
        }
    }

    static public void close( ResultSet rs )
    {
        if( rs != null )
        {
            try
            {
                rs.close();
            }
            catch( SQLException e )
            {
                e.printStackTrace();
            }
        }
    }

}
