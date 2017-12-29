package at.fhooe.mos.mountaineer;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import at.fhooe.mos.mountaineer.model.tour.Tour;
import at.fhooe.mos.mountaineer.model.tour.TourDataFormatter;


public class TourPreviewAdapter extends RecyclerView.Adapter<TourPreviewAdapter.TourPreviewViewHolder> {
    TourDataFormatter tourDataFormatter = new TourDataFormatter();
    public ArrayList<Tour> tours;
    Activity activity;

    public TourPreviewAdapter(@NonNull Activity activity, @NonNull ArrayList<Tour> tours) {
        this.activity = activity;
        this.tours = tours;
    }

    @Override
    public TourPreviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_completed_tour, parent, false);
        return new TourPreviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TourPreviewViewHolder holder, int position) {
        final Tour tour = tours.get(position);

        holder.previewTitle.setText(tourDataFormatter.getName(tour, null));
        holder.previewImage.setImageBitmap(tourDataFormatter.getImage(tour, null, activity));
        holder.previewDate.setText(tourDataFormatter.getDate(tour));
        holder.previewDetails.setText(tourDataFormatter.getPreviewDetails(tour));
        holder.previewLocation.setText(tourDataFormatter.getLocation(tour));
        holder.previewDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Selected: " + tour.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tours.size();
    }

    class TourPreviewViewHolder extends RecyclerView.ViewHolder {

        protected ImageView previewImage;
        protected TextView previewTitle;
        protected TextView previewDate;
        protected TextView previewLocation;
        protected TextView previewDetails;
        protected Button previewDetailsButton;

        TourPreviewViewHolder(View itemView) {
            super(itemView);
            previewImage = itemView.findViewById(R.id.previewImage);
            previewTitle = itemView.findViewById(R.id.previewTitle);
            previewDate = itemView.findViewById(R.id.previewDate);
            previewLocation = itemView.findViewById(R.id.previewLocation);
            previewDetails = itemView.findViewById(R.id.previewDetails);
            previewDetailsButton = itemView.findViewById(R.id.previewDetailsButton);
        }
    }
}
