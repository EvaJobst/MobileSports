//
// Copyright 2015-2016 by Garmin Ltd. or its subsidiaries.
// Subject to Garmin SDK License Agreement and Wearables
// Application Developer Agreement.
//

using Toybox.WatchUi as Ui;
using Toybox.Graphics as Gfx;
using Toybox.Timer as Timer;
using Toybox.Time.Gregorian as Gregorian;

var stopwatch = null;

function newInput(newInput){
	if(newInput == ON_SELECT){
		stopwatch = new Stopwatch();
		stopwatch.start();
	}
	else if(newInput == ON_MENU && stopwatch != null){
		stopwatch.stop();
	}
	else if(newInput == ON_BACK){
		stopwatch = null;
	}
	
	Ui.requestUpdate();
}



class StopwatchView extends Ui.View {
    var timer;

    function initialize() {
        View.initialize();
        
        timer = new Timer.Timer();
    }

	function onShow() {
	    Ui.requestUpdate();
        timer.start(method(:onTimer), 1000, true);
    }

    function onHide() {
        timer.stop();
    }
    
    //! Load your resources here
    function onLayout(dc) {
    	setLayout(Rez.Layouts.MainLayout(dc));
    }

    //! Update the view
    function onUpdate(dc) {
    	var elapsed = findDrawableById("elapsed");
    	var startTime = findDrawableById("startTime");

	    if(stopwatch != null)
	    {
		    var elapsedSeconds = stopwatch.getElapsedSeconds();

	    	var startInfo = stopwatch.getStartTimeInfo();
	    	
	    	var startTimeString = format("$1$:$2$:$3$", [startInfo.hour.format("%02d"), startInfo.min.format("%02d"), startInfo.sec.format("%02d")]);
	    	startTime.setText(startTimeString);
	    	elapsed.setText(secondsToTimeString(elapsedSeconds));
	    }
	    else{
	    	startTime.setText("Start Time");
	    	elapsed.setText(secondsToTimeString(0));
	    }

    	View.onUpdate(dc);
    }
    
    function onTimer() {
        Ui.requestUpdate();
    }
    
    function secondsToTimeString(totalSeconds) {
	    var hours = totalSeconds / 3600;
	    var minutes = (totalSeconds /60) % 60;
	    var seconds = totalSeconds % 60;
	    var timeString = format("$1$:$2$:$3$", [hours.format("%02d"), minutes.format("%02d"), seconds.format("%02d")]);
	    return timeString;
	}
}
