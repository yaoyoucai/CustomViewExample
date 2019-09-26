package com.yh.customviewexample.view;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yh.customviewexample.R;
import com.yh.customviewexample.utils.DensityUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


/**
 * Created by bin
 * 简单弹幕控件
 */
public class DanMuLayout extends RelativeLayout implements LifecycleObserver{

    private final static String LIKE_STRING = " 为主播 点了个赞";
    private final static int DANMU_DEFAULT_TEXT_SIZE = 22;
    private final static int DANMU_DEFAULT_TEXT_COLOR = 0xffffffff;
    private final static int DANMU_DEFAULT_VALID_HEIGHT_SPACE = 120;
    private final static int DANMU_DEFAULT_LINES_COUNT = 3;

    /****************TIME start********************/
    private final static int DANMU_TIME_INTERVAL = 2000;//弹幕发射时间间隔
    private final static int DANMU_REMOVE_TIME = 5000;//移除占用时间间隔, 防止重叠
    private final static int DURING_TIME = 4000;//每条text弹幕的持续时间
    /****************TIME end********************/

    /****************handler.what start********************/
    private final static int ADD_DANMU = 1;//添加彈幕
    private final static int DANMU_REMOVE_OCCUPIED = 2;//移除彈幕佔用
    /****************handler.what end********************/

    int linesCount;//彈幕顯示行數
    int validHeightSpace;//用于弹幕显示的空间高度
    boolean danMuSwitch = true;//弹幕开关标志
    boolean recycle = false;//是否循环弹幕

    List<DanMuInfo> danMuInfoList = new ArrayList<>();//弹幕内容
    List<DanMuInfo> recycleDanMu = new ArrayList<>();//循环弹幕
    Set<Integer> existMarginValues = new HashSet<>();//记录当前仍在显示状态的弹幕的位置（避免重叠）
    List<DanMuAttributes> dMAList = new ArrayList<>();//text彈幕屬性
    List<View> viewList = new ArrayList<>();//view弹幕集合

    TextPaint txtPaint;
    ScheduledExecutorService executorService;//单线程池

    DanMuHandler handler;
    private static class DanMuHandler extends Handler {
        private WeakReference<DanMuLayout> ref;
        private DanMuLayout dml;

        DanMuHandler(DanMuLayout dml) {
            ref = new WeakReference<>(dml);
            this.dml = ref.get();
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_DANMU:
                    addDanMu();
                    break;
                case DANMU_REMOVE_OCCUPIED:
                    dml.existMarginValues.remove(msg.arg1);
                    break;
            }
        }

        private void addDanMu() {
            if (canShowDanMu()) {
                DanMuInfo danMuInfo = dml.danMuInfoList.get(0);
                if (danMuInfo.getType().equals("text")) {
                    dml.addDanMuText(danMuInfo.getText());
                } else if (danMuInfo.getType().equals("like")) {
                    dml.addDanMuLike(danMuInfo.getUserName());
                }else if(danMuInfo.getType().equals("event")){
                    dml.addDanMuEvent(danMuInfo.getUserName(),danMuInfo.getText());
                }
                dml.danMuInfoList.remove(danMuInfo);
            } else if (dml.danMuInfoList != null && dml.danMuInfoList.size() == 0 && dml.recycle) {
                dml.danMuInfoList.addAll(dml.recycleDanMu);
            }
            sendEmptyMessageDelayed(ADD_DANMU, DANMU_TIME_INTERVAL);
        }

        private boolean canShowDanMu() {
            return dml != null && dml.danMuInfoList != null && dml.danMuInfoList.size() > 0;
        }

    }

    public DanMuLayout(Context context) {
        this(context, null);
    }

    public DanMuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DanMuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        handler = new DanMuHandler(this);
        executorService = Executors.newSingleThreadScheduledExecutor();

        txtPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        txtPaint.setTextSize(DANMU_DEFAULT_TEXT_SIZE);
        txtPaint.setColor(DANMU_DEFAULT_TEXT_COLOR);

        setWillNotDraw(false);
        validHeightSpace = DensityUtil.dip2px(getContext(),DANMU_DEFAULT_VALID_HEIGHT_SPACE);
        linesCount = DANMU_DEFAULT_LINES_COUNT;
        if(getContext() instanceof LifecycleOwner){
            ((LifecycleOwner)getContext()).getLifecycle().addObserver(this);
            Log.i("danmu","danmu bind lifecycle");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
        moveX();
        moveView();
        checkView();
        if (danMuSwitch) {
            updateDanMuView();
        }
    }

    private void updateDanMuView() {
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private void checkView() {
        for (int i = 0;i<viewList.size();i++){
            View view = viewList.get(i);
            int x = (int) view.getTag();
            if (x < -view.getWidth()) {
                viewList.remove(i);
                removeView(view);
            }
        }
    }

    private void moveView() {
        for (int i = 0;i<viewList.size();i++){
            View view = viewList.get(i);
            int x = (int) view.getTag()-(view.getWidth() + DensityUtil.getScreenWidth(getContext())) / (DURING_TIME / 17);
            view.setX(x);
            view.setTag(x);
        }
    }

    /**
     * 绘制text弹幕
     */
    private void drawText(Canvas canvas) {
        for (int i = 0; i < dMAList.size(); i++) {
            DanMuAttributes dMA = dMAList.get(i);
            canvas.drawText(dMA.getText(), dMA.getPoint().x, dMA.getPoint().y, dMA.getTextPaint());
        }
    }

    /**
     * 平移textDensityUtil
     */
    private void moveX() {
        for (int i = 0; i < dMAList.size(); i++) {
            DanMuAttributes dMA = dMAList.get(i);
            float measureText = dMA.getTextPaint().measureText(dMA.getText());
            /**
             * 按照安卓手机刷新频率大致在60帧的情况下计算;
             * 在该速度下动画的持续时间为12秒(产品需求);
             * 使用测试机 lex820 得出的结果;
             * 2016年11月21日15:21:20
             * create by bin
             */
            dMA.getPoint().x -= (measureText + DensityUtil.getScreenWidth(getContext())) / (DURING_TIME / 17);//在该算法下 弹幕越长 速度越快
            if (dMA.getPoint().x < -measureText) {
                dMAList.remove(i);
            }
        }
    }

    /**
     * 添加一条text弹幕(新)
     * 2016年11月21日18:09:23
     * by bin
     */
    private void addDanMuText(final String txt) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final int verticalMargin = getRandomTopMargin();
                dMAList.add(new DanMuAttributes(new TextPaint(txtPaint),
                        new Point(DensityUtil.getScreenWidth(getContext()), verticalMargin + DANMU_DEFAULT_TEXT_SIZE),
                        txt));
                removeOccupied(verticalMargin);
            }
        });
    }

    /**
     * 移除占用
     */
    private void removeOccupied(final int verticalMargin) {
        handler.sendMessageDelayed(handler.obtainMessage(2,verticalMargin,0), DANMU_REMOVE_TIME);
    }

    public void addDanMuLike(final String name) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final int verticalMargin = getRandomTopMargin();
                post(new Runnable() {
                    @Override
                    public void run() {
                        View view = View.inflate(getContext(), R.layout.toast_live_like, null);
                        TextView textView =  view.findViewById(R.id.tv_text);
                        textView.setText(name + LIKE_STRING + "");
                        addDanMuView(view, verticalMargin);
                    }
                });
            }
        });
    }

    public void addDanMuEvent(final String name, final String text) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final int verticalMargin = getRandomTopMargin();
                post(new Runnable() {
                    @Override
                    public void run() {
                        View view = View.inflate(getContext(), R.layout.danmu_live_event, null);
                        TextView tvName = view.findViewById(R.id.tv_name);
                        TextView tvText = view.findViewById(R.id.tv_text);
                        tvName.setText(name);
                        tvText.setText(text);
                        addDanMuView(view, verticalMargin);
                    }
                });
            }
        });
    }

    private void addDanMuView(View view, int verticalMargin) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.topMargin = verticalMargin;
        view.setLayoutParams(params);

        view.setTag(getRight() - getLeft() - getPaddingLeft());
        removeOccupied(verticalMargin);
        addView(view);
        viewList.add(view);
    }

    /**
     * 获取一个空闲的弹幕占位
     */
    private int getRandomTopMargin() {
        while (true) {
            int randomIndex = (int) (Math.random() * linesCount);
            int marginValue = randomIndex * (validHeightSpace / linesCount);

            if (!existMarginValues.contains(marginValue)) {
                existMarginValues.add(marginValue);
                return marginValue;
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void startDanMu() {
        danMuSwitch = true;
        handler.removeMessages(ADD_DANMU);
        handler.sendEmptyMessageDelayed(ADD_DANMU, DANMU_TIME_INTERVAL);
        updateDanMuView();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void stopDanMu() {
        danMuSwitch = false;
        handler.removeMessages(ADD_DANMU);
    }

    public void addAllDanMu(List<? extends DanMuInfo> danMuInfoList) {
        this.danMuInfoList.addAll(danMuInfoList);
        this.recycleDanMu.addAll(danMuInfoList);
    }

    public void addDanMu(DanMuInfo info) {
        danMuInfoList.add(0, info);
        recycleDanMu.add(0, info);
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (VISIBLE == visibility) {
            startDanMu();
        } else {
            stopDanMu();
        }
    }

    public void setRecycle(boolean recycle) {
        this.recycle = recycle;
    }

    /**
     * 设置弹幕行数
     * 默认行数为 3
     */
    public void setDanMuLines(int linesCount) {
        this.linesCount = linesCount;
    }

    /**
     * 設置彈幕顯示的空間高度
     * 默認高度為 120dp
     */
    public void setValidHeightSpace(int validHeightSpace) {
        this.validHeightSpace = validHeightSpace;
    }
}
