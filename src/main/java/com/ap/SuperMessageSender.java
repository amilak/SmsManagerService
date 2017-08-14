package com.ap;

import com.ap.SMPP.ConfigReaderSMPP;
import common.ApDataSet;
import common.MessageQueue;
import connection.ConnectionFactory;
import connection.ConnectionUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus PC on 10/1/2015.
 */
public class SuperMessageSender implements  Runnable
{
    private boolean running = true;
    private static  int THREAD_COUNT = Integer.parseInt( ConfigReaderSMPP.prop.get("THREAD_COUNT"));
    public ApDataSet loadPendingMessagestest ()
    {
        ApDataSet data = new ApDataSet( true );
        for(int i=0 ;i<200;i++) {
            MessageQueue queue = new MessageQueue();
            queue.setMessage("Message " + i);
            queue.setPhoneNumber("0716865321");
            data.getData().add(queue);
        }
        MessageQueue queue = new MessageQueue();
        queue.setMessage("I have a java application which uses jsmpp library to send SMSs to SMSC. Application connects successfully and sends SMSs. Connection issue occurs after a week or so up time, during this up time it sends thousands of SMSs. But suddenly after few days application starts facing connection issues, some time 'Negative bind response 0x00045' and some time waiting bind response. When I check from wireshark, Application constantly sends enquire line packets and receives responses for them with status 'OK'. This means that application is connected but still it is attempting for new connection. Below is code for connection management." );
        queue.setPhoneNumber( "0716865321" );
        data.getData().add(queue);
        return data;
    }

    public ApDataSet loadPendingMessages ()
    {
        ApDataSet data = new ApDataSet( false );
        MessageQueue queue = null;
        List<MessageQueue> messages = new ArrayList<MessageQueue>();
        String query = " select * from messagequeue where status = 'P'";

        Connection conn = ConnectionFactory.getNewConnection();
        PreparedStatement preparedStmt = null;
        ResultSet rs = null;
        if( conn != null )
        {
            try
            {
                preparedStmt = conn.prepareStatement( query );
                rs = preparedStmt.executeQuery();
                while ( rs.next() )
                {
                    queue = new MessageQueue();
                    queue.load( rs );
                    messages.add(queue);
                }
                data.getData().addAll(messages);
                data.setSuccess(true);
                data.setInfo( "Data Add Successfully" );

            }
            catch( SQLException e )
            {
                data.setInfo( "Error in executing query" );
                e.printStackTrace();
            }
            finally
            {

                ConnectionUtility.close(rs);
                ConnectionUtility.close(preparedStmt);
                ConnectionUtility.close( conn );

            }
        }
        else
        {
            data.setInfo( "Error in connecting DB" );
        }

        return data;

    }


    private boolean isRunning()
    {
        return running;
    }


    public static void main(String ar[])
    {
        System.out.println(0%10);
        ArrayList<MessageQueue> subList = new ArrayList<MessageQueue>();
        for(int i=0 ;i<subList.size();++i)
        {
            System.out.println(i);
        }
    }


    @Override
    public void run() {
        MessageQueue outQueue;
        while ( isRunning())
        {
//            ApDataSet data = this.loadPendingMessagestest();
            ApDataSet data = this.loadPendingMessages();

            if ( data.isSuccess()  )
            {
                ArrayList<MessageSender> senderList = new ArrayList<MessageSender>();
                ArrayList<MessageQueue> subList = new ArrayList<MessageQueue>();
                ArrayList<MessageQueue> pendingMessages = data.getData();

                if (pendingMessages != null && pendingMessages.size()>0)
                {
                    int messagePerThread = pendingMessages.size() / THREAD_COUNT;
                    for(int i=0 ;i<pendingMessages.size();i++)
                    {
                        if(messagePerThread != 0 && i%messagePerThread== 0 && subList.size()>0)
                        {
                          MessageSender sender = new MessageSender(subList);
                          senderList.add(sender);
                          subList = new ArrayList<MessageQueue>();
                        }
                        subList.add(pendingMessages.get(i));

                    }

                    if(subList.size()>0)//for last set of message
                    {
                        MessageSender sender = new MessageSender(subList);
                        senderList.add(sender);
                    }

                    for (MessageSender messageSender : senderList)
                    {
                        Thread senderThread = new Thread(messageSender);
                        senderThread.start();
                    }

                    boolean allThreadFinished = false;
                    while ( !allThreadFinished)
                    {
                        boolean finished = true;
                        for (MessageSender messageSender : senderList)
                        {
                            finished = finished && messageSender.isFinished();
                        }
                        allThreadFinished = finished;
                    }
//                running = false;
                }
            }
            else
            {
                MessageUtills.logger.severe("-> Message loading error" );

            }


            try
            {
                Thread.sleep(10000);
            }
            catch(InterruptedException iex)
            {
                running = false;
            }

        }
    }
}
