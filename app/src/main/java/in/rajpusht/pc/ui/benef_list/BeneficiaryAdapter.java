package in.rajpusht.pc.ui.benef_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.rajpusht.pc.R;
import in.rajpusht.pc.model.BefModel;

public class BeneficiaryAdapter extends RecyclerView.Adapter<BeneficiaryAdapter.ViewHolder> {

    private final List<BefModel> mValues;
    private final OnListFragmentInteractionListener mListener;

    public BeneficiaryAdapter(List<BefModel> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_beneficiart_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        BefModel BefModel = mValues.get(position);
        holder.mItem = BefModel;
        holder.benf_name.setText(BefModel.getName());

        if (BefModel.getStage().equals("PW")) {
            holder.benf_hus_name.setText("w/o:" + BefModel.getName());
            holder.date.setText("LMP Date:" + BefModel.getLmpDate());
        } else {
            holder.benf_hus_name.setText("m/o:" + BefModel.getName());
            holder.date.setText("DOB:" + BefModel.getDob());
        }

        holder.buttonststus.setText(BefModel.getCurrentSubStage());

        if (BefModel.getCurrentSubStage().equals(BefModel.getSubStage())) {
            if (position == 3)
                holder.img_synced.setImageResource(R.drawable.ic_done_all);
            holder.buttonststus.setBackgroundColor(holder.itemView.getContext().getColor(R.color.dark_green));
            holder.img_delete.setVisibility(View.GONE);
        } else {
            holder.img_delete.setVisibility(View.VISIBLE);
            holder.buttonststus.setBackgroundColor(holder.itemView.getContext().getColor(R.color.colorAccent));
        }

        holder.item_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(BefModel item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View item_holder;
        private final TextView benf_name;
        private final TextView benf_hus_name;
        private final TextView date;
        private final TextView pctsId;
        private final ImageView img_synced;
        private final ImageView img_delete;
        private final Button buttonststus;
        public BefModel mItem;

        public ViewHolder(View view) {
            super(view);
            item_holder = view.findViewById(R.id.item_holder);
            benf_name = view.findViewById(R.id.benf_name);
            benf_hus_name = view.findViewById(R.id.benf_hus_name);
            date = view.findViewById(R.id.date);
            pctsId = view.findViewById(R.id.pctsId);
            img_synced = view.findViewById(R.id.img_synced);
            img_delete = view.findViewById(R.id.img_delete);
            buttonststus = view.findViewById(R.id.buttonststus);
        }


    }
}
