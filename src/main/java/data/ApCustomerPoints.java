package data;

import common.ApDataSet;
import connection.ConnectionFactory;
import connection.ConnectionUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by rukman on 8/26/2015.
 */
public class ApCustomerPoints
{
    private String productCode;
    private String productType;
    private String subCategory;
    private String apCode;
    private int points;
    private Date processDate;
    private String phoneNumber;
    private String name;
    private String territory;
    private String nic;
    private long customerId;


    public ApDataSet insert()
    {
        ApDataSet data = new ApDataSet( false );
        String query = " insert into appoints (productCode,productType,sub_Category, apCode, date,phoneNumber,name, territory, nic,points,customerId)" + " values (?, ?,?, ?, ?,?,?, ?,?,?,?)";

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
                preparedStmt.setString( count++, this.productCode );
                preparedStmt.setString( count++, this.productType );
                preparedStmt.setString( count++, this.subCategory );
                preparedStmt.setString( count++, this.apCode );
                preparedStmt.setDate( count++, new java.sql.Date(  this.processDate.getTime()) );
                preparedStmt.setString( count++, this.phoneNumber );
                preparedStmt.setString( count++, this.name );
                preparedStmt.setString( count++, this.territory );
                preparedStmt.setString( count++, this.nic );
                preparedStmt.setInt( count++, this.points );
                preparedStmt.setLong( count++, this.customerId );
                //                preparedStmt.setString( count++, Login.loginUser.getUserName() );
                //                preparedStmt.setString( count++, Login.loginUser.getUserName() );
                //


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

    public String getProductCode()
    {
        return productCode;
    }

    public void setProductCode( String productCode )
    {
        this.productCode = productCode;
    }

    public String getProductType()
    {
        return productType;
    }

    public void setProductType( String productType )
    {
        this.productType = productType;
    }

    public String getSubCategory()
    {
        return subCategory;
    }

    public void setSubCategory( String subCategory )
    {
        this.subCategory = subCategory;
    }

    public String getApCode()
    {
        return apCode;
    }

    public void setApCode( String apCode )
    {
        this.apCode = apCode;
    }

    public int getPoints()
    {
        return points;
    }

    public void setPoints( int points )
    {
        this.points = points;
    }

    public Date getProcessDate()
    {
        return processDate;
    }

    public void setProcessDate( Date processDate )
    {
        this.processDate = processDate;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber( String phoneNumber )
    {
        this.phoneNumber = phoneNumber;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getTerritory()
    {
        return territory;
    }

    public void setTerritory( String territory )
    {
        this.territory = territory;
    }

    public String getNic()
    {
        return nic;
    }

    public void setNic( String nic )
    {
        this.nic = nic;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
}
