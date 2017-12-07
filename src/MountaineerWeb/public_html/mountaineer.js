var config = {
    apiKey: 'AIzaSyCEBS4uXwZGmYJGvWFfFgxQUVafhbvIyZQ',
    authDomain: 'mosproject-fdce4.firebaseapp.com',
    databaseURL: 'https://mosproject-fdce4.firebaseio.com',
    projectId: 'mosproject-fdce4',
    storageBucket: 'mosproject-fdce4.appspot.com',
    messagingSenderId: '563541673510'
};

firebase.initializeApp(config);

$(document).ready(initPage);

var allTours;
var selectedTourId;

var keyValueTableTemplateFn;
var tourListTemplateFn;
var gMap;
var gMarker;

function initPage() {
    keyValueTableTemplateFn = doT.template($('#keyValueTableTemplate').text());
    tourListTemplateFn = doT.template($('#tourListTemplate').text());

    $('#loadTours').click(function () {
        $(this).text('loading...');
        loadTours();
    });

    $(".nav-tabs a").click(function () {
        $(this).tab('show');
    });


    //loadTours();
    toursLoaded(JSON.parse('{"-L-BFSktECnlidMbLfBx":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":3,"elevation":0,"humidity":0,"maxTemp":0,"minTemp":0,"rain":0,"startTimestamp":1512034127190,"stopTimestamp":1512034130952,"totalSteps":0,"wind":0,"startTimeString":"30.11.2017, 10:28:47","stopTimeString":"30.11.2017, 10:28:50"},"-L-BG7rLKDoKF1DKZapY":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":3,"elevation":0,"humidity":0,"maxTemp":0,"minTemp":0,"rain":0,"startTimestamp":1512034303813,"stopTimestamp":1512034307488,"totalSteps":0,"wind":0,"startTimeString":"30.11.2017, 10:31:43","stopTimeString":"30.11.2017, 10:31:47"},"-L-CxTsASh5pC9wv2Li4":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":32,"elevation":0,"location":"Lat: 48.3709940033504, Long: 14.514109399612822","startTimestamp":1512062676330,"stopTimestamp":1512062709167,"totalSteps":0,"weather":{"main":{"humidity":86,"temp":1,"temp_max":1,"temp_min":1},"wind":{"deg":240,"speed":4.1}},"startTimeString":"30.11.2017, 18:24:36","stopTimeString":"30.11.2017, 18:25:09"},"-L-D1Rx07lyhZZ272Q4i":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":36,"elevation":0,"location":"Lat: 48.37090873652961, Long: 14.51439645556685","locationLat":47.37090873652961,"locationLong":15.51439645556685,"startTimestamp":1512063974855,"stopTimestamp":1512064012010,"totalSteps":0,"weather":{"main":{"humidity":86,"temp":1,"temp_max":1,"temp_min":1},"wind":{"deg":230,"speed":3.6}},"startTimeString":"30.11.2017, 18:46:14","stopTimeString":"30.11.2017, 18:46:52"},"-L-DSXHqC7YM5IvK8YKq":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":20,"elevation":0,"location":"Lat: 48.3707372, Long: 14.5143459","locationLat":48.3707372,"locationLong":14.5143459,"startTimestamp":1512071090517,"stopTimestamp":1512071111718,"totalSteps":10,"weather":{"main":{"humidity":86,"temp":1,"temp_max":1,"temp_min":1},"wind":{"deg":220,"speed":3.1}},"startTimeString":"30.11.2017, 20:44:50","stopTimeString":"30.11.2017, 20:45:11"},"-L-TRSSCUbQwxDCb7i6f":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":2,"elevation":0,"locationLat":0,"locationLong":0,"startTimestamp":1512339262607,"stopTimestamp":1512339265271,"totalSteps":0,"startTimeString":"3.12.2017, 23:14:22","stopTimeString":"3.12.2017, 23:14:25"},"-L-TUdDWhfcpyH5cjASO":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":3,"elevation":0,"locationLat":0,"locationLong":0,"startTimestamp":1512340085690,"stopTimestamp":1512340089646,"totalSteps":0,"startTimeString":"3.12.2017, 23:28:05","stopTimeString":"3.12.2017, 23:28:09"},"-L-TVoWH-XR8nduvnWPu":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":0,"elevation":0,"locationLat":0,"locationLong":0,"startTimestamp":1512340404665,"stopTimestamp":1512340405639,"totalSteps":0,"startTimeString":"3.12.2017, 23:33:24","stopTimeString":"3.12.2017, 23:33:25"},"-L-VfmGpPd4lNY6PuHeY":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":4,"elevation":0,"locationLat":37.421998333333335,"locationLong":-122.08400000000002,"startTimestamp":1512376829625,"stopTimestamp":1512376834394,"totalSteps":0,"startTimeString":"4.12.2017, 09:40:29","stopTimeString":"4.12.2017, 09:40:34"},"-L-VhUHGQrgTVpMzqtCH":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":3,"elevation":0,"locationLat":0,"locationLong":0,"startTimestamp":1512377277579,"stopTimestamp":1512377281600,"totalSteps":0,"startTimeString":"4.12.2017, 09:47:57","stopTimeString":"4.12.2017, 09:48:01"},"-L-cQGLuYUmO6byD-KEz":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":15,"elevation":0,"locationLat":37.421998333333335,"locationLong":-122.08400000000002,"startTimestamp":1512506707543,"stopTimestamp":1512506723359,"totalSteps":0,"startTimeString":"5.12.2017, 21:45:07","stopTimeString":"5.12.2017, 21:45:23"},"-L-cQoLRfkUz1UM2e65K":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":17,"elevation":0,"locationLat":0,"locationLong":0,"startTimestamp":1512506845862,"stopTimestamp":1512506863066,"totalSteps":0,"startTimeString":"5.12.2017, 21:47:25","stopTimeString":"5.12.2017, 21:47:43"},"-L-cRGEBo0fMGCAoZvkC":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":2,"elevation":0,"locationLat":0,"locationLong":0,"startTimestamp":1512506956426,"stopTimestamp":1512506958904,"totalSteps":0,"startTimeString":"5.12.2017, 21:49:16","stopTimeString":"5.12.2017, 21:49:18"},"-L-cUGQNqx64DOv1ookn":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":5,"elevation":0,"locationLat":0,"locationLong":0,"startTimestamp":1512507768159,"stopTimestamp":1512507773341,"totalSteps":0,"startTimeString":"5.12.2017, 22:02:48","stopTimeString":"5.12.2017, 22:02:53"},"-L-cYeg14tNWZBvI_jtL":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":125,"elevation":0,"locationLat":0,"locationLong":0,"startTimestamp":1512508799126,"stopTimestamp":1512508924991,"totalSteps":125,"startTimeString":"5.12.2017, 22:19:59","stopTimeString":"5.12.2017, 22:22:04"},"-L-cq12zFdA-B4ZYkj20":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":133,"elevation":0,"locationLat":45.123,"locationLong":40.567,"startTimestamp":0,"stopTimestamp":1512513739846,"totalSteps":133,"startTimeString":"1.1.1970, 01:00:00","stopTimeString":"5.12.2017, 23:42:19"},"-L-cs-EFC9K1_hfeMg81":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":68,"elevation":0,"locationLat":45.123,"locationLong":40.567,"startTimestamp":1512514188100,"stopTimestamp":1512514256608,"totalSteps":68,"startTimeString":"5.12.2017, 23:49:48","stopTimeString":"5.12.2017, 23:50:56"},"-L-fffUiJ4s-XGiWWeOe":{"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"currentHeartRate":152,"distance":0,"duration":189,"elevation":0,"locationLat":45.123,"locationLong":40.567,"startTimestamp":1512561167,"stopTimestamp":1512561357,"totalSteps":187,"weather":{"main":{"humidity":98,"temp":1.6,"temp_max":1.6,"temp_min":1.6},"wind":{"deg":275.001,"speed":3.62}},"startTimeString":"6.12.2017, 12:52:47","stopTimeString":"6.12.2017, 12:55:57"}}'));
    initMap();
}

function initMap() {
    gMap = new google.maps.Map(document.getElementById('map'));

    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
        var target = $(e.target).attr("href") // activated tab
        if (target == "#tourMapTab")
        {
            currentCenter = gMap.getCenter();
            google.maps.event.trigger(map, 'resize');
            gMap.setCenter(currentCenter);
        }
    });
}

function loadTours() {
    firebase.database().ref('/tours/user1').once('value').then(function (snapshot) {
        toursLoaded(snapshot.val());
    });
}

function toursLoaded(tours) {
    allTours = processTourData(tours);

    $('#jsonDataDebug').text(JSON.stringify(allTours, null, 2));

    generateMenu();
}

function processTourData(tours) {
    $.each(tours, function (tourId)
    {
        tours[tourId]['startTimeString'] = timestampToDateString(tours[tourId]['startTimestamp']);
        tours[tourId]['stopTimeString'] = timestampToDateString(tours[tourId]['stopTimestamp']);
    });

    return tours;
}

function generateMenu() {
    var menuItems = [];

    $.each(allTours, function (tourId)
    {
        menuItems.push({text: allTours[tourId]['startTimeString'], data: tourId, startTimestamp: allTours[tourId]['startTimestamp']});
    });

    //sort descending by startdate
    menuItems.sort(function (a, b) {
        if (a.startTimestamp < b.startTimestamp)
        {
            return 1;
        }
        if (a.startTimestamp > b.startTimestamp)
        {
            return -1;
        }
        return 0;
    });

    $('#tourList').html(tourListTemplateFn(menuItems));
    $('.nav-link').click(function () {
        tourSelected($(this).attr('data'));
    });
}

function showStartPage() {
    $('#startPage').show();
}

function tourSelected(tourId) {
    selectedTourId = tourId;

    loadTourDetails();

    $('#tourDetailsTitle').text('Your Tour from ' + allTours[selectedTourId]['startTimeString']);

    var tableOverviewRows = [
        ['StartTime', allTours[selectedTourId]['startTimeString']],
        ['StopTime', allTours[selectedTourId]['stopTimeString']]
    ];

    $('#tourOverviewTable').html(keyValueTableTemplateFn(tableOverviewRows));

    var lat = allTours[selectedTourId]['locationLat'];
    var long = allTours[selectedTourId]['locationLong'];

    if (lat != undefined && long != undefined)
    {
        centerMap(lat, long);
    }

    $('#tourDetails').show();
}


function loadTourDetails() {
    firebase.database().ref('/tourDetails/user1/' + selectedTourId).once('value').then(function (snapshot) {
        tourDetailsLoaded(snapshot.val());
    });
}

function tourDetailsLoaded(tourDetails) {
    $('#jsonDataDebug2').text(JSON.stringify(tourDetails, null, 2));

    var startTime = allTours[selectedTourId]['startTimestamp'];

    var baseTime = moment(startTime, "X");

    var heartRateChartData = [];

    $.each(tourDetails['heartRateAtTime'], function (timeOffset)
    {
        heartRateChartData.push({x: baseTime.clone().add(timeOffset, 's').toDate(), y: tourDetails['heartRateAtTime'][timeOffset]});
    });

    drawHeartRateChart(heartRateChartData);


    var baseTime = moment(startTime, "X");

    var stepsChartData = [];

    var prevStepCount = 0;

    $.each(tourDetails['stepCountAtTime'], function (timeOffset)
    {
        var stepCountSinceLast = tourDetails['stepCountAtTime'][timeOffset] - prevStepCount;
        prevStepCount = tourDetails['stepCountAtTime'][timeOffset];
        stepsChartData.push({x: baseTime.clone().add(timeOffset, 's').toDate(), y: stepCountSinceLast});
    });

    drawStepsChart(stepsChartData);
}

function centerMap(lat, long) {
    var location = {lat: lat, lng: long};
    var zoom = 8;

    gMap.setCenter(location);
    gMap.setZoom(zoom);

    if (gMarker != undefined)
    {
        gMarker.setMap(null);
    }

    gMarker = new google.maps.Marker({
        position: location,
        map: gMap
    });
}

function drawHeartRateChart(chartData) {
    var ctx = document.getElementById("heartRateChart").getContext('2d');

    var config = {
        type: 'line',
        data: {
            datasets: [{
                    label: "Heart Rate",
                    data: chartData,
                    borderColor: 'rgb(255, 114, 96)',
                    backgroundColor: 'rgba(0, 0, 0, 0)',
                    fill: false,
                }]
        },
        options: {
            responsive: true,
            title: {
                display: true,
                text: "Heart Rate in BPM over Time"
            },
            scales: {
                xAxes: [{
                        type: "time",
                        distribution: 'linear',
                        display: true,
                        time: {
                            displayFormats: {
                                quarter: 'hh:mm'
                            }
                        },
                        scaleLabel: {
                            display: true,
                            labelString: 'Time'
                        }
                    }],
                yAxes: [{
                        display: true,
                        scaleLabel: {
                            display: true,
                            labelString: 'Heart Rate [bpm]'
                        }
                    }]
            }
        }
    };

    var heartRateChart = new Chart(ctx, config);
}

function drawStepsChart(chartData) {
    var ctx = document.getElementById("stepsChart").getContext('2d');

    var config = {
        type: 'line',
        data: {
            datasets: [{
                    label: "Heart Rate",
                    data: chartData,
                    borderColor: 'rgb(255, 114, 96)',
                    backgroundColor: 'rgba(0, 0, 0, 0)',
                    fill: false,
                }]
        },
        options: {
            responsive: true,
            title: {
                display: true,
                text: "Heart Rate in BPM over Time"
            },
            scales: {
                xAxes: [{
                        type: "time",
                        distribution: 'linear',
                        display: true,
                        time: {
                            displayFormats: {
                                quarter: 'hh:mm'
                            }
                        },
                        scaleLabel: {
                            display: true,
                            labelString: 'Time'
                        }
                    }],
                yAxes: [{
                        display: true,
                        scaleLabel: {
                            display: true,
                            labelString: 'Heart Rate [bpm]'
                        },
                        ticks: {
                            beginAtZero: true
                        }
                    }]
            }
        }
    };

    var heartRateChart = new Chart(ctx, config);
}

function timestampToDateString(timestamp) {
    date = moment(timestamp, "X").toDate();
    return date.toLocaleString();
}
