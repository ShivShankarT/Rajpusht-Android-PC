package in.rajpusht.pc.ui.image_banner;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.piasy.biv.indicator.progresspie.ProgressPieIndicator;
import com.github.piasy.biv.view.BigImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.rajpusht.pc.R;
import in.rajpusht.pc.model.Image;

public class BannerImageViewFragment extends Fragment {

    private static final String ARG_ITEM_COUNT = "item_count";
    List<Image> image = Collections.emptyList();
    RecyclerView thumbImRv;
    private Listener mListener;
    private RecyclerView bigImRv;
    private ThumbImageItemAdapter thumbImageItemAdapter;
    private TextView title;

    public static BannerImageViewFragment newInstance(List<Image> image) {
        final BannerImageViewFragment fragment = new BannerImageViewFragment();
        final Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_ITEM_COUNT, new ArrayList<>(image));
        fragment.setArguments(args);
        return fragment;
    }

    public static void launch(FragmentManager supportFragmentManager, List<Image> image) {
        supportFragmentManager.beginTransaction().add(android.R.id.content, BannerImageViewFragment.newInstance(image),
                "tage").addToBackStack("txxag").commit();

    }

    public static void launch(FragmentManager supportFragmentManager, List<String> image, List<String> imageSm) {
        List<Image> imageList = new ArrayList<>(image.size());
        for (int i = 0; i < image.size(); i++) {
            Image imageMd = new Image();
            imageMd.setImageLg(image.get(i));
            imageMd.setImageSm(imageSm.get(i));
            imageList.add(imageMd);
        }
        launch(supportFragmentManager, imageList);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            image = getArguments().getParcelableArrayList(ARG_ITEM_COUNT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bigImRv = view.findViewById(R.id.list);
        title = view.findViewById(R.id.title);
        Toolbar toolbar_view = view.findViewById(R.id.toolbar);
        toolbar_view.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        bigImRv.setLayoutManager(layoutManager);
        bigImRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int pos;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (pos != firstVisibleItemPosition) {
                    thumbImageItemAdapter.setSelectedPos(firstVisibleItemPosition);
                    thumbImRv.scrollToPosition(firstVisibleItemPosition);
                    title.setText(image.get(firstVisibleItemPosition).getCaption());
                }
                pos = firstVisibleItemPosition;
            }
        });
        bigImRv.setAdapter(new ImageItemAdapter());
        new PagerSnapHelper().attachToRecyclerView(bigImRv);
        thumbImRv = view.findViewById(R.id.thumb_list);
        thumbImRv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        thumbImageItemAdapter = new ThumbImageItemAdapter();
        thumbImRv.setAdapter(thumbImageItemAdapter);
        new LinearSnapHelper().attachToRecyclerView(thumbImRv);
        title.setText(image.get(0).getCaption());

        if (!image.isEmpty() && image.get(0).getImageSm() == null) {
            view.findViewById(R.id.thumb_cont).setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
      /*  final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }*/
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onImageItemClicked(int position);
    }


    private class ImageItemAdapter extends RecyclerView.Adapter<ImageItemAdapter.ViewHolder> {
        private ImageItemAdapter() {
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bigImageView.setProgressIndicator(new ProgressPieIndicator());
            Uri parse = Uri.parse(image.get(position).getImageLg());
            holder.bigImageView.showImage(parse);


        }

        @Override
        public int getItemCount() {
            return image.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final BigImageView bigImageView;

            ViewHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.fragment_image_item, parent, false));
                bigImageView = itemView.findViewById(R.id.full_image);


            }
        }

    }

    private class ThumbImageItemAdapter extends RecyclerView.Adapter<ThumbImageItemAdapter.ViewHolder> {
        int pos = 0;

        private ThumbImageItemAdapter() {

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        public void setSelectedPos(int pos) {
            int prev = this.pos;
            this.pos = pos;
            notifyItemChanged(prev);
            notifyItemChanged(pos);

        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (pos == position)
                holder.bigImageView.setBackgroundColor(Color.YELLOW);
            else
                holder.bigImageView.setBackgroundColor(Color.WHITE);
            Glide.with(holder.itemView).load(image.get(position).getImageSm()).into(holder.bigImageView);
        }

        @Override
        public int getItemCount() {
            return image.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView bigImageView;

            ViewHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.fragment_thumb_image_item, parent, false));
                bigImageView = itemView.findViewById(R.id.thumb_iv);
                bigImageView.setOnClickListener(v -> {
                    bigImRv.scrollToPosition(getLayoutPosition());

                });


            }
        }

    }

}
