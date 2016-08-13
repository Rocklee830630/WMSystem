package com.ccthanking.framework.common;

public class BOException extends Exception
{

    public final static String BOException_message = "BO异常!";

    public Throwable causeException = null;

    public BOException()
    {
        super();
    }

    public BOException(String message)
    {
        super(message);
        this.causeException = new Throwable(message);
    }

    public BOException(String message,Throwable cause)
    {
        super(message);
        this.causeException = cause;
    }

    public BOException(Throwable cause)
    {
        super(cause.getMessage());
        this.causeException = cause;
    }
    public Exception getCauseException(){
        return (Exception)this.causeException;
    }
}
