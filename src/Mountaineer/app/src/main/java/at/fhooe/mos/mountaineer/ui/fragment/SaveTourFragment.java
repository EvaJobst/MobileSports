package at.fhooe.mos.mountaineer.ui.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import at.fhooe.mos.mountaineer.FirebaseManager;
import at.fhooe.mos.mountaineer.PersistenceManager;
import at.fhooe.mos.mountaineer.R;
import at.fhooe.mos.mountaineer.services.TourDataCollector;

@EFragment(R.layout.fragment_save_tour)
public class SaveTourFragment extends Fragment {
    private int requestCodeGallery = 123;
    private String selectedImagePath;

    private PersistenceManager persistenceManager;

    @ViewById
    protected TextInputEditText addTitleToTour;

    @ViewById
    protected FloatingActionButton addGalleryImageButton;

    @Override
    public void onStart() {
        super.onStart();

        persistenceManager = new PersistenceManager(getContext());
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);

        super.onStop();
    }

    @Click
    protected void addGalleryImageButtonClicked() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, requestCodeGallery);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == requestCodeGallery && data != null) {
            Uri selectedImageUri = data.getData();
            selectedImagePath = getPath(selectedImageUri);
            Toast.makeText(getContext(), "Image selected!", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onMessageEvent(TourDataCollector.FinalTourDataEvent event) {
        String userId = persistenceManager.getUserId();
        FirebaseManager firebaseManager = new FirebaseManager(userId);
        firebaseManager.addTour(event.getTour());

        Toast.makeText(getContext(), "tour saved!", Toast.LENGTH_SHORT).show();
    }

    private String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }

    public SaveTourFragment() {
        // Required empty public constructor
    }
}
