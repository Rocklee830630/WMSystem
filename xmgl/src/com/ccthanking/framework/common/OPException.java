package com.ccthanking.framework.common;

public class OPException extends Exception
{
    public final static String INVALID_DATE_FORMAT="invalid date format";
    public final static String PARSING_ERROR="parsing error in DataItem";
    public final static String CANT_GET_DATABASE_CONNECTION="can't get database connection";
    public final static String PARAMENTERS_ARE_NOT_ENOUGH="parameters are not enough";
    public final static String NO_SUCH_ATTRIBUTE_EXISTS="no such attribute exists: ";
    public final static String TRANSTION_ALREADY_START="transaction already start";
    public final static String TRANSTION_NOT_START="transaction is not started yet";
    public final static String DATASET_ALREADY_MAKE_PERSISTENT="DataSet already make persistent";


    public final static String ORA_00001="标识已存在!";
    public final static String ORA_WR = "数据库操作错误!";

    public Throwable causeException = null;

    public OPException()
    {
        super();
    }

    public OPException(String message)
    {
        super(message);
        this.causeException = new Throwable(message);
    }

    public OPException(String message,Throwable cause)
    {
        super(message);
        this.causeException = cause;
    }

    public OPException(Throwable cause)
    {
        super(cause.getMessage());
        this.causeException = cause;
    }
    public Exception getCauseException(){
        return (Exception)this.causeException;
    }
}
