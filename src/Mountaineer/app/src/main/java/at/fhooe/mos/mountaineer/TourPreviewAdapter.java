package at.fhooe.mos.mountaineer;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import at.fhooe.mos.mountaineer.model.tour.Tour;


public class TourPreviewAdapter extends RecyclerView.Adapter<TourPreviewAdapter.TourPreviewViewHolder> {
    public ArrayList<Tour> tours;
    private Activity activity;

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

        holder.previewTitle.setText(tour.getName());
        holder.previewDate.setText("Not available");
        holder.previewDistance.setText(String.valueOf(tour.getDistance()));
        holder.previewDuration.setText(String.valueOf(tour.getDuration()));
        holder.previewLocation.setText(tour.getWeather().getName());
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
        protected TextView previewDuration;
        protected TextView previewDistance;
        protected Button previewDetailsButton;

        TourPreviewViewHolder(View itemView) {
            super(itemView);
            previewImage = itemView.findViewById(R.id.previewImage);
            previewTitle = itemView.findViewById(R.id.previewTitle);
            previewDate = itemView.findViewById(R.id.previewDate);
            previewLocation = itemView.findViewById(R.id.previewLocation);
            previewDuration = itemView.findViewById(R.id.previewDuration);
            previewDistance = itemView.findViewById(R.id.previewDistance);
            previewDetailsButton = itemView.findViewById(R.id.previewDetailsButton);
        }
    }
}
