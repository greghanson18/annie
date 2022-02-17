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

	populateResultsTable();
}

function populateResultsTable(){
	
	$.getJSON(baseURL,
		function(results){
			$("results").empty();	
			
			for (var i in results){
				var result = results[i];
				var id = result["id"];				
				var studentNum = result["studentNum"];
				var numCompressions = result["numCompressions"];
				var avgTime = result["avgTime"];
				var minTime = result["minTime"];
				var maxTime = result["maxTime"];
				var didPass = result["didPass"];
				var feedback = result["feedback"];
				
				var newTR = "<tr><td>" + studentNum + "</td><td>" + numCompressions + "</td><td>" + avgTime + "</td><td>" + minTime + "</td><td>" + maxTime + "</td><td>" + didPass + "</td><td>" + feedback + "</td><<td><input type=image src=gfx/trash.png name=trashicon id=" + id + " onclick=deleteRecord(this.id)></td></tr>";
				$('#results tr:last').after(newTR);
			}
		})
	
}

function deleteRecord(id) {
        $.ajax({
            url: 'http://localhost:8080/annie/annie/api/' + id,
            type: 'DELETE',
            success: function (result) {
                location.reload();
            }

        });
    }

function downloadResults(){
	
	$.getJSON(baseURL,function(results){
		var x = new CSVExport(results);
		return false;
	})
}
