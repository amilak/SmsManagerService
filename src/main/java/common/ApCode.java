package common;

import connection.ConnectionFactory;
import connection.ConnectionUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by rukman on 8/26/2015.
 */
public class ApCode
{

    public static int GENERATE = 0;
    public static int PROCESS = 1;
    private String code;
    private String productCode;
    private String quantity;
    private String user;
    private int status;


    public boolean validate( Connection conn )
    {
        String query = "Select * from ApCodes where code =?";
        PreparedStatement preparedStmt = null;
        ResultSet rs = null;
        try
        {
            preparedStmt = conn.prepareStatement( query );

            preparedStmt.setString( 1, code );
            rs = preparedStmt.executeQuery();
            if( rs.next() )
            {

                return true;
            }
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        finally
        {

            ConnectionUtility.close( rs );
            ConnectionUtility.close( preparedStmt );

        }
        return false;
    }

    public ApDataSet update()
    {
        ApDataSet data = new ApDataSet( false );
        String query = "update apcodes set status = ?,last_modified=now()" + "where code = ?";
        Connection conn = ConnectionFactory.getNewConnection();
        PreparedStatement preparedStmt = null;
        if( conn != null )
        {
            try
            {
                preparedStmt = conn.prepareStatement( query );
                int count = 1;

                preparedStmt.setInt( count++, this.status );
                preparedStmt.setString( count++, this.code );
                preparedStmt.execute();
                data.setSuccess( true );
                data.setInfo( "Codes Update Successfully" );


            }
            catch( SQLException e )
            {
                e.printStackTrace();
                data.setInfo( "Error in executing query" );
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
    public void load( ResultSet rs ) throws Exception
    {
        this.code = rs.getString( "code" );
        this.productCode = rs.getString( "productCode" );
        this.quantity = rs.getString( "quantity" );

    }
    public ApDataSet insert()
    {
        ApDataSet data = new ApDataSet( false );
        String query = " insert into apcodes (code,productCode,quantity, user, status, date_created,last_modified)" + " values (?, ?, ?, ?,?,now(),now())";

        Connection conn = ConnectionFactory.getNewConnection();
        // create the mysql insert preparedstatement
        PreparedStatement preparedStmt = null;
        if( conn != null )
        {
            try
            {

                if( validate( conn ) )
                {
                    data.setInfo( "Duplicate Code Found In DB" );
                    return data;
                }
                preparedStmt = conn.prepareStatement( query );

                int count = 1;
                preparedStmt.setString( count++, this.code );
                preparedStmt.setString( count++, this.productCode );
                preparedStmt.setString( count++, this.quantity );
                preparedStmt.setString( count++, this.user );
                preparedStmt.setInt( count++, this.status );

                preparedStmt.execute();
                data.setSuccess( true );
                data.setInfo( "Code Add Successfully" );

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

    public String getCode()
    {
        return code;
    }

    public void setCode( String code )
    {
        this.code = code;
    }

    public String getProductCode()
    {
        return productCode;
    }

    public void setProductCode( String productCode )
    {
        this.productCode = productCode;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public void setQuantity( String quantity )
    {
        this.quantity = quantity;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser( String user )
    {
        this.user = user;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus( int status )
    {
        this.status = status;
    }
}
