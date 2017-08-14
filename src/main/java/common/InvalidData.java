package common;

import connection.ConnectionFactory;
import connection.ConnectionUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by rukman on 9/8/2015.
 */
public class InvalidData
{
    public  static final  String INVALID_CUSTOMER = "P";
    public  static final  String INVALID_CODE = "C";
    private String phoneNumber;
    private String code;
    private String type;
    private long id;

    public ApDataSet insert()
    {
        ApDataSet data = new ApDataSet( false );
        String query = " insert into invaliddata (phoneNumber,code,type,date)" + " values (?, ?, ?,now())";

        Connection conn = ConnectionFactory.getNewConnection();
        PreparedStatement preparedStmt = null;
        if( conn != null )
        {
            try
            {
                preparedStmt = conn.prepareStatement( query );


                int count = 1;
                preparedStmt.setString( count++, this.phoneNumber );
                preparedStmt.setString( count++, this.code );
                preparedStmt.setString( count++, this.type );


                preparedStmt.execute();
                data.setSuccess( true );
                data.setInfo( "Data Add Successfully" );

            }
            catch( SQLException e )
            {
                data.setInfo( "Error in executing query" );
                e.printStackTrace();
            }
            finally
            {

                ConnectionUtility.close( preparedStmt );
                ConnectionUtility.close( conn );

            }
        }
        else
        {
            data.setInfo( "Error in connecting DB" );
        }

        return data;
    }


    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber( String phoneNumber )
    {
        this.phoneNumber = phoneNumber;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode( String code )
    {
        this.code = code;
    }

    public long getId()
    {
        return id;
    }

    public void setId( long id )
    {
        this.id = id;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }
}
