package annie.annie.annie.resource;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

//Jersey
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import annie.aws.util.*;
import annie.annie.annie.model.*;
import annie.annie.config.*;
import annie.annie.exception.*;
import software.amazon.awssdk.enhanced.dynamodb.*;

/**
 * @author G.Hanson
 */
@Path("/api")
public class AnnieResource
{
	/**
	 * Parameters are recieved in @POST request
	 * @FormParam studentnumber is the student number of the person the DB entry is about
	 * @FormParam compressions is the number of chest compressions they did on Annie
	 * @FormParam minimum is the minimum length of time between 2 successful compressions of Annies chest - in milliseconds
	 * @FormParam maximum is the maximum length of time between 2 successful compressions of Annies chest - in milliseconds
	 * @FormParam average is the average length of time between 2 successful compressions of Annies chest - in milliseconds
	 * @FormParam didpass is a boolean result on whether or not the student passed the exam - true if the student passed
	 * @FormParam feedback is a string, readable by humans, that describes what (if anything) the student needs to improve on
	 * 
	 */
	@POST
	public Response addResult(@FormParam("studentnumber") String studentNum,
			@FormParam("compressions") Integer numCompressions,
			@FormParam("minimum") Double minTime,
			@FormParam("maximum") Double maxTime,
			@FormParam("average") Double avgTime,
			@FormParam("didpass") Boolean didPass,
			@FormParam("feedback") String feedback)
	
	
	{
		System.out.println("*** DATA DUMP ***");
		System.out.println("studentNum: " + studentNum);
		System.out.println("numCompressions: " + numCompressions);
		System.out.println("minTime: " + minTime);
		System.out.println("maxTime: " + maxTime);
		System.out.println("avgTime: " + avgTime);
		System.out.println("didPass: " + didPass);
		System.out.println("feedback: " + feedback);
		
		
		
		try	{

			//All fields are mandatory - Throw an error if any are not complete
			if (studentNum==null || numCompressions==null || minTime == null || maxTime==null || avgTime==null || didPass==null || feedback==null) {
				throw new InvalidParamException("Incomplete data - All fields are mandatory");
			}

			//Create dynamoDB client and write to database
			DynamoDbEnhancedClient eClient=DynamoDbUtil.getEClient(Config.REGION,Config.LOCAL_ENDPOINT);
			DynamoDbTable<Annie> annieTable=eClient.table(Config.TABLE_NAME,TableSchema.fromBean(Annie.class));
			Annie annie = new Annie(studentNum,avgTime,minTime,maxTime,numCompressions,didPass,feedback);
			annieTable.putItem(annie);

			//Return 201 if successful
			return Response.
					status(201).
					entity("DB Write Successful").
					build();			//a successful reply
		} catch (Exception e)
		{
			// Return 400 if unsuccessful - Include error in string format
			return Response.
					status(400).
					entity("SOMETHING WENT WRONG: " + e.toString()).
					build();		//if the client did something wrong
		}
	}

	/**
	 * This method should handle a GET request to return one specific result"
	 * 
	 * @param code is the ID field of the database
	 * @return	The Annie object retrieved using the given code.
	 * 			This Annie object will be converted to JSON by Jackson.
	 */
	@GET
	@Path("/{code}")
	@Produces(MediaType.APPLICATION_JSON)
	public Annie retrieveOneResult(@PathParam("code") String id)
	{

		DynamoDbEnhancedClient eClient=DynamoDbUtil.getEClient(Config.REGION,Config.LOCAL_ENDPOINT);
		DynamoDbTable<Annie> annieTable=eClient.table(Config.TABLE_NAME,TableSchema.fromBean(Annie.class));
		Key key=Key.builder().partitionValue(id).build();
		Annie annie=annieTable.getItem(key);

		//If annie is not null (something found) return it
		if (annie!=null)
			return annie;

		//Only gets here if annie is null (nothing found)
		//Return helpful error message
		throw new ResultNotFoundException(id);

	}

	/**
	 * This method handles a GET request to return all annie objects as JSON list"
	 * 
	 * @return A Collection<Annie> object. Jackson will convert this to a JSON list.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Annie> retrieveAllResults()
	{

		try	{
			DynamoDbEnhancedClient eClient=DynamoDbUtil.getEClient(Config.REGION,Config.LOCAL_ENDPOINT);
			DynamoDbTable<Annie> annieTable=eClient.table(Config.TABLE_NAME,TableSchema.fromBean(Annie.class));
			List<Annie> results=annieTable.scan().items().stream().collect(Collectors.toList());
			return results;
		} catch (Exception e)
		{
			throw new WebApplicationException(e.toString(),500);
		}
	} //end method


	@DELETE
	@Path("/{code}")
	public Response deleteResult(@PathParam("code") String id)
	{

		DynamoDbEnhancedClient eClient=DynamoDbUtil.getEClient(Config.REGION,Config.LOCAL_ENDPOINT);
		DynamoDbTable<Annie> annieTable=eClient.table(Config.TABLE_NAME,TableSchema.fromBean(Annie.class));
		Key key=Key.builder().partitionValue(id).build();
		Annie annie=annieTable.deleteItem(key);

		//If annie is not null (something found)
		if (annie!=null) {
			return Response.
					status(201).
					entity(id + " has been deleted").
					build();
		}

		//If result not found, throw a helpful error message.
		throw new ResultNotFoundException(id);

	}




}
