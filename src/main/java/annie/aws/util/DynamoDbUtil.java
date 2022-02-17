package annie.aws.util;

//AWS SDK
import software.amazon.awssdk.services.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.regions.*;
import java.net.*;

public class DynamoDbUtil
{
private static DynamoDbClient client=null;		//a reusable DynamoDBClient
private static DynamoDbEnhancedClient eClient=null;

/**
 * This method provides a handy way to get a DynamoDbEnhancedClient object.
 * The same object is reused in different requests.
 * 
 * @param region	The AWS region to connect to. e.g. Region.EU_WEST_1.
 * 					This parameter is only use if endPoint is not null.
 * @parm endPoint	The URL of the local DynamoDB server. e.g. http://localhost:8000
 * 					If this parameter is null, the AWS region in the region parameter is used.
 * @return	A DynamoDbEnhancedClient object for accessing DynamoDB.
 */
public static DynamoDbEnhancedClient getEClient(Region region,String endPoint)
{
if (DynamoDbUtil.eClient==null)	//no e-client yet, create one
	{
	DynamoDbClient client=DynamoDbUtil.getClient(region,endPoint);	//get client
	if (client==null)	//if client available
		return null;	//return null

	DynamoDbUtil.eClient=DynamoDbEnhancedClient.builder().	//otherwise create e-client
							dynamoDbClient(client).
							build();
	}
return DynamoDbUtil.eClient;
} //end method

/**
 * Return a DynamoDbClient object. The same object is reused.
 * @param region	The AWS region to connect to. e.g. Region.EU_WEST_1.
 * 					This parameter is only use if endPoint is not null.
 * @parm endPoint	The URL of the local DynamoDB server. e.g. http://localhost:8000
 * 					If this parameter is null, the AWS region in the region parameter is used.
 * @return	A DynamoDbClient object for accessing DynamoDB.
 */
public static DynamoDbClient getClient(Region region,String endPoint)
{
if (DynamoDbUtil.client==null)	//no client yet, create 1
	{
	if (endPoint==null)	//if no end-point, use AWS region given
		{
		DynamoDbUtil.client=DynamoDbClient.builder().
								region(region).			//use region
								build();
		}
	else try	{
				region=Region.of("local");				//set to "local" for local server
				DynamoDbUtil.client=DynamoDbClient.builder().
										region(region).
										endpointOverride(new URI(endPoint)).	//otherwise use end-point URL
										build();
				} catch (Exception e)
					{
					DynamoDbUtil.client=null;
					e.printStackTrace();
					return null;
					}
	}
return DynamoDbUtil.client;
} //end method
} //end class
