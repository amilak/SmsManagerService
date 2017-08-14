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
public class Customer
{
    private long customerId;
    private String name;
    private String NIC;
    private String phoneNumber;
    private String territory;
    private String houseNo;
    private String street1;
    private String street2;
    private String city;
    private String dealers;


    public Customer()
    {

    }


    public String getName()
    {
        return name;
    }

    public ApDataSet insert()
    {
        ApDataSet data = new ApDataSet( false );
        String query = " insert into customers (name,NIC,phoneNumber, territory, houseNo,street1, street2,city,dealers, date_created,last_modified,created_by,modified_by)" + " values (?, ?, ?, ?,?,?, ?,  ?,?,now(),now(),?,?)";

        Connection conn = ConnectionFactory.getNewConnection();
        // create the mysql insert preparedstatementstreet1
        PreparedStatement preparedStmt = null;
        if( conn != null )
        {
            try
            {
/*
                if( validate( conn ) )
                {
                    data.setInfo( "Duplicate Code Found In DB" );
                    return data;
                }*/
                preparedStmt = conn.prepareStatement( query );


                int count = 1;
                //                preparedStmt.setLong( count++, this.customerId );
                preparedStmt.setString( count++, this.name );
                preparedStmt.setString( count++, this.NIC );
                preparedStmt.setString( count++, this.phoneNumber );
                preparedStmt.setString( count++, this.territory );
                preparedStmt.setString( count++, this.houseNo );
                preparedStmt.setString( count++, this.street1 );
                preparedStmt.setString( count++, this.street2 );
                preparedStmt.setString( count++, this.city );
                preparedStmt.setString( count++, this.dealers );
                preparedStmt.setString( count++, "SMS Processor" );
                preparedStmt.setString( count++, "SMS Processor" );

                preparedStmt.execute();
                data.setSuccess( true );
                data.setInfo( "Customer Add Successfully" );

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

    public void load( ResultSet rs ) throws Exception
    {
        this.customerId = rs.getLong( "customerId" );
        this.name = rs.getString( "name" );
        this.NIC = rs.getString( "NIC" );
        this.phoneNumber = rs.getString( "phoneNumber" );
        this.territory = rs.getString( "territory" );
        this.houseNo = rs.getString( "houseNo" );
        this.street1 = rs.getString( "street1" );
        this.street2 = rs.getString( "street2" );
        this.city = rs.getString( "city" );
        this.dealers = rs.getString( "dealers" );

    }


    public ApDataSet update()
    {
        ApDataSet data = new ApDataSet( false );
        String query = "update customers set name = ?, NIC = ?,phoneNumber=?, territory =?,houseNo=?,street2=?  ,street1=? ,city=?,dealers=?,last_modified=now(), modified_by =?" + "  where customerId = ?";
        Connection conn = ConnectionFactory.getNewConnection();
        PreparedStatement preparedStmt = null;
        if( conn != null )
        {
            try
            {
                preparedStmt = conn.prepareStatement( query );
                int count = 1;


                preparedStmt.setString( count++, this.name );
                preparedStmt.setString( count++, this.NIC );
                preparedStmt.setString( count++, this.phoneNumber );
                preparedStmt.setString( count++, this.territory );
                preparedStmt.setString( count++, this.houseNo );
                preparedStmt.setString( count++, this.street1 );
                preparedStmt.setString( count++, this.street2 );
                preparedStmt.setString( count++, this.city );
                preparedStmt.setString( count++, this.dealers );
                preparedStmt.setString( count++, "SMS Processor"  );
                preparedStmt.setLong( count++, this.customerId );
                preparedStmt.execute();
                data.setSuccess( true );
                data.setInfo( "User Update Successfully" );


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

    public String getCity()
    {
        return city;
    }

    public void setCity( String city )
    {
        this.city = city;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getNIC()
    {
        return NIC;
    }

    public void setNIC( String NIC )
    {
        this.NIC = NIC;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber( String phoneNumber )
    {
        this.phoneNumber = phoneNumber;
    }

    public String getTerritory()
    {
        return territory;
    }

    public void setTerritory( String territory )
    {
        this.territory = territory;
    }

    public String getHouseNo()
    {
        return houseNo;
    }

    public void setHouseNo( String houseNo )
    {
        this.houseNo = houseNo;
    }

    public String getStreet1()
    {
        return street1;
    }

    public void setStreet1( String street1 )
    {
        this.street1 = street1;
    }

    public String getStreet2()
    {
        return street2;
    }

    public void setStreet2( String street2 )
    {
        this.street2 = street2;
    }

    public String getDealers()
    {
        return dealers;
    }

    public void setDealers( String dealers )
    {
        this.dealers = dealers;
    }

    public long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( long customerId )
    {
        this.customerId = customerId;
    }
}
