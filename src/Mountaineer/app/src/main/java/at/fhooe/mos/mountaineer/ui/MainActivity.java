package at.fhooe.mos.mountaineer.ui;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;

import at.fhooe.mos.mountaineer.R;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main_activity_menu)
public class MainActivity extends AppCompatActivity {

    @ViewById
    Toolbar mainToolbar;

    @OptionsMenuItem
    protected MenuItem mainActivityMenuItem;

    @ViewById
    FloatingActionButton addTourButton;

    @Click(R.id.addTourButton)
    public void onAddTourButtonClick() {
        Intent i = new Intent(this, TourActivity_.class);
        startActivity(i);
    }

    @OptionsItem(R.id.mainActivityMenuItem)
    protected void onOptionsItemClicked() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setSupportActionBar(mainToolbar);
    }
}
