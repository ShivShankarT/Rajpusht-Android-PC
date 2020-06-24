package in.rajpusht.pc.ui.sync;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.rajpusht.pc.R;
import in.rajpusht.pc.model.AwcSyncCount;
import in.rajpusht.pc.model.DataStatus;

public class AWCSyncAdapter extends RecyclerView.Adapter<AWCSyncAdapter.ViewHolder> {

    private List<AwcSyncCount> awcSyncCounts = new ArrayList<>();

    public void setAwcSyncCounts(List<AwcSyncCount> awcSyncCounts) {
        this.awcSyncCounts = awcSyncCounts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.awc_sync_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AwcSyncCount awcSyncCount = awcSyncCounts.get(position);

        holder.awc_name.setText(awcSyncCount.getAwcEnglishName() + "\n" + awcSyncCount.getAwcCode());

        Context context=holder.awc_name.getContext();
        String data = "";
        if ("Y".equals(awcSyncCount.getIsMother()))
            data = context.getString(R.string.Women);
        else if ("N".equals(awcSyncCount.getIsMother()))
            data = context.getString(R.string.Child);

        String staus = "-";
        if (awcSyncCount.getDataStatus() != null) {
            if (awcSyncCount.getDataStatus() == DataStatus.NEW) {
                staus = context.getString(R.string.New);
            } else if (awcSyncCount.getDataStatus() == DataStatus.EDIT) {
                staus = context.getString(R.string.Edit);
            } else if (awcSyncCount.getDataStatus() == DataStatus.OLD) {
                staus = "NOC";
            }
        }

        holder.awc_bef.setText(staus + " " + data);
        holder.awc_bef_count.setText("" + awcSyncCount.getCount());
    }

    @Override
    public int getItemCount() {
        return awcSyncCounts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView awc_name, awc_bef, awc_bef_count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            awc_name = itemView.findViewById(R.id.awc_name);
            awc_bef = itemView.findViewById(R.id.awc_bef);
            awc_bef_count = itemView.findViewById(R.id.awc_bef_count);
        }
    }


}
