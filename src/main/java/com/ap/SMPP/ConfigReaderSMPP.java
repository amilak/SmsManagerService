package com.ap.SMPP;



import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;

/**
 * Created by rukman on 7/24/2017.
 */
public class ConfigReaderSMPP
{

  public   final static Hashtable<String,String> prop = new Hashtable<String,String>(  );

    public  void loadConfigurations()
    {
        if( prop == null || prop.size()==0)
        {
            File configDir = new File(System.getProperty("catalina.base"), "conf");
            File configFile = new File(configDir, "SMPPconfig.txt");
            try{
//                FileInputStream fstream = new FileInputStream("C:\\SMS Service\\SMPPconfig.txt");
                FileInputStream fstream = new FileInputStream(configFile);
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                String strLine;
                String[] temp;
                String delimiter = "=";

                while ((strLine = br.readLine()) != null)
                {
                    temp = strLine.split(delimiter);
                    if( ! prop.containsKey( temp[0] ) )
                    {
                        prop.put( temp[0].trim(),temp[1].trim() );
                    }
                }

                in.close();
                br.close();

            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    public static void main( String ar[] )
    {
        //        loadConfigurations();
        ConfigReaderSMPP dd = new ConfigReaderSMPP();
        dd.loadConfigurations();
        System.out.println( "dddddd" );
        System.out.println(new Date());
    }
}
