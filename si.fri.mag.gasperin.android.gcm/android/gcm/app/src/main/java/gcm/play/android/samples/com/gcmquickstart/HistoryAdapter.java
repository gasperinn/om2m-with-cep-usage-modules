package gcm.play.android.samples.com.gcmquickstart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class HistoryAdapter extends BaseAdapter {

    Context context;
    ArrayList<SensorData> data;
    private static LayoutInflater inflater = null;

    public HistoryAdapter(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        data = new ArrayList<SensorData>();
    }

    public void add(SensorData sensorData){
        this.data.add(sensorData);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row_gcm_list, null);

        TextView tvTitle = (TextView) vi.findViewById(R.id.rowTitle);
        TextView tvSystolic = (TextView) vi.findViewById(R.id.rowSystolic);
        TextView tvDiastolic = (TextView) vi.findViewById(R.id.rowDiastolic);
        TextView tvX = (TextView) vi.findViewById(R.id.rowX);
        TextView tvY = (TextView) vi.findViewById(R.id.rowY);
        TextView tvZ = (TextView) vi.findViewById(R.id.rowZ);

        tvTitle.setText(data.size()-position + ". " + data.get(position).title);
        tvSystolic.setText(data.get(position).systolic);
        tvDiastolic.setText(data.get(position).diastolic);
        tvX.setText(data.get(position).x);
        tvY.setText(data.get(position).y);
        tvZ.setText(data.get(position).z);

        return vi;
    }
}