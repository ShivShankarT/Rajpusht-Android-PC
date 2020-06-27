package in.rajpusht.pc.ui.profile;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import in.rajpusht.pc.R;
import in.rajpusht.pc.data.local.db.entity.AssignedLocationEntity;
import in.rajpusht.pc.utils.ExpandableRecyclerAdapter;

public class AwcLocationAdapter extends ExpandableRecyclerAdapter<ProfileFragment.AssignedLocationListItem> {


    private String selectedAWC = null;

    private AWCChangeLister awcChangeLister;

    public AwcLocationAdapter(Context context, AWCChangeLister awcChangeLister) {
        super(context);
        this.awcChangeLister = awcChangeLister;
    }

    public void setSelectedAWC(String selectedAWC) {
        this.selectedAWC = selectedAWC;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ExpandableRecyclerAdapter.TYPE_HEADER) {

            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_header, parent, false));
        } else {
            return new AwcViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.assigned_location_awc_list_item, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ExpandableRecyclerAdapter.ViewHolder holder, int position) {

        if (holder instanceof ExpandableRecyclerAdapter.HeaderViewHolder) {
            HeaderViewHolder holder1 = (HeaderViewHolder) holder;
            holder1.bind(position);
        } else if (holder instanceof AwcViewHolder) {
            AssignedLocationEntity locationEntity = visibleItems.get(position).assignedLocationEntity;
            AwcViewHolder holder1 = (AwcViewHolder) holder;
            holder1.textviewLocation.setText(locationEntity.getAwcEnglishName());
            holder1.textviewSector.setText(locationEntity.getSectorName());

            if (locationEntity.getPwCount() != null) {
                holder1.pw_count.setText(String.valueOf(locationEntity.getPwCount()));
            } else {
                holder1.pw_count.setText("0");

            }

            if (locationEntity.getLmCount() != null) {
                holder1.lm_count.setText(String.valueOf(locationEntity.getLmCount()));
            } else {
                holder1.lm_count.setText("0");

            }

            if (locationEntity.getMyCount() != null) {
                holder1.my_count.setText(String.valueOf(locationEntity.getMyCount()));
            } else {
                holder1.my_count.setText("0");

            }
            holder1.radiobutton.setOnClickListener(null);
            if (locationEntity.getAwcCode().equals(selectedAWC)) {
                holder1.radiobutton.setChecked(true);
            } else {
                holder1.radiobutton.setChecked(false);
            }
            holder1.radiobutton.setOnClickListener(holder1);
        }

    }

    public interface AWCChangeLister {
        void onAwcChange(String awcCode, String awcName);
    }

    public class AwcViewHolder extends ExpandableRecyclerAdapter.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
        private final TextView textviewLocation;
        private final TextView textviewSector;
        private final RadioButton radiobutton;
        private final TextView lm_count;
        private final TextView my_count;
        private final TextView pw_count;

        public AwcViewHolder(View view) {
            super(view);
            textviewLocation = view.findViewById(R.id.textviewLocation);
            textviewSector = view.findViewById(R.id.textviewSector);
            radiobutton = view.findViewById(R.id.radiobutton);
            pw_count = view.findViewById(R.id.pw_count);
            lm_count = view.findViewById(R.id.lm_count);
            my_count = view.findViewById(R.id.my_count);
            radiobutton.setOnCheckedChangeListener(this);
            this.itemView.setOnClickListener(this);

        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
                onClick(null);//todo check

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            if (adapterPosition != -1) {
                AssignedLocationEntity locationEntity = visibleItems.get(adapterPosition).assignedLocationEntity;
                selectedAWC = locationEntity.getAwcCode();
                awcChangeLister.onAwcChange(locationEntity.getAwcCode(), locationEntity.getAwcEnglishName());
                new Handler().postDelayed(AwcLocationAdapter.this::notifyDataSetChanged, 500);

            }
        }
    }

    private class HeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder {
        TextView name;
        ImageView imageView;

        public HeaderViewHolder(View view) {
            super(view, view.findViewById(R.id.item_arrow));
            name = view.findViewById(R.id.item_header_name);
            imageView = view.findViewById(R.id.item_arrow);

        }

        public void bind(int position) {
            super.bind(position);
            String title = visibleItems.get(position).title;
            name.setText(title);
        }
    }


}
