package annie.annie.annie.model;

import java.time.Instant;
import java.util.Random;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;


/*
*
* This class models the data for Annie
*
*/

@DynamoDbBean
public class Annie{

/*
 * The id of the DB entry 
 */
public String ID;

/*
 * Feedback on the students exam
 */
public String feedback;

/*
 * The student number
 */
public String studentNumber;

/*
 * The average time between compressions 
 */
public double avgTime;

/*
 * The minimum time between compression
 */
public double minTime;

/*
 * The maximum time between compressions 
 */
public double maxTime;

/*
 * The number of compressions
 */
public int numCompressions;

/*
 * Did the student pass the test? 
 */
public boolean didPass;


/*
 * The default constructor
 */
public Annie() {
}


/*
 * Create a DB entry
 */
public Annie (String studentNum, double avgTime, double minTime, double maxTime, int numCompressions, boolean didPass, String feedback) {
	this.setID(generateID());
	this.setstudentNum(studentNum);
	this.setavgTime(avgTime);
	this.setminTime(minTime);
	this.setmaxTime(maxTime);
	this.setnumCompressions(numCompressions);
	this.setdidPass(didPass);
	this.setfeedback(feedback);
}

/*
 * Getters + Setters
 */
@DynamoDbPartitionKey
public String getID() {
	return this.ID;
}

public void setID(String id) {
	this.ID = id;
}

public double getavgTime() {
	return this.avgTime;
}

public void setavgTime(double time) {
	this.avgTime = time;
}

public String getstudentNum() {
	return this.studentNumber;
}

public void setstudentNum(String studentNum) {
	this.studentNumber = studentNum;
}

public double getminTime() {
	return this.minTime;
}

public void setminTime(double time) {
	this.minTime = time;
}

public double getmaxTime() {
	return this.maxTime;
}

public void setmaxTime(double time) {
	this.maxTime = time;
}

public int getnumCompressions() {
	return this.numCompressions;
}

public void setnumCompressions(int i) {
	this.numCompressions = i;
}

public boolean getdidPass() {
	return this.didPass;
}

public void setdidPass(boolean pass) {
	this.didPass = pass;
}

public String getfeedback() {
	return this.feedback;
}

public void setfeedback(String feedback) {
	this.feedback = feedback;
}

/*
 * Generate ID for DB entry
 * To make each result unique, result is based on time-stamp method was run + 8x random characters
 */

private String generateID() {
	
	Instant instant = Instant.now();
    int leftLimit = 97; // letter 'a'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = 8;
    Random random = new Random();
    StringBuilder buffer = new StringBuilder(targetStringLength);
    
    buffer.append(instant.toString());
    buffer.append("-");
    
    for (int i = 0; i < targetStringLength; i++) {
        int randomLimitedInt = leftLimit + (int) 
          (random.nextFloat() * (rightLimit - leftLimit + 1));
        buffer.append((char) randomLimitedInt);
    }

    return buffer.toString();
}

}

