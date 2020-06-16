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
import in.rajpusht.pc.model.DataStatus;
import in.rajpusht.pc.utils.AppDateTimeUtils;
import in.rajpusht.pc.utils.FormDataConstant;

public class BeneficiaryAdapter extends RecyclerView.Adapter<BeneficiaryAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;
    private boolean isOtherWomen = false;
    private List<BefModel> mValues;

    public BeneficiaryAdapter(List<BefModel> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void setOtherWomen(boolean otherWomen) {
        isOtherWomen = otherWomen;
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
            holder.date.setText("LMP Date :" + AppDateTimeUtils.convertLocalDate(befModel.getLmpDate()));
        } else {
            holder.benf_name.setText(befModel.getMotherName());
            holder.date.setText("DOB :" + AppDateTimeUtils.convertLocalDate(befModel.getDob()));
        }
        holder.benf_hus_name.setText("w/o:" + (TextUtils.isEmpty(befModel.getHusbandName()) ? "-" : befModel.getHusbandName()));
        holder.buttonststus.setText(befModel.getCurrentSubStage());

        if (!TextUtils.isEmpty(befModel.getPctsId())) {
            holder.pctsId.setVisibility(View.VISIBLE);
            holder.pctsId.setText("PCTS ID :" + befModel.getPctsId());
        } else {
            holder.pctsId.setText("PCTS ID :-");

        }

        Context context = holder.itemView.getContext();
        if (befModel.getPwFormId() != null || befModel.getLmFormId() != null) {
            holder.buttonststus.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_green));
            holder.img_delete.setVisibility(View.GONE);
        } else {
            holder.img_delete.setVisibility(View.GONE);//todo
            holder.buttonststus.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        }

        if (befModel.getDataStatus() != null && befModel.getDataStatus() == DataStatus.OLD) {
            holder.img_synced.setImageResource(R.drawable.ic_done_all);
        } else {
            holder.img_synced.setImageResource(R.drawable.ic_done);
        }

        if (FormDataConstant.ANC_NOT_COMPLETED.equalsIgnoreCase(befModel.getNaReason())) {
            holder.ic_flag.setVisibility(View.VISIBLE);
        } else {
            holder.ic_flag.setVisibility(View.GONE);
        }

        if (isOtherWomen) {
            holder.layout_na_reason.setVisibility(View.VISIBLE);
            if (befModel.getNaReason() != null)
                switch (befModel.getNaReason()) {
                    case "MA":
                        holder.txt_na_reason.setText(R.string.pw_na1);
                        break;
                    case "WD":
                        holder.txt_na_reason.setText(R.string.pw_na2);
                        break;
                    case "WM":
                        holder.txt_na_reason.setText(R.string.pw_na3);
                        break;
                    case "AC":
                        holder.txt_na_reason.setText("ANC not completed");
                        break;
                    //child LM/MY

                    case "MD":
                        holder.txt_na_reason.setText(R.string.lm_na1);
                        break;
                    case "MM":
                        holder.txt_na_reason.setText(R.string.lm_na2);
                        break;
                    case "CD":
                        holder.txt_na_reason.setText(R.string.lm_na3);
                        break;
                    case "CM":
                        holder.txt_na_reason.setText(R.string.lm_na4);
                        break;
                    case "BD":
                        holder.txt_na_reason.setText(R.string.lm_na5);
                        break;
                    case "BM":
                        holder.txt_na_reason.setText(R.string.lm_na6);
                        break;


                }
        }

        //NA_REASON AT PW STAGE:
        //"MA" // Miscarriage/ abortion
        //"WD" // women death
        //"WM" // women migrated
        //"AC" //anc not completed
        //
        //NA_REASON AT LM/MY STAGE:
        //"MD" //mother death
        //"MM" //mother migrated
        //"CD" //child death
        //"CM" //child migrated
        //"BD" //both death
        //"BM" //both migrated

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

    public void setValues(List<BefModel> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged();
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
        private final ImageView ic_flag;
        private final Button buttonststus;
        private final ViewGroup layout_na_reason;
        private final TextView txt_na_reason;
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
            ic_flag = view.findViewById(R.id.ic_flag);
            buttonststus = view.findViewById(R.id.buttonststus);
            layout_na_reason = view.findViewById(R.id.layout_na_reason);
            txt_na_reason = view.findViewById(R.id.txt_na_reason);
        }


    }
}
