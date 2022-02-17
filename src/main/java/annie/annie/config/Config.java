package annie.annie.config;

import software.amazon.awssdk.regions.Region;

public class Config
{
//
//The region of DynamoDB service to connect. e.g. Region.EU_WEST_1 for the region in Ireland.
//This is ignored if the end-point is set to a non-null String, and it will connect to a local server specified by the end-point.
//
public static final Region REGION=Region.EU_WEST_1;

//
//Local DynamoDB server connection end-point URL.
//If your local DynamoDB server listen to a different port, update the URL accordingly.
//If you want to connect to AWS, set this to null and it will use the region specified above.
//
public static final String LOCAL_ENDPOINT="http://localhost:8000";

//
//The DynamoDB table name.
//
public static final String TABLE_NAME="annie";
} //end class
