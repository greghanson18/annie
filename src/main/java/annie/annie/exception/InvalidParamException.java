package annie.annie.exception;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

public class InvalidParamException extends WebApplicationException 
{
public InvalidParamException(String message)
{
super(Response.status(Response.Status.BAD_REQUEST).entity(message).type("text/plain").build());
System.out.println("INVALID PARAM EXCEPTION: " + message);
} //end method
} //end class
