package pradadesigners.com.tequesta;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
        parkStatusText.setText(park.getLastUpdated() + " " + park.getComment());

        ImageView parkStatusIcon = (ImageView) convertView.findViewById(R.id.statusIcon);

        int parkOpenIcon   = android.R.drawable.ic_media_play;
        int parkClosedIcon = android.R.drawable.ic_dialog_alert;

        int parkIcon = park.isOpen() ? parkOpenIcon: parkClosedIcon;
        parkStatusIcon.setImageResource(parkIcon);

        return convertView;
    }
}