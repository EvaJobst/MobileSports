package at.fhooe.mos.mountaineer.ui.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.greenrobot.eventbus.Subscribe;

import at.fhooe.mos.mountaineer.EventSource;
import at.fhooe.mos.mountaineer.NameChangedEventListener;
import at.fhooe.mos.mountaineer.R;
import at.fhooe.mos.mountaineer.model.tour.Tour;
import at.fhooe.mos.mountaineer.services.TourDataCollector;
import at.fhooe.mos.mountaineer.services.TourRecorderService;

public class EditNameDialog extends DialogFragment {
    private EventSource<NameChangedEventListener> nameChangedEventListener;

    public EditNameDialog() {
        //EventBus.getDefault().register(this);
        nameChangedEventListener = new EventSource<NameChangedEventListener>() {};
        nameChangedEventListener.registerListener(TourRecorderService.tourDataCollector);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // 2. Chain together various setter methods to set the dialog characteristics
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_text_input, null);
        final EditText tourName = view.findViewById(R.id.tourName);

        builder.setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (NameChangedEventListener listener : nameChangedEventListener.getEventListeners()) {
                            listener.onNameChangedEvent(tourName.getText().toString());
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });

        // 3. Get the AlertDialog from create()
        return builder.create();
    }
}
