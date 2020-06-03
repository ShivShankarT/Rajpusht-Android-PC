package in.rajpusht.pc.ui.benef_list;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.rajpusht.pc.R;
import in.rajpusht.pc.model.BefModel;
import in.rajpusht.pc.utils.AppDateTimeUtils;

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
        BefModel befModel = mValues.get(position);
        holder.mItem = befModel;


        if (befModel.getStage().equals("PW")) {
            holder.benf_name.setText(befModel.getName());
            holder.benf_hus_name.setText("w/o:" + befModel.getHusbandName());
            holder.date.setText(R.string.lmp_date + AppDateTimeUtils.convertLocalDate(befModel.getLmpDate()));
        } else {
            holder.benf_name.setText(befModel.getMotherName());
            holder.benf_hus_name.setText("w/o:" + befModel.getHusbandName());
            holder.date.setText(R.string.dob + AppDateTimeUtils.convertLocalDate(befModel.getDob()));
        }

        holder.buttonststus.setText(befModel.getCurrentSubStage());

        if (!TextUtils.isEmpty(befModel.getPctsId())) {
            holder.pctsId.setVisibility(View.VISIBLE);
            holder.pctsId.setText(R.string.pcts_id + befModel.getPctsId());
        } else {
            holder.pctsId.setVisibility(View.GONE);
        }

        Context context = holder.itemView.getContext();
        if (befModel.getPwFormId() != null || befModel.getLmFormId() != null) {
            if (position == 3)
                holder.img_synced.setImageResource(R.drawable.ic_done_all);
            holder.buttonststus.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_green));
            holder.img_delete.setVisibility(View.GONE);
        } else {
            holder.img_delete.setVisibility(View.GONE);//todo
            holder.buttonststus.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
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
