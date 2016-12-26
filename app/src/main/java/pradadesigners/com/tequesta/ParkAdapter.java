package pradadesigners.com.tequesta;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * Created by dprada on 12/25/16.
 */

public class ParkAdapter extends ArrayAdapter<Park> {

    public ParkAdapter(Context context, List<Park> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Park park = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list_view, parent, false);
        }

        TextView parkLabel = (TextView) convertView.findViewById(R.id.parkName);
        parkLabel.setText(park.getName());

        TextView parkStatusText = (TextView) convertView.findViewById(R.id.parkStatusText);
        parkStatusText.setText(park.getComment());

        ImageView parkStatusIcon = (ImageView) convertView.findViewById(R.id.statusIcon);

        Drawable parkOpenIcon   = ContextCompat.getDrawable(getContext(), R.drawable.ic_insert_emoticon_white_36dp);
        Drawable parkClosedIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_cancel_white_36dp);

        Drawable parkIcon = park.isOpen() ? parkOpenIcon: parkClosedIcon;
        parkStatusIcon.setImageDrawable(parkIcon);

        TextView lastUpdatedText = (TextView) convertView.findViewById(R.id.lastUpdated);
        lastUpdatedText.setText(park.getLastUpdated());

        return convertView;
    }
}