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
    toursLoaded(JSON.parse('{"-L-BFSktECnlidMbLfBx":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":3,"elevation":0,"humidity":0,"maxTemp":0,"minTemp":0,"rain":0,"startTimestamp":1512034127190,"stopTimestamp":1512034130952,"totalSteps":0,"wind":0,"startTimeString":"30.11.2017, 10:28:47","stopTimeString":"30.11.2017, 10:28:50"},"-L-BG7rLKDoKF1DKZapY":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":3,"elevation":0,"humidity":0,"maxTemp":0,"minTemp":0,"rain":0,"startTimestamp":1512034303813,"stopTimestamp":1512034307488,"totalSteps":0,"wind":0,"startTimeString":"30.11.2017, 10:31:43","stopTimeString":"30.11.2017, 10:31:47"},"-L-CxTsASh5pC9wv2Li4":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":32,"elevation":0,"location":"Lat: 48.3709940033504, Long: 14.514109399612822","startTimestamp":1512062676330,"stopTimestamp":1512062709167,"totalSteps":0,"weather":{"main":{"humidity":86,"temp":1,"temp_max":1,"temp_min":1},"wind":{"deg":240,"speed":4.1}},"startTimeString":"30.11.2017, 18:24:36","stopTimeString":"30.11.2017, 18:25:09"},"-L-D1Rx07lyhZZ272Q4i":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":36,"elevation":0,"location":"Lat: 48.37090873652961, Long: 14.51439645556685","locationLat":47.37090873652961,"locationLong":15.51439645556685,"startTimestamp":1512063974855,"stopTimestamp":1512064012010,"totalSteps":0,"weather":{"main":{"humidity":86,"temp":1,"temp_max":1,"temp_min":1},"wind":{"deg":230,"speed":3.6}},"startTimeString":"30.11.2017, 18:46:14","stopTimeString":"30.11.2017, 18:46:52"},"-L-DSXHqC7YM5IvK8YKq":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":20,"elevation":0,"location":"Lat: 48.3707372, Long: 14.5143459","locationLat":48.3707372,"locationLong":14.5143459,"startTimestamp":1512071090517,"stopTimestamp":1512071111718,"totalSteps":10,"weather":{"main":{"humidity":86,"temp":1,"temp_max":1,"temp_min":1},"wind":{"deg":220,"speed":3.1}},"startTimeString":"30.11.2017, 20:44:50","stopTimeString":"30.11.2017, 20:45:11"}}'));

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

    var data = [];

    $.each(allTours, function (tourId)
    {
        data.push({text: allTours[tourId]['startTimeString'], data: tourId});
    });

    $('#tourList').html(tourListTemplateFn(data));
    $('.nav-link').click(function () {
        tourSelected($(this).attr('data'));
    });
}

function processTourData(tours) {
    $.each(tours, function (tourId)
    {
        tours[tourId]['startTimeString'] = timestampToDateString(tours[tourId]['startTimestamp']);
        tours[tourId]['stopTimeString'] = timestampToDateString(tours[tourId]['stopTimestamp']);
    });

    return tours;
}

function tourSelected(tourId) {
    $('#tourDetailsTitle').text('Your Tour from ' + allTours[tourId]['startTimeString']);

    var tableOverviewRows = [
        ['StartTime', allTours[tourId]['startTimeString']],
        ['StopTime', allTours[tourId]['stopTimeString']]
    ];

    $('#tourOverviewTable').html(keyValueTableTemplateFn(tableOverviewRows));

    var lat = allTours[tourId]['locationLat'];
    var long = allTours[tourId]['locationLong'];

    if (lat != undefined && long != undefined)
    {
        centerMap(lat, long);
    }

    $('#tourDetails').show();
}

function centerMap(lat, long, ) {
    var location = {lat: lat, lng: long};
    var zoom = 8;

    gMap.setCenter(location);
    gMap.setZoom(zoom);

    if(gMarker != undefined)
    {
        gMarker.setMap(null);
    }

    gMarker = new google.maps.Marker({
        position: location,
        map: gMap
    });
}

function timestampToDateString(timestamp) {
    date = new Date(timestamp);
    return date.toLocaleString();
}