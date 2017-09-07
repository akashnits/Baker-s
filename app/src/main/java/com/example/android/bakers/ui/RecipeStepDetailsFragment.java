package com.example.android.bakers.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakers.R;
import com.example.android.bakers.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.R.attr.orientation;


public class RecipeStepDetailsFragment extends Fragment {
    @Nullable
    @BindView(R.id.mediaPlayerView)
    SimpleExoPlayerView mediaPlayerView;
    @Nullable
    @BindView(R.id.tvRecipeStepDescription)
    TextView tvRecipeStepDescription;
    @Nullable
    @BindView(R.id.btPrevious)
    Button btPrevious;
    @Nullable
    @BindView(R.id.btNext)
    Button btNext;
    Unbinder unbinder;

    private SimpleExoPlayer mExoPlayer;
    private int mStepId;
    private ArrayList<Step> mStepArrayList;
    private Step mStep;
    private boolean isVisible= false;
    private boolean mTwoPane;




   public static RecipeStepDetailsFragment newInstance(int position, ArrayList<Step> stepArrayList, String recipeName, Context context) {

        Bundle args = new Bundle();
        args.putInt(context.getString(R.string.stepPosition), position);
        args.putParcelableArrayList(context.getString(R.string.stepArrayList), stepArrayList);
        args.putString(context.getString(R.string.recipeName), recipeName);

        RecipeStepDetailsFragment fragment = new RecipeStepDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mTwoPane= getResources().getBoolean(R.bool.twoPaneMode);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_step_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActionBar actionBar= ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && !mTwoPane) {
            actionBar.setTitle(getArguments().getString(getString(R.string.recipeName)));
        }

        if(!mTwoPane) {
            if (mExoPlayer == null) {
                initializeVisibleFragment(mStep);
            }
        }else {
            Bundle args = getArguments();
            mStepId = args.getInt(getString(R.string.stepPosition));
            try {
                mStepArrayList = args.getParcelableArrayList(getString(R.string.stepArrayList));
                if (mStepArrayList != null)
                    mStep = mStepArrayList.get(mStepId);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
            if(mStep != null) {
                initializeVisibleFragment(mStep);
            }
            if(mExoPlayer == null){
                Snackbar.make(view, getString(R.string.noVideoAvailable),Snackbar.LENGTH_SHORT ).show();
            }
        }
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            isVisible= true;
            if(mStep != null){
                initializeVisibleFragment(mStep);
            }
            else {
                Bundle args = getArguments();
                mStepId = args.getInt("stepPosition");
                try {
                    mStepArrayList = args.getParcelableArrayList("stepArrayList");
                    if (mStepArrayList != null)
                        mStep = mStepArrayList.get(mStepId);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
                if(mStep != null) {
                    initializeVisibleFragment(mStep);
                }
            }
        }else if(mExoPlayer != null){
            mExoPlayer.setPlayWhenReady(false);
        }

    }


    private void initializeVisibleFragment(Step step) {

        if (step != null) {
            if (step.getVideoURL().length() > 0) {
                initializePlayer(Uri.parse(step.getVideoURL()));
            } else if (!TextUtils.isEmpty(step.getThumbnailURL()) && isVideoFile(step.getThumbnailURL())) {
                    initializePlayer(Uri.parse(step.getThumbnailURL()));
            } else {
                initializePlayer(null);
            }

            String description = step.getDescription();
            if (description.length() > 0) {
                if(tvRecipeStepDescription != null){
                    tvRecipeStepDescription.setText(description);
                }
            }
        }
    }

    private void initializePlayer(Uri mediaUri){
        if(mediaUri != null){
            if(mediaPlayerView != null){
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
                mediaPlayerView.setPlayer(mExoPlayer);

                String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
                MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                                            getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
                LoopingMediaSource loopingSource = new LoopingMediaSource(mediaSource, 2);
                mExoPlayer.prepare(loopingSource);
                mExoPlayer.setPlayWhenReady(false);
            }
        }
    }


    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }

    public void releasePlayer() {
        if(mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
            }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mExoPlayer != null){
            mExoPlayer.setPlayWhenReady(false);
        }
    }


}
