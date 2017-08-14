package common;

import java.util.ArrayList;

/**
 * Created by rukman on 8/26/2015.
 */
public class ApDataSet
{   private ArrayList data;
    private String info;
    private boolean success;

    public ApDataSet()
    {
        this.data = new ArrayList();
    }

    public ApDataSet( boolean success )
    {
        this.data = new ArrayList();
        this.success = success;
    }

    public ArrayList getData()
    {
        return data;
    }

    public void setData( ArrayList data )
    {
        this.data = data;
    }

    public String getInfo()
    {
        return info;
    }

    public void setInfo( String info )
    {
        this.info = info;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess( boolean success )
    {
        this.success = success;
    }
}
