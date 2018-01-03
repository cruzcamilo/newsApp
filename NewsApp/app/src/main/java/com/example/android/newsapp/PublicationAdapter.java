package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PublicationAdapter extends ArrayAdapter<Publication> {

    public PublicationAdapter(Context context, ArrayList<Publication> publications) {
        super(context, 0, publications);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Publication currentPublication = getItem(position);

        TextView sectionView = (TextView) listItemView.findViewById(R.id.section);

        if (currentPublication.getSection() != "") {
            sectionView.setText(currentPublication.getSection());
        } else {
            sectionView.setText(R.string.no_section);
        }

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);

        if (currentPublication.getPublicationDate() != "") {
            String date = formattedDate(currentPublication.getPublicationDate());
            dateView.setText(date);
        } else {
            dateView.setText(R.string.no_date);
        }

        TextView titleView = (TextView) listItemView.findViewById(R.id.title);

        if (currentPublication.getPublicationDate() != "") {
            titleView.setText(currentPublication.getTitle());
        } else {
            titleView.setText(R.string.no_title);
        }

        ImageView thumbnail = (ImageView) listItemView.findViewById(R.id.thumbnail);

        if (currentPublication.getThumbnail() != "") {

            thumbnail.setImageBitmap(currentPublication.getThumbnailImage());
        } else {
            thumbnail.setImageResource(R.drawable.noimageicon);
        }

        TextView trailTextView = (TextView) listItemView.findViewById(R.id.trailText);
        trailTextView.setText(currentPublication.getTrailText());

        return listItemView;
    }

    private String formattedDate(String date) {
        String dateToView = date;
        dateToView = dateToView.replace("T", " - ");
        dateToView = dateToView.replace("Z", "");

        return dateToView;
    }
}