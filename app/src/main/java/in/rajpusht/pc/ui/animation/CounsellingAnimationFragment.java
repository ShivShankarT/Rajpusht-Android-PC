package in.rajpusht.pc.ui.animation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.MediaController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.transition.TransitionManager;
import android.util.Log;

import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import in.rajpusht.pc.R;
import in.rajpusht.pc.custom.utils.HUtil;
import in.rajpusht.pc.databinding.FragmentCounsellingAnimationBinding;
import in.rajpusht.pc.model.CounsellingMedia;
import in.rajpusht.pc.ui.benef_list.BeneficiaryFragment;
import in.rajpusht.pc.ui.pregnancy_graph.PregnancyGraphFragment;
import in.rajpusht.pc.utils.FragmentUtils;
import in.rajpusht.pc.utils.ui.CustomVideoView;


public class CounsellingAnimationFragment extends Fragment {


    private static final int delayMillis = 500;
    private static int finalHeight = Target.SIZE_ORIGINAL;
    private static int finalWidth = Target.SIZE_ORIGINAL;
    private Handler handler = new Handler();
    private CounsellingMedia counsellingMedia;
    private int mediaPos = 0;
    private int pos = 0;
    private FragmentCounsellingAnimationBinding vb;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPos < counsellingMedia.getMediaImage().size()) {
                 Picasso.get().load(counsellingMedia.getMediaImage().get(mediaPos)).noPlaceholder().into(vb.imageview);
                /*Glide.with(requireContext()).asBitmap()
                        .load(counsellingMedia.getMediaImage().get(mediaPos))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .dontAnimate()
                        .into(new SimpleTarget<Bitmap>(finalWidth, finalHeight) {

                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                vb.imageview.setImageBitmap(resource);
                            }
                        });*/
                mediaPos++;

                handler.postDelayed(runnable, delayMillis);
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
            Log.i("imagedd",""+counsellingMedia.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vb = FragmentCounsellingAnimationBinding.inflate(inflater, container, false);
        return vb.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mediaPos = 0;

        vb.toolbarLy.toolbar.setTitle(R.string.Women_Counselling);
       /* if (counsellingMedia.getType() == CounsellingMedia.IMAGE_MEDIA)
            vb.toolbarLy.toolbar.setTitle(counsellingMedia.getId() + "-" + counsellingMedia.getMediaImage().get(0).substring("file:///android_asset/counseling".length()));
*/
        vb.toolbarLy.toolbar.getMenu().add(1, 1, 1, "Exit").setIcon(R.drawable.ic_exit).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        vb.toolbarLy.toolbar.setNavigationOnClickListener((v) -> {
            requireActivity().onBackPressed();
        });

        vb.toolbarLy.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 1) {
                    requireActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    FragmentUtils.replaceFragment(requireActivity(), new BeneficiaryFragment(), R.id.fragment_container, false, true, FragmentUtils.TRANSITION_FADE_IN_OUT);

                }
                return true;
            }
        });

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
            CustomVideoView videoView = vb.videoView;
            videoView.setVisibility(View.GONE);
            vb.imageview.setVisibility(View.VISIBLE);
            Picasso.get().load("file:///android_asset/counseling/video_thumb.webp").noPlaceholder().into(vb.imageview);
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
                    vb.playBtn.setVisibility(View.VISIBLE);
                }
            });
            vb.playBtn.setVisibility(View.INVISIBLE);
            videoView.setPlayPauseListener(new CustomVideoView.PlayPauseListener() {
                @Override
                public void onPlay() {
                    if (vb.playBtn.getVisibility() != View.INVISIBLE)
                        vb.playBtn.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onPause() {
                    if (vb.playBtn.getVisibility() != View.VISIBLE)
                        vb.playBtn.setVisibility(View.VISIBLE);
                }
            });
            vb.playBtn.setVisibility(View.VISIBLE);
            View.OnClickListener onClickListener = v -> {
                videoView.start();
                startActivity(new Intent(requireContext(), ActivityVideoPlay.class));
                TransitionManager.beginDelayedTransition(vb.ll);
                vb.nxtBtn.setVisibility(View.VISIBLE);

            };
            vb.playBtn.setOnClickListener(onClickListener);
            vb.imageview.setOnClickListener(onClickListener);

        } else {
            vb.playBtn.setVisibility(View.GONE);
            handler.postDelayed(runnable, 10);
        }

        int nextCounPos = pos + 1;
        if (!(nextCounPos < CounsellingMedia.counsellingMediaData().size())) {
            vb.nxtBtn.setText(R.string.finish);
        }

        vb.nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int nextCounPos = pos + 1;
                if (nextCounPos < CounsellingMedia.counsellingMediaData().size()) {
                    CounsellingMedia nextCounsellingMedia = CounsellingMedia.counsellingMediaData().get(nextCounPos);

                    if (nextCounsellingMedia.getType() == CounsellingMedia.IMAGE_MEDIA || nextCounsellingMedia.getType() == CounsellingMedia.VIDEO_MEDIA) {
                        FragmentUtils.replaceFragment(requireActivity(), CounsellingAnimationFragment.newInstance(nextCounPos), R.id.fragment_container, true, true, FragmentUtils.TRANSITION_POP);
                    } else {
                        FragmentUtils.replaceFragment(requireActivity(), PregnancyGraphFragment.newInstance(), R.id.fragment_container, true, true, FragmentUtils.TRANSITION_POP);
                    }


                } else {

                    showAlertDialog(getString(R.string.counselling_completed), new Runnable() {
                        @Override
                        public void run() {
                            requireActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            FragmentUtils.replaceFragment(requireActivity(), new BeneficiaryFragment(), R.id.fragment_container, false, true, FragmentUtils.TRANSITION_FADE_IN_OUT);
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
            vb.playBtn.setVisibility(View.VISIBLE);
        }
    }

    protected void showAlertDialog(String message, Runnable runnable) {//todo add basefragment impl
        new AlertDialog.Builder(requireContext()).setTitle(R.string.alert).setMessage(message).setCancelable(false).setPositiveButton(R.string.ok, (dialog, which) -> {
            if (runnable != null)
                runnable.run();
        }).show();
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}

