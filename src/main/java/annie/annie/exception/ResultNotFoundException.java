package annie.annie.exception;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

public class ResultNotFoundException extends WebApplicationException
{
public ResultNotFoundException(String id)
{
super(Response.status(Response.Status.NOT_FOUND).entity("result: "+id+" not found").type("text/plain").build());
}
}
