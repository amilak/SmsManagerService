package common;

import connection.ConnectionFactory;
import connection.ConnectionUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rukman on 9/8/2015.
 */
public class MessageQueue
{
    public  static final  String STATUS_PENDING = "P";
    public  static final  String STATUS_SEND = "S";
    public  static final  String STATUS_FAILED = "F";
    private String phoneNumber;
    private String message;
    private String status;
    private int messageId;



    public ApDataSet insert()
    {
        ApDataSet data = new ApDataSet( false );
        String query = " insert into messagequeue (phoneNumber,message,status,createDate,sendDate) values (?, ?, ?,now(),now())";

        Connection conn = ConnectionFactory.getNewConnection();
        PreparedStatement preparedStmt = null;
        if( conn != null )
        {
            try
            {
                preparedStmt = conn.prepareStatement( query );


                int count = 1;
                preparedStmt.setString( count++, this.phoneNumber );
                preparedStmt.setString( count++, this.message );
                preparedStmt.setString( count++, this.status );


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

    public ApDataSet update()
    {
        ApDataSet data = new ApDataSet( false );
        String query = " update  messagequeue set status = 'S', senddate = now() where message_id = ? ";

        Connection conn = ConnectionFactory.getNewConnection();
        PreparedStatement preparedStmt = null;
        if( conn != null )
        {
            try
            {
                preparedStmt = conn.prepareStatement( query );


                int count = 1;
                preparedStmt.setInt( count++, this.messageId );

                preparedStmt.execute();
                data.setSuccess( true );
                data.setInfo( "Data Updated Successfully" );

            }
            catch( SQLException e )
            {
                data.setInfo( "Error in updating query" );
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

    public String getMessage()
    {
        return message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus( String status )
    {
        this.status = status;
    }

    public int getMessageId()
    {
        return messageId;
    }

    public void setMessageId(int messageId)
    {
        this.messageId = messageId;
    }

    public void load(ResultSet rs) throws SQLException
    {
        this.setMessage(rs.getString("message"));
        this.setPhoneNumber(rs.getString("phoneNumber"));
        this.setStatus(rs.getString("status"));
        this.setMessageId(rs.getInt("message_id"));

    }
}
