package com.wxgh.livehappy.le_play.skin.base;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.wxgh.livehappy.le_play.skin.BaseView;
import com.wxgh.livehappy.le_play.skin.popupwindow.BackLivePopWindow;
import com.wxgh.livehappy.le_play.skin.popupwindow.BackLivePopWindow.OnBackToLiveListener;
import com.lecloud.leutils.ReUtils;
import com.lecloud.leutils.TimerUtils;
import com.letv.controller.interfacev1.ISplayerController;
import com.letv.universal.iplay.EventPlayProxy;
import com.letv.universal.iplay.ISplayer;
import com.letv.universal.notice.UIObserver;

import java.util.Date;
import java.util.Observable;

/**
 * 时移seekbar
 * 
 * 
 */
public abstract class BaseLiveSeekBar extends BaseView implements UIObserver {

    private static final int MIN_SEEKTIME_BUFFER = -5;
    private static final int MAX = 3600 * 2;
    private static final int MAX_2 = 3600;

    private static final String TAG = "BaseLiveSeekBar";

    private static final int HOURS_2_SECOND = 2 * 60 * 60 * 1000;// 2小时，单位毫秒

    protected SeekBar seekBar;
    /**
     * 判断是否拖动状态
     */
    protected boolean isTrackingTouch = false;
    private Date positionTime;
    private Date beginTime;
    private Date serverTime;
    private long currentSeekTime = 0;
    private long betweenTime;
    private BackLivePopWindow mBackLivePopWindow;
    private int mprogress = 0;
    private OnSeekChangeListener mOnSeekChangeListener;

    public BaseLiveSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseLiveSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseLiveSeekBar(Context context) {
        super(context);
    }

    @Override
    protected void initPlayer() {
        player.attachObserver(this);
        if (uiPlayContext != null && uiPlayContext.getCurrentTimeShirtProgress() > 0) {
            setLiveSeekBarProgress(seekBar, uiPlayContext.getCurrentTimeShirtProgress());
        }
    }

    public abstract String getLayout();

    @Override
    protected void initView(Context context) {
        seekBar = (SeekBar) LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, getLayout()), null);
        // seekBar.setp
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.addView(seekBar, params);
        mBackLivePopWindow = BackLivePopWindow.getInstance(context);
        mBackLivePopWindow.setOnBackToLiveListener(new OnBackToLiveListener() {
            @Override
            public void onBackToLive() {
                // TODO 返回直播
                if (player != null) {
                    player.resetPlay();
                }
            }
        });

        initSeekbar();
        reset();
    }

    private void initSeekbar() {
        if (seekBar != null) {
            seekBar.setMax(MAX);
            seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    stopTrackingTouch();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    startTrackingTouch();
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressChanged(progress);
                    if (mOnSeekChangeListener != null) {
                        mOnSeekChangeListener.onProgressChanged(seekBar, progress, fromUser || isTrackingTouch);
                    }
                }

            });
        }
    }

    public void startTrackingTouch() {
        if (isTrackingTouch) {
            return;
        }
        isTrackingTouch = true;
        mprogress = seekBar.getProgress();
    }

    public void stopTrackingTouch() {
        isTrackingTouch = false;

        if (player != null) {// 只有在播放的时候才允许seek
            int progress = seekBar.getProgress();
            long seekTime = 0;
            if (positionTime == null || serverTime == null) {
                // TODO
                return;
            }
            long gapTime = (long) ((positionTime.getTime() - serverTime.getTime()) * 0.001);

            if (betweenTime > HOURS_2_SECOND) {
                // TODO 说明当前当前播放时间是最新的
                if (gapTime == 0) {
                    seekTime = progress - seekBar.getMax();
                } else {
                    seekTime = (long) (progress - seekBar.getMax() * 0.5 + gapTime);
                }
            } else {
                Log.d(TAG, "[seekTime] progress2 :" + progress);
                seekTime = progress - seekBar.getMax();
            }

            if (seekTime > 0) {
                progress = (int) (progress - seekTime);
                seekTime = MIN_SEEKTIME_BUFFER;
                Toast.makeText(getContext(), "已经回到最新播放时间啦", Toast.LENGTH_SHORT).show();
            }
            setLiveSeekBarProgress(seekBar, progress);

            Log.d(TAG, "[seekTime] seekTime:" + seekTime + ",gapTime:" + gapTime);
            player.seekTo(seekTime);
            if (!player.isPlaying()) {
            	player.start();
			}
            uiPlayContext.setCurrentTimeShirtProgress(seekBar.getProgress());// 进入时移模式

        } else {
            seekBar.setProgress(mprogress);
        }

        if (seekBar.getProgress() < seekBar.getMax() - 10) {
            showBackToLive();
        } else {
            hideBackToLive();
        }
    }

    private void progressChanged(int progress) {
        int i = seekBar.getWidth() * progress / seekBar.getMax();

        // seekBar.gett
    }

    public void setProgress(int progress) {
        if (seekBar != null) {
            seekBar.setProgress(this.mprogress + progress * seekBar.getMax() / 1000);
        }
    }

    public String getCurrentTime() {
        if (serverTime == null || seekBar == null) {
            return "";
        }
        // long currentPosition = serverTime.getTime() + (seekBar.getProgress()
        // - seekBar.getMax())*1000;
        long currentPosition = positionTime.getTime();
        String time = TimerUtils.timeToDate(currentPosition) + "";
        return time;
    }

    public String getSeekToTime() {
        if (serverTime == null || seekBar == null) {
            return "";
        }
        long currentPosition = serverTime.getTime() + (seekBar.getProgress() - seekBar.getMax()) * 1000;
        String time = TimerUtils.timeToDate(currentPosition) + "";
        return time;
    }

    private void setLiveSeekBarProgress(SeekBar seekBar, int progress) {
        if (progress > seekBar.getMax() * 0.2 && progress < seekBar.getMax() * 0.8) {
            seekBar.setProgress(progress);
        } else {
            seekBar.setProgress((int) (seekBar.getMax() * 0.5));
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        Bundle bundle = (Bundle) data;
        int state = bundle.getInt("state");
        switch (state) {
        case EventPlayProxy.PLAYER_TIME_SHIRT:
            if (!isShown()) {
                setVisibility(View.VISIBLE);
            }
            positionTime = (Date) bundle.getSerializable("currentTimeShirt");
            serverTime = (Date) bundle.getSerializable("serverTime");

            if (beginTime == null && positionTime != null) {
                beginTime = (Date) bundle.getSerializable("beginTime");
                if (beginTime != null) {
                    betweenTime = positionTime.getTime() - beginTime.getTime();
                    Log.d(TAG, "[seekbar] betweenTime:" + betweenTime + ",HOURS_2_SECOND:" + HOURS_2_SECOND + ",--:" + (betweenTime - HOURS_2_SECOND));

                    if (betweenTime > HOURS_2_SECOND) {
                        seekBar.setMax(MAX_2);
                        if (uiPlayContext.getCurrentTimeShirtProgress() > 0) {
                            setLiveSeekBarProgress(seekBar, uiPlayContext.getCurrentTimeShirtProgress());
                        } else {
                            seekBar.setProgress(MAX_2);
                        }

                    } else {
                        seekBar.setMax(MAX);
                        if (uiPlayContext.getCurrentTimeShirtProgress() > 0) {
                            setLiveSeekBarProgress(seekBar, uiPlayContext.getCurrentTimeShirtProgress());
                        } else {
                            seekBar.setProgress(MAX);
                        }
                    }
                }
            }
            if (mOnSeekChangeListener != null && !isTrackingTouch && player.isPlaying()) {
                mOnSeekChangeListener.onProgressChanged(seekBar, seekBar.getProgress(), false);
            }
            break;
        case EventPlayProxy.PLAYER_TIME_SHIRT_SEEK_ERROR:
            uiPlayContext.setCurrentTimeShirtProgress(0);
            break;
        case ISplayer.PLAYER_EVENT_RATE_TYPE_CHANGE:
            break;
        case ISplayer.PLAYER_EVENT_RESET_PLAY:
            uiPlayContext.setCurrentTimeShirtProgress(0);
            seekBar.setProgress(seekBar.getMax());
            if (mBackLivePopWindow != null && mBackLivePopWindow.isShowing()) {
                mBackLivePopWindow.dismiss();
            }
            break;
            case ISplayerController.SHOW_FLOATING_VIEW:
            showBackToLive();
            break;
            case ISplayerController.HIDE_FLOATING_VIEW:
            hideBackToLive();
            break;
        default:
            break;
        }
    }

    private void hideBackToLive() {
        mBackLivePopWindow.dismiss();
    }

    private void showBackToLive() {
        if (seekBar.getProgress() > seekBar.getMax() - 10) {
            mBackLivePopWindow.dismiss();
            return;
        }
        if (mBackLivePopWindow.isShowing()) {
            return;
        }
        if (uiPlayContext.getScreenResolution(context) == ISplayerController.SCREEN_ORIENTATION_LANDSCAPE) {
            mBackLivePopWindow.dismiss();
            mBackLivePopWindow.showPop(seekBar);
        }
    }

    public void setOnSeekChangeListener(OnSeekChangeListener l) {
        mOnSeekChangeListener = l;
    }

    public interface OnSeekChangeListener {
        void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);
    }

}
