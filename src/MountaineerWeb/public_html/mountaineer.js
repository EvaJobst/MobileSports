var config = {
    apiKey: 'AIzaSyCEBS4uXwZGmYJGvWFfFgxQUVafhbvIyZQ',
    authDomain: 'mosproject-fdce4.firebaseapp.com',
    databaseURL: 'https://mosproject-fdce4.firebaseio.com',
    projectId: 'mosproject-fdce4',
    storageBucket: 'mosproject-fdce4.appspot.com',
    messagingSenderId: '563541673510'
};

firebase.initializeApp(config);

var allTours;
$(document).ready(initPage);

var keyValueTableTemplateFn;
var tourListTemplateFn;

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
    toursLoaded(JSON.parse('{"-L-BFSktECnlidMbLfBx":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":3,"elevation":0,"humidity":0,"maxTemp":0,"minTemp":0,"rain":0,"startTimestamp":1512034127190,"stopTimestamp":1512034130952,"totalSteps":0,"wind":0},"-L-BG7rLKDoKF1DKZapY":{"averageHeartRate":0,"averageRespiration":0,"averageSpeed":0,"averageSteps":0,"burnedKcal":0,"distance":0,"duration":3,"elevation":0,"humidity":0,"maxTemp":0,"minTemp":0,"rain":0,"startTimestamp":1512034303813,"stopTimestamp":1512034307488,"totalSteps":0,"wind":0}}'));
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

    $('#tourDetails').show();
}

function timestampToDateString(timestamp) {
    date = new Date(timestamp);
    return date.toLocaleString();
}