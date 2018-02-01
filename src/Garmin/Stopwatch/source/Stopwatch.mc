using Toybox.Time;
using Toybox.Time.Gregorian as Gregorian;


class Stopwatch {

	var startTime = null;
	var stopTime = null;

	function start(){
		startTime = new Time.Moment(Time.now().value());
	}
	
	function stop() {
		stopTime = new Time.Moment(Time.now().value());
	}
	
	function getStartTime(){
		return startTime;
	}
	
	function getStartTimeInfo(){
		return Gregorian.info(startTime, Time.FORMAT_MEDIUM);
	}
	
	function getElapsedSeconds(){
		var time = null;
		
		if(stopTime == null){
			time = new Time.Moment(Time.now().value());
		}
		else{
			time = stopTime;
		}
	
		return time.subtract(startTime).value();
	}
}