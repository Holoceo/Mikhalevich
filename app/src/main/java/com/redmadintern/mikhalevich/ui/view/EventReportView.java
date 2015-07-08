package com.redmadintern.mikhalevich.ui.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.TextView;

import com.redmadintern.mikhalevich.R;
import com.redmadintern.mikhalevich.model.local.EventReport;
import com.redmadintern.mikhalevich.model.local.EventStatus;
import com.redmadintern.mikhalevich.model.local.Stoa;
import com.redmadintern.mikhalevich.utils.DateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alexander on 04.07.2015.
 */
public class EventReportView extends LinearLayout implements View.OnClickListener{
    private static final int ANIM_DURATION = 500;
    private static final int DIVIDER_HEIGHT = 1;
    private static final int TYPE_STATUS = 0;
    private static final int TYPE_PHONE = 1;
    private static final int TYPE_MAP = 2;

    private List<EventStatus> statuses;

    private OnStatusClickListener statusClickListener;
    private LayoutInflater layoutInflater;

    private List<RelatedPoint> circles = new ArrayList<>();
    private List<Line> lines = new ArrayList<>();
    private List<Integer> itemsWithPhone = new ArrayList<>();//positions of items with phone
    private List<Integer> phoneIconsTop = new ArrayList<>();
    private List<Integer> itemsWithMap = new ArrayList<>();//positions of items with map
    private List<Integer> mapIconsTop = new ArrayList<>();

    private int iconSectionWidth;
    private int iconSectionPadding;
    private int circleRadius;
    private int circleRadiusOuter;
    private int lineMargin;
    private int lineLeft;
    private int phoneIconPadding;
    private int mapIconPadding;
    private Paint paintDivider;
    private Paint paintVerticalLine;
    private Paint paintCircle;
    private Paint paintCircleEnabled;
    private Bitmap bmpMap;
    private Bitmap bmpCall;
    private float titleTextSize;
    private boolean animate = true;
    private int animatedRadius = 0;

    private boolean isNewCircleAvaliable = false;
    private int newCircleRadius = 0;

    private ScrollView scrollView;

    public EventReportView(Context context) {
        super(context);TextView textView;
        init(context, null);
    }

    public EventReportView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EventReportView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        setWillNotDraw(false);
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        iconSectionWidth = getResources().getDimensionPixelSize(R.dimen.icon_section_width);
        iconSectionPadding = getResources().getDimensionPixelSize(R.dimen.icon_section_padding);
        circleRadius = getResources().getDimensionPixelSize(R.dimen.circle_radius);
        lineMargin = getResources().getDimensionPixelSize(R.dimen.line_margin);
        phoneIconPadding = getResources().getDimensionPixelSize(R.dimen.phone_icon_padding);
        mapIconPadding = getResources().getDimensionPixelSize(R.dimen.map_icon_padding);
        circleRadiusOuter = circleRadius + lineMargin;
        lineLeft = iconSectionPadding + circleRadius;

        int defaultColor = getResources().getColor(R.color.GR);
        int circleEnabledColor = getResources().getColor(R.color.GRE);

        int circleColor = circleEnabledColor;
        int dividerColor = defaultColor;

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EventReportView);
            circleColor = array.getColor(R.styleable.EventReportView_circleColor, circleEnabledColor);
            dividerColor = array.getColor(R.styleable.EventReportView_dividerColor, defaultColor);
            titleTextSize = array.getFloat(R.styleable.EventReportView_titleTextSize, 19.0f);
        }


        paintDivider = new Paint();
        paintDivider.setColor(dividerColor);

        paintVerticalLine = new Paint();
        paintVerticalLine.setColor(dividerColor);

        paintCircle = new Paint();
        paintCircle.setColor(dividerColor);
        paintCircle.setStyle(Paint.Style.FILL);
        paintCircle.setAntiAlias(true);

        paintCircleEnabled = new Paint();
        paintCircleEnabled.setColor(circleColor);
        paintCircleEnabled.setStyle(Paint.Style.FILL);
        paintCircleEnabled.setAntiAlias(true);

        bmpMap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_map);
        bmpCall = BitmapFactory.decodeResource(getResources(), R.drawable.ic_call);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawHeaderDividers(canvas);
        drawStatusDividers(canvas);
        drawVerticalLines(canvas);
        drawCircles(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawPhoneIcons(canvas);
        drawMapIcons(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        computeCircleCoords();
        computeLineCoords();
        computePhoneIconsTop();
        computeMapIconsTop();

        startAnimation();
    }

    //---------------------Compute values-----------------------------------------------------------

    private void computeCircleCoords() {
        int childCount = getChildCount();
        if (circles.size() == statuses.size() || childCount == 1)
            return;

        circles.clear();
        int cx = iconSectionPadding + circleRadius;
        int deltaY = iconSectionPadding + circleRadius;
        for (int pos = 1; pos < childCount; pos++) {
            View statusView = getChildAt(pos);
            Meta meta = (Meta)statusView.getTag();
            if (meta != null && meta.type == TYPE_STATUS) {
                int cy = statusView.getTop() + deltaY;
                RelatedPoint relatedPoint = new RelatedPoint(cx, cy, meta.position);
                circles.add(relatedPoint);
            }
        }
    }

    private void computeLineCoords() {
        int circlesCount = circles.size();
        if (circlesCount < 2 || lines.size() == circlesCount - 1)
            return;

        lines.clear();
        int top = circles.get(0).y + circleRadiusOuter;
        for (int pos = 1; pos < circlesCount; pos++) {
            int y = circles.get(pos).y;
            int bottom = y - circleRadiusOuter;
            Line line = new Line(top, bottom);
            lines.add(line);
            top = y + circleRadiusOuter;
        }
    }

    private void computePhoneIconsTop() {
        for (int pos : itemsWithPhone) {
            View vItem = getChildAt(pos);
            int top = vItem.getTop() + phoneIconPadding;
            phoneIconsTop.add(top);
        }
    }

    private void computeMapIconsTop() {
        for (int pos : itemsWithMap) {
            View vItem = getChildAt(pos);
            int top = vItem.getTop() + mapIconPadding;
            mapIconsTop.add(top);
        }
    }

    //--------------------------Draw items----------------------------------------------------------

    private void drawHeaderDividers(Canvas canvas) {
        canvas.drawLine(0, 0, getWidth(), 0, paintDivider);
        int bottom = getChildAt(0).getBottom();
        canvas.drawLine(0, bottom, getWidth(), bottom, paintDivider);
    }

    private void drawStatusDividers(Canvas canvas) {
        int childCount = getChildCount();
        if (childCount > 2) {
            for (int pos = 2; pos < childCount; pos++) {
                int top = getChildAt(pos).getTop()-1;//because of item has margin top
                canvas.drawLine(iconSectionWidth, top, getWidth(), top, paintVerticalLine);
            }
        }
    }

    private void drawVerticalLines(Canvas canvas) {
        for (Line line : lines) {
            canvas.drawLine(lineLeft, line.top, lineLeft, line.bottom, paintDivider);
        }
    }

    private void drawCircles(Canvas canvas) {
        int maxIndex = circles.size() - 1;

        for (int i = 0; i < maxIndex; i++) {
            drawCircle(canvas, i, animatedRadius);
        }

        if (isNewCircleAvaliable)
            drawCircle(canvas, maxIndex, newCircleRadius);
        else
            drawCircle(canvas, maxIndex, animatedRadius);
    }

    private void drawCircle(Canvas canvas,int position, int radius) {
        RelatedPoint point = circles.get(position);
        EventStatus eventStatus = statuses.get(point.statusPosition);
        Paint paint = eventStatus.isPassed() ? paintCircleEnabled : paintCircle;
        canvas.drawCircle(point.x, point.y, radius, paint);
    }

    private void drawPhoneIcons(Canvas canvas) {
        int icWidth = bmpCall.getWidth();
        int left = getWidth() - icWidth - iconSectionPadding;
        for (int top : phoneIconsTop) {
            canvas.drawBitmap(bmpCall, left, top, null);
        }
    }

    private void drawMapIcons(Canvas canvas) {
        int icWidth = bmpMap.getWidth();
        int left = getWidth() - icWidth - iconSectionPadding;
        for (int top : mapIconsTop) {
            canvas.drawBitmap(bmpMap, left, top, null);
        }
    }

    private void startAnimation() {
        if (!animate)
            return;

        animate = false;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, circleRadius);
        valueAnimator.setDuration(ANIM_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatedRadius = (Integer) animation.getAnimatedValue();
                invalidate();
            }

        });
        valueAnimator.start();
    }

    //-------------------------------Data-----------------------------------------------------------

    public void bindScrollView(ScrollView scrollView) {
        this.scrollView = scrollView;
    }

    public void addEventStatus(EventStatus status) {
        statuses.add(status);
        addEventStatus(statuses.size() - 1);

        final View recent = getChildAt(getChildCount()-1);
        ViewTreeObserver viewTreeObserver = recent.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ObjectAnimator yTranslate = ObjectAnimator.ofInt(scrollView, "scrollY", getBottom());
                yTranslate.setDuration(ANIM_DURATION);

                isNewCircleAvaliable = true;
                ValueAnimator valueAnimator = ValueAnimator.ofInt(0, circleRadius);
                valueAnimator.setDuration(ANIM_DURATION);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        newCircleRadius = (Integer) animation.getAnimatedValue();
                        invalidate();
                    }
                });

                yTranslate.start();
                valueAnimator.start();
            }
        });
        /*viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                recent.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });*/
    }

    private void addEventStatus(int pos) {
        EventStatus status = statuses.get(pos);

        Stoa stoa = status.getStoa();
        ViewGroup vItem = (ViewGroup)createStatusView(status, pos);
        vItem.setOnClickListener(this);
        Meta meta = new Meta(pos, TYPE_STATUS);
        vItem.setTag(meta);
        addView(vItem);
        if (!stoa.isEmpty())
            itemsWithMap.add(currentItemPos);
        currentItemPos++;

        //Add phone from STOA if exists
        String phone = stoa.getPhone();
        if (!phone.isEmpty()) {
            View phoneView = createPhoneView(phone, status.isPassed());
            meta = new Meta(pos, TYPE_PHONE);
            phoneView.setTag(meta);
            phoneView.setOnClickListener(this);
            addView(phoneView);
            itemsWithPhone.add(currentItemPos);
            currentItemPos++;
        }
    }

    //ReportView child position
    int currentItemPos;

    public void setEventReport(EventReport eventReport) {
        statuses = eventReport.getStatuses();
        currentItemPos = 0;
        if (getChildCount() > 0)
            removeAllViews();

        //Add header
        View header = layoutInflater.inflate(R.layout.event_status_header, this, false);
        //Add space for divider
        ((LayoutParams)header.getLayoutParams()).topMargin = DIVIDER_HEIGHT;
        addView(header);
        currentItemPos++;

        //Add items
        for (int pos = 0; pos < statuses.size(); pos++) {
            addEventStatus(pos);
        }
    }

    private View createStatusView(EventStatus status, int pos) {
        //Inflate
        ViewGroup vItem = (ViewGroup)layoutInflater.inflate(R.layout.event_status_item, this, false);
        TextView tvTitle = (TextView)vItem.findViewById(R.id.tv_title);
        TextView tvDescription = (TextView)vItem.findViewById(R.id.tv_description);
        TextView tvDate = (TextView)vItem.findViewById(R.id.tv_date);

        //Add space for divider
        ((LayoutParams)vItem.getLayoutParams()).topMargin = DIVIDER_HEIGHT;

        //Populate
        tvTitle.setTextSize(titleTextSize);
        tvTitle.setText(status.getTitle());
        tvTitle.setEnabled(status.isPassed());

        String description = status.getShortDescription();
        if (description.isEmpty()) {
            tvDescription.setVisibility(GONE);
        } else {
            tvDescription.setText(status.getShortDescription());
        }

        if (status.getDate() > 0) {
            String dateString = DateUtil.formatDate(status.getDate());
            tvDate.setText(dateString);
        } else {
            tvDate.setVisibility(GONE);
        }

        //Add STOA info if exists
        Stoa stoa = status.getStoa();
        if (!stoa.isEmpty()) {
            inflateStoaView(stoa, status.isPassed(), vItem, pos);
        }

        return vItem;
    }

    private void inflateStoaView(Stoa stoa, boolean enabled, ViewGroup root, int pos) {
        //Inflate
        View vStoa = layoutInflater.inflate(R.layout.stoa_detail, root, true);
        TextView tvAdress = (TextView)vStoa.findViewById(R.id.tv_adress);
        TextView tvServHours = (TextView)vStoa.findViewById(R.id.tv_service_hours);
        View vMap = vStoa.findViewById(R.id.map);

        Meta meta = new Meta(pos, TYPE_MAP);
        vMap.setTag(meta);

        //Populate
        tvAdress.setText(stoa.getAdress());
        tvAdress.setEnabled(enabled);
        tvServHours.setText(stoa.getServiceHours());
        tvServHours.setEnabled(enabled);
        vMap.setOnClickListener(this);
    }

    private View createPhoneView(String phone, boolean enabled) {
        //Inflate
        View vPhone = layoutInflater.inflate(R.layout.stoa_phone, this, false);
        TextView tvPhone = (TextView)vPhone.findViewById(R.id.tv_phone);

        //Add space for divider
        ((LayoutParams)vPhone.getLayoutParams()).topMargin = DIVIDER_HEIGHT;

        //Populate
        tvPhone.setText(phone);
        tvPhone.setEnabled(enabled);

        return vPhone;
    }

    @Override
    public void onClick(View view) {
        if (statusClickListener != null) {
            Meta meta = (Meta)view.getTag();
            EventStatus status = statuses.get(meta.position);
            switch (meta.type) {
                case TYPE_PHONE:
                    String phone = status.getStoa().getPhone();
                    statusClickListener.onPhoneClick(phone);
                    break;

                case TYPE_MAP:
                    String adress = status.getStoa().getAdress();
                    statusClickListener.onMapClick(adress);
                    break;

                case TYPE_STATUS:
                    statusClickListener.onStatusClick(status);
                    break;
            }

        }
    }

    public void setStatusClickListener(OnStatusClickListener statusClickListener) {
        this.statusClickListener = statusClickListener;
    }

    public interface OnStatusClickListener {
        void onStatusClick(EventStatus eventStatus);
        void onPhoneClick(String phone);
        void onMapClick(String adress);
    }

    class Meta {
        int position;
        int type;

        public Meta(int position, int type) {
            this.position = position;
            this.type = type;
        }
    }

    class RelatedPoint {
        int x;
        int y;
        int statusPosition;

        public RelatedPoint(int x, int y, int statusPosition) {
            this.x = x;
            this.y = y;
            this.statusPosition = statusPosition;
        }
    }

    class Line {
        int top;
        int bottom;

        public Line(int top, int bottom) {
            this.top = top;
            this.bottom = bottom;
        }
    }

}
