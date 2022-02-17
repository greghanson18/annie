/**
 * 
 */
 var baseURL = "http://localhost:8080/annie/annie/api";
 
 try	{
	$(function()
		{
		init();
		}
	);
	} catch (e)
		{
		alert("*** jQuery not loaded. ***");
		}

//
// Initialise page.
//

function init(){

	doCalculations();
}

function doCalculations(){
	
	//Get all table data
	$.getJSON(baseURL,function(results){
		var numPasses = 0;
		var numResults = 0;
		var tooSlow = 0;
		var tooFast = 0;
		var tooFew = 0;
		var tooMany = 0;
		
		//For each entry in table
		for (var i in results){
				
				
			//Gather data about student performance
			var result = results[i];
			var feedback = result["feedback"];
			
			numResults++;
			
			if(feedback.includes("OK")){
				numPasses++;
			}
			if(feedback.includes("slow on compressions")){
				tooSlow++;
			}
			if(feedback.includes("fast on compressions")){
				tooFast++;
			}
			if(feedback.includes("many compressions")){
				tooMany++;
			}
			if(feedback.includes("few compressions")){
				tooFew++;
			}
			}
			
			var passPercent = parseInt(getPercent(numPasses,numResults));
			var slowPercent = parseInt(getPercent(tooSlow,numResults));
			var fastPercent = parseInt(getPercent(tooFast,numResults));
			var manyPercent = parseInt(getPercent(tooMany,numResults));
			var fewPercent = parseInt(getPercent(tooFew,numResults));
		
		//Write results to web page
		$("#passratehead").text("Pass Rate: " + passPercent + "%");
		$("#slowhead").text("Students who went too slow: " + slowPercent + "%");
		$("#fasthead").text("Students who went too fast: " + fastPercent + "%");
		$("#manyhead").text("Students who did too many compressions: " + manyPercent + "%");
		$("#fewhead").text("Students who did too few compressions: " + fewPercent + "%");

		
		var passScaleWidth = $("#passscale").width();
		var arrowWidth = $("#passarrow").width()
		var space = passScaleWidth - arrowWidth;
		
		var passArrowMargin = (space * (passPercent / 100));
		var slowArrowMargin = (space * (slowPercent) / 100);
		var fastArrowMargin = (space * (fastPercent) / 100);
		var manyArrowMargin = (space * (manyPercent) / 100);
		var fewArrowMargin = (space * (fewPercent) / 100);
		
		
		$("#passarrow").css({"margin-left": passArrowMargin});
		$("#slowarrow").css({"margin-left": slowArrowMargin});
		$("#fastarrow").css({"margin-left": fastArrowMargin});
		$("#manyarrow").css({"margin-left": manyArrowMargin});
		$("#fewarrow").css({"margin-left": fewArrowMargin});
		
	});
	
}

function getPercent(query,total){
	
	var i = parseFloat(query);
	var j = parseFloat(total);
	
	return ((i/j) * 100.0);
	
}