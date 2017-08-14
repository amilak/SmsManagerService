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
public class Product
{
    private long productId;
    private String productCode;
    private String productName;
    private String productType;
    private String quantity;
    private String group;
    private String subCategory;
    private int points;


    public ApDataSet insert()
    {
        ApDataSet data = new ApDataSet( false );
        String query = " insert into product (code,name,type, quantity, product_group,sub_Category, point, date_created,last_modified,created_by,modified_by)" + " values (?, ?, ?, ?,?,?, ?,  now(),now(),?,?)";

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
                preparedStmt.setString( count++, this.productName );
                preparedStmt.setString( count++, this.productType );
                preparedStmt.setString( count++, this.quantity );
                preparedStmt.setString( count++, this.group );
                preparedStmt.setString( count++, this.subCategory );
                preparedStmt.setInt( count++, this.points );
                //                preparedStmt.setString( count++, Login.loginUser.getUserName() );
                //                preparedStmt.setString( count++, Login.loginUser.getUserName() );
                //
                preparedStmt.setString( count++, "test" );
                preparedStmt.setString( count++, "test" );

                preparedStmt.execute();
                data.setSuccess( true );
                data.setInfo( "Product Add Successfully" );

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
        this.productId = rs.getLong( "product_Id" );
        this.productCode = rs.getString( "code" );
        this.productName = rs.getString( "name" );
        this.productType = rs.getString( "type" );
        this.quantity = rs.getString( "quantity" );
        this.group = rs.getString( "product_group" );
        this.subCategory = rs.getString( "sub_Category" );
        this.points = rs.getInt( "point" );


    }


    public ApDataSet update()
    {
        ApDataSet data = new ApDataSet( false );
        String query = "update product set name = ?, code = ?,type=?, quantity =?,product_group=?,sub_Category=?  ,point=? ,last_modified=now(), modified_by =?" + "  where product_Id = ?";
        Connection conn = ConnectionFactory.getNewConnection();
        PreparedStatement preparedStmt = null;
        if( conn != null )
        {
            try
            {
                preparedStmt = conn.prepareStatement( query );
                int count = 1;


                preparedStmt.setString( count++, this.productName );
                preparedStmt.setString( count++, this.productCode );
                preparedStmt.setString( count++, this.productType );
                preparedStmt.setString( count++, this.quantity );
                preparedStmt.setString( count++, this.group );
                preparedStmt.setString( count++, this.subCategory );
                preparedStmt.setInt( count++, this.points );

                //                preparedStmt.setString( count++, Login.loginUser.getUserName()  );
                preparedStmt.setString( count++, "test" );
                preparedStmt.setLong( count++, this.productId );
                preparedStmt.execute();
                data.setSuccess( true );
                data.setInfo( "Product Update Successfully" );


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


    public Product()
    {

    }

    public String getProductCode()
    {
        return productCode;
    }

    public void setProductCode( String productCode )
    {
        this.productCode = productCode;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName( String productName )
    {
        this.productName = productName;
    }

    public String getProductType()
    {
        return productType;
    }

    public void setProductType( String productType )
    {
        this.productType = productType;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public void setQuantity( String quantity )
    {
        this.quantity = quantity;
    }

    public String getGroup()
    {
        return group;
    }

    public void setGroup( String group )
    {
        this.group = group;
    }

    public String getSubCategory()
    {
        return subCategory;
    }

    public void setSubCategory( String subCategory )
    {
        this.subCategory = subCategory;
    }

    public int getPoints()
    {
        return points;
    }

    public void setPoints( int points )
    {
        this.points = points;
    }

    public long getProductId()
    {
        return productId;
    }

    public void setProductId( long productId )
    {
        this.productId = productId;
    }
}
