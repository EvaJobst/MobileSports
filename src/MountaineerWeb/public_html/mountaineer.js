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

    $('.nav-tabs a').click(function () {
        $(this).tab('show');
    });


    loadTours();
    //toursLoaded(JSON.parse('{"-L0F7tYlB_lGX6RubJmz":{"averageRespiration":0,"averageSpeed":0,"averageSteps":56,"burnedKcal":0,"currentHeartRate":152,"distance":0,"duration":880,"elevation":0,"startLocation":{"altitude":100,"latitude":48.123,"longitude":14.567},"startTimestamp":1513172116,"startTimestampMillis":1513172116000,"stopTimestamp":1513172996,"stopTimestampMillis":1513172996000,"totalSteps":841,"weather":{"main":{"humidity":64,"temp":4,"temp_max":4,"temp_min":4},"name":"Haag","weather":[{"description":"clear sky"}],"wind":{"deg":70,"speed":1.5}},"startTimeString":"13.12.2017, 14:35:16","stopTimeString":"13.12.2017, 14:49:56"},"-L0FGl1EUeA8XEeqGLeT":{"averageRespiration":0,"averageSpeed":0,"averageSteps":52,"burnedKcal":-72550.8055367204,"currentHeartRate":162,"distance":0,"duration":672,"elevation":0,"name":"simulation1","startLocation":{"altitude":100,"latitude":48.123,"longitude":14.567},"startTimestamp":1513174649,"startTimestampMillis":1513174649000,"stopTimestamp":1513175321,"stopTimestampMillis":1513175321000,"totalSteps":818,"userInformation":{"age":22,"bodyMass":65,"completeAndValid":true,"gender":"Male","height":165,"id":"user1","par":3,"restingHearRate":60},"weather":{"main":{"humidity":60,"temp":4,"temp_max":4,"temp_min":4},"name":"Haag","weather":[{"description":"clear sky"}],"wind":{"deg":90,"speed":2.6}},"startTimeString":"13.12.2017, 15:17:29","stopTimeString":"13.12.2017, 15:28:41"}}'));
    initMap();
}

function initMap() {
    gMap = new google.maps.Map(document.getElementById('map'));

    $("a[data-toggle='tab']").on('shown.bs.tab', function (e) {
        var target = $(e.target).attr('href') // activated tab
        if (target == '#tourMapTab')
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
    $('#jsonDataDebugPlain').text(JSON.stringify(allTours));
    generateMenu();
}

function processTourData(tours) {
    $.each(tours, function (tourId)
    {
        tours[tourId]['startTimeString'] = moment(tours[tourId]['startTimestamp'], 'X').format('MMMM Do YYYY, h:mm:ss a');
        tours[tourId]['stopTimeString'] = moment(tours[tourId]['stopTimestamp'], 'X').format('MMMM Do YYYY, h:mm:ss a');

        tours[tourId]['startDateString'] = moment(tours[tourId]['startTimestamp'], 'X').format('MMM Do YYYY');

        var tourTitle = tours[tourId]['startDateString'];

        if (tours[tourId]['name'] != undefined)
        {
            tourTitle = tours[tourId]['name'] + ', ' + tourTitle;
        }

        tours[tourId]['tourTitle'] = tourTitle;
    });

    return tours;
}

function generateMenu() {
    var menuItems = [];

    $.each(allTours, function (tourId)
    {
        menuItems.push({text: allTours[tourId]['tourTitle'], data: tourId, startTimestamp: allTours[tourId]['startTimestamp']});
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

    $('#tourDetailsTitle').text(allTours[tourId]['tourTitle']);

    var tableOverviewRows = [
        ['StartTime', allTours[selectedTourId]['startTimeString']],
        ['StopTime', allTours[selectedTourId]['stopTimeString']]
    ];

    $('#tourOverviewTable').html(keyValueTableTemplateFn(tableOverviewRows));

    var tableOverviewRows = [
        ['Average Heart Rate', allTours[selectedTourId]['currentHeartRate']],
        ['Min Heart Rate', 'placeholder'],
        ['Max Heart Rate', 'placeholder']
    ];

    $('#heartRateTable').html(keyValueTableTemplateFn(tableOverviewRows));



    var lat = allTours[selectedTourId]['startLocation']['latitude'];
    var long = allTours[selectedTourId]['startLocation']['longitude'];

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

    if (tourDetails != null)
    {
        var baseTime = moment(startTime, 'X');
        var heartRateChartData = [];
        $.each(tourDetails['heartRateAtTime'], function (timeOffset)
        {
            heartRateChartData.push({x: baseTime.clone().add(timeOffset, 's').toDate(), y: tourDetails['heartRateAtTime'][timeOffset]});
        });
        drawHeartRateChart(heartRateChartData);


        var baseTime = moment(startTime, 'X');
        var stepsChartData = [];
        $.each(tourDetails['stepCountAtTime'], function (timeOffset)
        {
            stepsChartData.push({x: baseTime.clone().add(timeOffset, 's').toDate(), y: tourDetails['stepCountAtTime'][timeOffset]});
        });
        drawStepsChart(stepsChartData);

        var baseTime = moment(startTime, 'X');
        var energyChartData = [];
        $.each(tourDetails['stepCountAtTime'], function (timeOffset)
        {
            energyChartData.push({x: baseTime.clone().add(timeOffset, 's').toDate(), y: tourDetails['energyExpenditureAtTime'][timeOffset]});
        });
        drawEnergyChart(energyChartData);
    }
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
    drawChart('heartRateChart', chartData, 'Heart Rate', 'Heart Rate in BPM over Time', 'Heart Rate [bpm]', false);
}

function drawStepsChart(chartData) {
    drawChart('stepsChart', chartData, 'Steps per Minute', 'Average Steps per Minute over Time', 'Step Count', true);
}

function drawEnergyChart(chartData) {
    drawChart('energyChart', chartData, 'Energy Expenditure per Minute', 'Average Energy Expenditure per Minute over Time', 'Energy Expenditure [?]', true);
}

function drawChart(id, chartData, label, title, yLabel, yBeginAtZero = false) {
    var ctx = document.getElementById(id).getContext('2d');

    var config = {
        type: 'line',
        data: {
            datasets: [{
                    label: label,
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
                text: title
            },
            scales: {
                xAxes: [{
                        type: 'time',
                        distribution: 'linear',
                        display: true,
                        time: {
                            displayFormats: {
                                minute: 'hh:mm',
                                second: 'hh:mm'
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
                            labelString: yLabel
                        },
                        ticks: {
                            beginAtZero: yBeginAtZero
                        }
                    }]
            }
        }
    };

    var chart = new Chart(ctx, config);
}

function timestampToDateString(timestamp) {
    date = moment(timestamp, 'X').toDate();
    return date.toLocaleString();
}
