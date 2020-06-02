package in.rajpusht.pc.ui.animation;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import in.rajpusht.pc.R;
import in.rajpusht.pc.databinding.FragmentTestAnimationBinding;
import in.rajpusht.pc.model.CounsellingMedia;
import in.rajpusht.pc.ui.benef_list.BeneficiaryFragment;
import in.rajpusht.pc.ui.pregnancy_graph.PregnancyGraphFragment;
import in.rajpusht.pc.utils.FragmentUtils;


public class CounsellingAnimationFragment extends Fragment {


    private static int finalHeight = Target.SIZE_ORIGINAL;
    private static int finalWidth = Target.SIZE_ORIGINAL;
    private Handler handler = new Handler();
    private CounsellingMedia counsellingMedia;
    private int mediaPos = 0;
    private int pos = 0;
    private FragmentTestAnimationBinding vb;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPos < counsellingMedia.getMediaImage().size()) {
                // Picasso.get().load(counsellingMedia.getMediaImage().get(mediaPos)).noPlaceholder().into(vb.imageview);
                Glide.with(getContext()).asBitmap()
                        .load(counsellingMedia.getMediaImage().get(mediaPos))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .dontAnimate()
                        .into(new SimpleTarget<Bitmap>(finalWidth, finalHeight) {

                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                vb.imageview.setImageBitmap(resource);
                            }
                        });
                mediaPos++;
                handler.postDelayed(runnable, 2000);
            } else {
                TransitionManager.beginDelayedTransition(vb.ll);
                vb.nxtBtn.setVisibility(View.VISIBLE);
            }
        }
    };
    private int videoCurrentPos = 0;


    public CounsellingAnimationFragment() {
        // Required empty public constructor
    }

    public static CounsellingAnimationFragment newInstance(int pos) {
        CounsellingAnimationFragment testAnimationFragment = new CounsellingAnimationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("data", pos);
        testAnimationFragment.setArguments(bundle);
        return testAnimationFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pos = getArguments().getInt("data");
            counsellingMedia = CounsellingMedia.counsellingMediaData().get(pos);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vb = FragmentTestAnimationBinding.inflate(inflater, container, false);
        return vb.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        vb.toolbarLy.toolbar.setTitle("PW Women Counselling");
        vb.nxtBtn.setVisibility(View.INVISIBLE);
        ViewTreeObserver vto = vb.imageview.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {

                vb.imageview.getViewTreeObserver().removeOnPreDrawListener(this);
                finalHeight = vb.imageview.getMeasuredHeight();
                finalWidth = vb.imageview.getMeasuredWidth();
                return true;
            }
        });
        if (counsellingMedia.getType() == CounsellingMedia.VIDEO_MEDIA) {

            vb.nxtBtn.setVisibility(View.GONE);
            //vb.toolbarLy.appBarLy.setVisibility(View.GONE);
            VideoView videoView = vb.videoView;
            videoView.setVisibility(View.VISIBLE);
            vb.imageview.setVisibility(View.GONE);
            //Creating MediaController
            MediaController mediaController = new MediaController(requireContext());
            mediaController.setAnchorView(videoView);
            //specify the location of media file
            Uri uri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + counsellingMedia.getMediaImage().get(0));
            //Setting MediaController and URI, then starting the videoView
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(uri);
            videoView.requestFocus();
            if (videoCurrentPos > 0)
                videoView.seekTo(videoCurrentPos);
            videoView.start();
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    TransitionManager.beginDelayedTransition(vb.ll);
                    vb.nxtBtn.setVisibility(View.VISIBLE);
                }
            });


        } else {
            handler.postDelayed(runnable, 10);
        }

        int nextCounPos = pos + 1;
        if (!(nextCounPos < CounsellingMedia.counsellingMediaData().size())) {
            vb.nxtBtn.setText("Finish");
        }

        vb.nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int nextCounPos = pos + 1;
                if (nextCounPos < CounsellingMedia.counsellingMediaData().size()) {
                    CounsellingMedia nextCounsellingMedia = CounsellingMedia.counsellingMediaData().get(nextCounPos);

                    if (nextCounsellingMedia.getType() == CounsellingMedia.IMAGE_MEDIA || nextCounsellingMedia.getType() == CounsellingMedia.VIDEO_MEDIA) {
                        FragmentUtils.replaceFragment(requireActivity(), CounsellingAnimationFragment.newInstance(nextCounPos), R.id.fragment_container, true, FragmentUtils.TRANSITION_FADE_IN_OUT);
                    } else {
                        FragmentUtils.replaceFragment(requireActivity(), PregnancyGraphFragment.newInstance(), R.id.fragment_container, true, FragmentUtils.TRANSITION_FADE_IN_OUT);
                    }
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .remove(CounsellingAnimationFragment.this)
                            .commit();

                } else {

                    showAlertDialog("PW Women Counselling Completed !!!", new Runnable() {
                        @Override
                        public void run() {
                            FragmentUtils.replaceFragment(requireActivity(), new BeneficiaryFragment(), R.id.fragment_container, false, FragmentUtils.TRANSITION_FADE_IN_OUT);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (counsellingMedia.getType() == CounsellingMedia.VIDEO_MEDIA) {
            videoCurrentPos = vb.videoView.getCurrentPosition();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (counsellingMedia.getType() == CounsellingMedia.VIDEO_MEDIA) {
            if (videoCurrentPos > 0) {
                vb.videoView.seekTo(videoCurrentPos);
            }
        }
    }

    protected void showAlertDialog(String message, Runnable runnable) {//todo add basefragment impl
        new AlertDialog.Builder(requireContext()).setTitle("Alert").setMessage(message).setCancelable(false).setPositiveButton("OK", (dialog, which) -> {
            if (runnable != null)
                runnable.run();
        }).show();
    }

}

