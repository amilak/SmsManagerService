package com.ap;

import common.ApCode;
import common.ApDataSet;
import common.Customer;
import common.InvalidData;
import common.MessageQueue;
import common.Product;
import connection.ConnectionFactory;
import connection.ConnectionUtility;
import data.ApCustomerPoints;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Yasantha
 * Date: 8/9/15
 * Time: 7:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageUtills
{

//    public static String MESSAGE="Thank You. You have earned  ";
    public static String MESSAGE="Thank You. Obata prasada lakunu  ";
    public static String MESSAGE_TAMIL="Nanri. Neengal ";
//    public static String MESSAGE2="You have total ";
    public static String MESSAGE2="obe mulu prasada lakunu pramanaya ";
    public static String MESSAGE2_TAMIL=" pullikalai petrulleergal. Neengal pettra moththa pulligal  ";
//    public static String MESSAGE_INCORRECT_CODE="Incorrect Code. Please type the correct code. ";
    public static String MESSAGE_INCORRECT_CODE="Oba athulath kala code eka weradiya. Karunakara niwaredi code eka athulath karanna.\n " +
        "Neengal anuppiya code pilayanathu. Thayavu saithu sariyana code ai anuppavum.";
//    public static String MESSAGE_FIRST="Thank you. You have successfully registered with Masterstrokes Loyalty Scheme. You have earned ";
    public static String MESSAGE_FIRST="Thank you. Oba 'Masterstrokes' prasada thilina wedasatahanata sadarayen piliganimu. Obata prasada lakunu ";
    public static String MESSAGE_FIRST_TAMIL="Neengal Masterstrokes thittathudan inainthamaiku nanri. Neengal  ";
//    public static String MESSAGE_REJECT="Your number is not registered with Masterstrokes Loyalty scheme. Please contact Teshan on 0773541255 to register   ";
    public static String MESSAGE_REJECT="Oba ‘Masterstrokes’ prasada thilina wedasatahanata sandaha liyapadinchi wee nomatha. Karunakara Teshan mahatha (0773541255) amatha liyapadinchi wanna.\n" +
        " Mannikkavum. Neengal Masterstrokes thittathudan innam inaikapadavillai. Meladiha vibarangaluku Teshan (0773541255) ai alaikkavum.";
    public static Logger logger = Logger.getLogger("SMSServiceLogger");


    public static boolean processMassage( String message, Date receiveDate, String sender )
    {
        sender = updateSender( sender );
        Customer customer = getCustomer( sender );
        if( customer != null )
        {
            int totalPoints = getTotalPoints( customer.getCustomerId() );
            boolean firstTime = totalPoints==0;
            ApCode apCode = getApCode( message );
            if( apCode != null )
            {
                Product product = getProduct( apCode.getProductCode() );
                if( product != null )
                {
                    int bonusPoints = getBonusPoints(totalPoints, product.getPoints());
                    ApCustomerPoints points = new ApCustomerPoints();
                    points.setProductType( product.getProductType() );
                    points.setProductCode( product.getProductCode() );
                    points.setSubCategory( product.getSubCategory() );
                    points.setApCode( apCode.getCode() );
                    points.setPoints( bonusPoints);
                    points.setProcessDate( receiveDate );
                    points.setProcessDate( receiveDate );
                    points.setPhoneNumber( customer.getPhoneNumber() );
                    points.setName( customer.getName() );
                    points.setTerritory( customer.getTerritory() );
                    points.setNic( customer.getNIC() );
                    points.setCustomerId( customer.getCustomerId() );
                    ApDataSet insert = points.insert();

                    if(insert.isSuccess())
                    {
                        MessageUtills.logger.info( "Points Successfully updated" );
                        apCode.setStatus( ApCode.PROCESS );
                        apCode.update();

                        totalPoints = totalPoints + bonusPoints;

                        MessageQueue messageQueue = new MessageQueue();
                        messageQueue.setPhoneNumber( sender );
                        String messageStr = "";
                        if(firstTime)
                        {
                            messageStr = MESSAGE_FIRST + totalPoints  + " himiwe. \n";
                            messageStr = messageStr + MESSAGE_FIRST_TAMIL + totalPoints  + " pullikalai petrulleergal.";
                        }
                        else
                        {
                            messageStr = MESSAGE + bonusPoints  + " himiwe. " + MESSAGE2 + totalPoints + " ki.\n";
                            messageStr = messageStr + MESSAGE_TAMIL + bonusPoints   + MESSAGE2_TAMIL + totalPoints + " ahum.";
                        }
                        messageQueue.setMessage( messageStr );
                        messageQueue.setStatus( MessageQueue.STATUS_PENDING );
                        ApDataSet msgInsert = messageQueue.insert();
                        if(!msgInsert.isSuccess())
                        {
                            MessageUtills.logger.severe("Sender Message  saving error . Phone " + sender + " Message " + messageQueue.getMessage());
                        }
                        return true;
                    }
                }
                return true;
            }
            else
            {
                InvalidData invalidCode = new InvalidData();
                invalidCode.setCode( message );
                invalidCode.setPhoneNumber( sender );
                invalidCode.setType( InvalidData.INVALID_CODE );
                ApDataSet insert = invalidCode.insert();
                if(!insert.isSuccess())
                {
                    MessageUtills.logger.severe("Invalid Code  saving error . Phone " + sender + " Code " + message);
                }
                MessageQueue messageQueue = new MessageQueue();
                messageQueue.setPhoneNumber( sender );

                messageQueue.setMessage( MESSAGE_INCORRECT_CODE );
                messageQueue.setStatus( MessageQueue.STATUS_PENDING );
                ApDataSet msgInsert = messageQueue.insert();
                if(!msgInsert.isSuccess())
                {
                    MessageUtills.logger.severe("Invalid Code Sender Message  saving error . Phone " + sender + " Message " + messageQueue.getMessage());
                }

                return false;
            }

        }
        else
        {
            InvalidData regCustomer = new InvalidData();
            regCustomer.setCode( message );
            regCustomer.setPhoneNumber( sender );
            regCustomer.setType( InvalidData.INVALID_CUSTOMER );
            ApDataSet insert = regCustomer.insert();
            if(!insert.isSuccess())
            {
                MessageUtills.logger.severe("Un- Registered customer data saving error . Phone " + sender + " Code " + message);
            }
            MessageQueue messageQueue = new MessageQueue();
            messageQueue.setPhoneNumber( sender );

            messageQueue.setMessage( MESSAGE_REJECT );
            messageQueue.setStatus( MessageQueue.STATUS_PENDING );
            ApDataSet msgInsert = messageQueue.insert();
            if(!msgInsert.isSuccess())
            {
                MessageUtills.logger.severe("Un- Registered customer Sender Message  saving error . Phone " + sender + " Message " + messageQueue.getMessage());
            }

            return false;
        }

    }
    public static  int getTotalPoints(long cutomerId)
    {
        int total =0;

        String query = "Select sum(points) as total_points from appoints where customerId = ? ";
        PreparedStatement preparedStmt = null;
        Connection conn = ConnectionFactory.getNewConnection();
        ResultSet rs = null;
        if( conn != null )
        {
            try
            {
                preparedStmt = conn.prepareStatement( query );
                int count = 1;


                preparedStmt.setLong( count++, cutomerId );

                rs = preparedStmt.executeQuery();
                if( rs.next() )
                {
                    total = rs.getInt( "total_points" );

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
                ConnectionUtility.close( conn );

            }
        }


        return total;
    }
    public static void main( String ar[] )
    {
        /*System.out.println("Bonus Points 2000 " + getBonusPoints(2000, 40));
        System.out.println("Bonus Points 2999 " + getBonusPoints(2999, 40));
        System.out.println("Bonus Points 3000 " +  getBonusPoints(3000, 40));
        System.out.println("Bonus Points 3001 " + getBonusPoints(3001, 40));
        System.out.println("Bonus Points 4000 " + getBonusPoints(4000, 40));
        System.out.println("Bonus Points 11999 " + getBonusPoints(11999, 40));
        System.out.println("Bonus Points 12000 " + getBonusPoints(12000, 40));
        System.out.println("Bonus Points 12001 " +  getBonusPoints(12001, 40));
        System.out.println("Bonus Points 12001aaaaaa " +  Math.round(40.5));
        System.out.println("Bonus Points 24999 " + getBonusPoints(24999, 40));
        System.out.println("Bonus Points 25000 " + getBonusPoints(25000, 40));
        System.out.println("Bonus Points 25001 " + getBonusPoints(25001, 40));
        System.out.println("Bonus Points 40000 " + getBonusPoints(40000, 40));
        System.out.println("Bonus Points 49999 " + getBonusPoints(49999, 40));
        System.out.println("Bonus Points 50000 " + getBonusPoints(50000, 40));
        System.out.println("Bonus Points 50001 " + getBonusPoints(50001, 40));
        System.out.println("Bonus Points 75000 " + getBonusPoints(75000, 40));*/

        System.out.println(getInternationalNumber( "0716865321" ));
    }

    private static String updateSender( String sender )
    {
        String modified = sender;
        if( sender.startsWith( "+94" ) )
        {
            modified = "0" + sender.substring( 3 );
            //            modified = sender.replace("+94","0"  );
        }
        else if( sender.startsWith( "94" ) )
        {
            modified = "0" + sender.substring( 2 );
            //            modified = sender.replace("+94","0"  );
        }
        else if( sender.startsWith( "0094" ) )
        {
            //            modified = sender.replace("0094","0"  );
            modified = "0" + sender.substring( 4 );
        }
        return modified;
    }
    public static String getInternationalNumber( String sender )
    {
//        9471567765321
        String modified = sender;
        if( sender.startsWith( "0" ) )
        {
            modified = "94" + sender.substring( 1 );

        }

        return modified;
    }
    public static Customer getCustomer( String sender )
    {
        String query = "Select * from customers  where phoneNumber like  '%" + sender + "%'";

        PreparedStatement preparedStmt = null;
        Connection conn = ConnectionFactory.getNewConnection();
        ResultSet rs = null;
        if( conn != null )
        {
            try
            {
                preparedStmt = conn.prepareStatement( query );


                rs = preparedStmt.executeQuery();
                if(rs.next())
                {
                    Customer customer = new Customer();
                    customer.load( rs );
                    return customer;
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
                ConnectionUtility.close( conn );

            }
        }
        return null;

    }

    public static ApCode getApCode( String code )
    {
        String query = "Select * from ApCodes where code =? and status = 0";

        PreparedStatement preparedStmt = null;
        Connection conn = ConnectionFactory.getNewConnection();
        ResultSet rs = null;
        if( conn != null )
        {
            try
            {
                preparedStmt = conn.prepareStatement( query );

                preparedStmt.setString( 1, code );
                rs = preparedStmt.executeQuery();
                if(rs.next())
                {
                    ApCode apCode = new ApCode();
                    apCode.load( rs );
                    return apCode;
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
                ConnectionUtility.close( conn );

            }
        }
        return null;
    }

    public static Product getProduct( String code )
    {
        String query = "Select * from product where code =?";

        PreparedStatement preparedStmt = null;
        Connection conn = ConnectionFactory.getNewConnection();
        ResultSet rs = null;
        if( conn != null )
        {
            try
            {
                preparedStmt = conn.prepareStatement( query );

                preparedStmt.setString( 1, code );
                rs = preparedStmt.executeQuery();
                if(rs.next())
                {
                    Product product = new Product();
                    product.load( rs );
                    return product;
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
                ConnectionUtility.close( conn );

            }
        }
        return null;
    }

    public static void logMessage( String message, Date receiveDate, String sender )
    {
        MessageUtills.logger.info( "___________Start Reading messages from dongle ________________\n" );
        if( message == null || message.isEmpty() )
        {
            MessageUtills.logger.info( "   Empty message!" );
        }
        else
        {
            MessageUtills.logger.info( "   Message------------> " + message  );
        }
        MessageUtills.logger.info( "   Received date------> " + receiveDate  );
        MessageUtills.logger.info( "   Sender Mobile No---> " + sender  );
        MessageUtills.logger.info( "___________Complete messages from dongle ________________\n" );

    }

    public static int getBonusPoints(int totalPoints ,int point)
    {
        double bonusPoints=0;
        double percentage=0;
     if(totalPoints >49999)
     {
         percentage = 12;
     }
     else if(totalPoints >24999)
     {
         percentage = 10;
     }
     else if(totalPoints >11999)
     {
         percentage = 8;
     }
     else if(totalPoints >2999)
     {
         percentage = 5;
     }
        bonusPoints = (point * ((100 + percentage)/100));

      return ((int) Math.round(bonusPoints));
    }


}
