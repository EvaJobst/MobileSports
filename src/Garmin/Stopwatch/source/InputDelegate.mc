//
// Copyright 2016 by Garmin Ltd. or its subsidiaries.
// Subject to Garmin SDK License Agreement and Wearables
// Application Developer Agreement.
//

using Toybox.WatchUi as Ui;
using Toybox.Graphics as Gfx;
using Toybox.System as Sys;

var last_key = null;
var last_behavior = null;

enum {
    ON_NEXT_PAGE,
    ON_PREV_PAGE,
    ON_MENU,
    ON_BACK,
    ON_NEXT_MODE,
    ON_PREV_MODE,
    ON_SELECT,
    ON_TAP
}

class InputDelegate extends Ui.BehaviorDelegate {

    function initialize() {
        BehaviorDelegate.initialize();
    }

    function onMenu() {
        last_behavior = ON_MENU;
        newInput(ON_MENU);
        return false;
    }

    function onBack() {
        if (ON_BACK == last_behavior) {
            Sys.exit();
        }
        last_behavior = ON_BACK;
        newInput(ON_BACK);
        return false;
    }

    function onSelect() {
        last_behavior = ON_SELECT;
        newInput(ON_SELECT);
        return false;
    }

    function onTap(evt) {
        newInput(ON_TAP);
        return true;
    }

    function onSwipe(evt) {
        var swipe = evt.getDirection();

        newInput(swipe);
      
        return true;
    }

    function onKey(evt) {
        var key = evt.getKey();

        if (key == KEY_ESC) {
            if (last_key == KEY_ESC) {
                Sys.exit();
            }
        }

        last_key = key;

        return true;
    }

}
