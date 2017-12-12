package at.fhooe.mos.mountaineer.ui.fragment;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import at.fhooe.mos.mountaineer.R;

public class EditNameDialog extends DialogFragment {
    private NameChangedEventListener nameChangedEventListener;

    public void registerListener(NameChangedEventListener nameChangedEventListener) {
        this.nameChangedEventListener = nameChangedEventListener;
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
                        if(nameChangedEventListener != null){
                            nameChangedEventListener.onNameChangedEvent(tourName.getText().toString());
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
        Dialog dialog = builder.create();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        return dialog;
    }

    public interface NameChangedEventListener {
        void onNameChangedEvent(String name);
    }
}
