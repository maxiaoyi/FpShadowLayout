package com.mxy.fpshadowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by mxy on 2019/4/19.
 */

public class FpShadowLayout extends FrameLayout {

    //阴影所在的边
    public static final int ALL = 0x1111;

    public static final int LEFT = 0x0001;

    public static final int TOP = 0x0010;

    public static final int RIGHT = 0x0100;

    public static final int BOTTOM = 0x1000;

    //圆角所在的边
    public static final int CORNER_ALL = 0x1111;

    public static final int CORNER_LEFT_TOP = 0x0001;

    public static final int CORNER_LEFT_BOTTOM = 0x0010;

    public static final int CORNER_RIGHT_TOP = 0x0100;

    public static final int CORNER_RIGHT_BOTTOM = 0x1000;

    //view的形状
    public static final int SHAPE_RECTANGLE = 0x0001;

    public static final int SHAPE_ROUND_RECTANGLE = 0x0100;

//    public static final int SHAPE_OVAL = 0x0010;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //阴影颜色
    private int mShadowColor = Color.TRANSPARENT;

    //阴影大小
    private float mShadowRadius = 0;

    //阴影所在的边
    private int mShadowSide = ALL;

    //圆角所在位置
    private int mCornerPosition = CORNER_ALL;

    //控件形状，圆角矩形 / 矩形
    private int mShadowShape = SHAPE_RECTANGLE;

    private float mRoundCornerRadius = 10;

    private int mViewWidth, mViewHeight;


    public FpShadowLayout(@NonNull Context context) {
        this(context, null);
    }

    public FpShadowLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FpShadowLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(attrs);
    }

    private void initViews(AttributeSet attrs) {

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);// 关闭硬件加速

        this.setWillNotDraw(false);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.Fp_ShadowLayout);

        if (typedArray != null) {

            mShadowColor = typedArray.getColor(R.styleable.Fp_ShadowLayout_fp_shadowColor,
                    getContext().getResources().getColor(android.R.color.black));

            mShadowRadius = typedArray.getDimension(R.styleable.Fp_ShadowLayout_fp_shadowRadius, dip2px(0));

            mRoundCornerRadius = typedArray.getDimension(R.styleable.Fp_ShadowLayout_fp_shadowRoundRadius, dip2px(0));

            mShadowSide = typedArray.getInt(R.styleable.Fp_ShadowLayout_fp_shadowSide, ALL);

            mShadowShape = typedArray.getInt(R.styleable.Fp_ShadowLayout_fp_shadowShape, SHAPE_RECTANGLE);

            mCornerPosition = typedArray.getInt(R.styleable.Fp_ShadowLayout_fp_round_corner, CORNER_ALL);

            typedArray.recycle();

        }

        mPaint.reset();

        mPaint.setAntiAlias(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        float effect = mShadowRadius;

        int paddingLeft = 0;

        int paddingTop = 0;

        int paddingRight = 0;

        int paddingBottom = 0;

        if ((mShadowSide & LEFT) == LEFT) {
            paddingLeft = (int) effect;
        }

        if ((mShadowSide & TOP) == TOP) {
            paddingTop = (int) effect;
        }

        if ((mShadowSide & RIGHT) == RIGHT) {
            paddingRight = (int) effect;
        }

        if ((mShadowSide & BOTTOM) == BOTTOM) {
            paddingBottom = (int) effect;
        }

        this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mViewHeight = h;

        mViewWidth = w;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.reset();

        mPaint.setAntiAlias(true);

        int[] colors = {mShadowColor, 0x00ffffff};

        if (mShadowShape == SHAPE_RECTANGLE) {

            float[] floats = new float[]{0f, 1.0f};

            mPaint.setStrokeWidth(mShadowRadius);

            if ((mShadowSide & LEFT) == LEFT && (mShadowSide & TOP) != TOP && (mShadowSide & RIGHT) != RIGHT && (mShadowSide & BOTTOM) != BOTTOM) {
                //画左边阴影
                drawRectLinearGradient(canvas, mShadowRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, mShadowRadius, mViewHeight));
            } else if ((mShadowSide & LEFT) != LEFT && (mShadowSide & TOP) == TOP && (mShadowSide & RIGHT) != RIGHT && (mShadowSide & BOTTOM) != BOTTOM) {
                //画上边阴影
                drawRectLinearGradient(canvas, 0, mShadowRadius, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, mViewWidth, mShadowRadius));
            } else if ((mShadowSide & LEFT) != LEFT && (mShadowSide & TOP) != TOP && (mShadowSide & RIGHT) == RIGHT && (mShadowSide & BOTTOM) != BOTTOM) {
                //画右边阴影
                drawRectLinearGradient(canvas, mViewWidth - mShadowRadius, 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - mShadowRadius, 0, mViewWidth, mViewHeight));
            } else if ((mShadowSide & LEFT) != LEFT && (mShadowSide & TOP) != TOP && (mShadowSide & RIGHT) != RIGHT && (mShadowSide & BOTTOM) == BOTTOM) {
                //画下边阴影
                drawRectLinearGradient(canvas, 0, mViewHeight - mShadowRadius, 0, mViewHeight, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mViewHeight - mShadowRadius, mViewWidth, mViewHeight));
            } else {

                if ((mShadowSide & LEFT) == LEFT && (mShadowSide & TOP) == TOP && (mShadowSide & RIGHT) != RIGHT && (mShadowSide & BOTTOM) != BOTTOM) {
                    //画左边阴影
                    drawRectLinearGradient(canvas, mShadowRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mShadowRadius, mShadowRadius, mViewHeight));

                    //画上边阴影
                    drawRectLinearGradient(canvas, 0, mShadowRadius, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mShadowRadius, 0, mViewWidth, mShadowRadius));

                    //画上边和左边角落的阴影
                    drawArcRadialGradient(canvas, mShadowRadius, mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * mShadowRadius, 2 * mShadowRadius), 180, 90);

                } else if ((mShadowSide & LEFT) == LEFT && (mShadowSide & RIGHT) == RIGHT && (mShadowSide & TOP) != TOP && (mShadowSide & BOTTOM) != BOTTOM) {//左边和右边

                    //画左边阴影
                    drawRectLinearGradient(canvas, mShadowRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, mShadowRadius, mViewHeight));

                    //画右边阴影
                    drawRectLinearGradient(canvas, mViewWidth - mShadowRadius, 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - mShadowRadius, 0, mViewWidth, mViewHeight));

                } else if ((mShadowSide & LEFT) == LEFT && (mShadowSide & RIGHT) != RIGHT && (mShadowSide & TOP) != TOP && (mShadowSide & BOTTOM) == BOTTOM) {//左边和下边

                    //画左边阴影
                    drawRectLinearGradient(canvas, mShadowRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, mShadowRadius, mViewHeight - mShadowRadius));

                    //画下边阴影
                    drawRectLinearGradient(canvas, 0, mViewHeight - mShadowRadius, 0, mViewHeight, colors, floats, Shader.TileMode.CLAMP, new RectF(mShadowRadius, mViewHeight - mShadowRadius, mViewWidth, mViewHeight));

                    //画左边和下边角落的阴影

                    drawArcRadialGradient(canvas, mShadowRadius, mViewHeight - mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mViewHeight - 2 * mShadowRadius, 2 * mShadowRadius, mViewHeight), 90, 90);
                } else if ((mShadowSide & LEFT) == LEFT && (mShadowSide & RIGHT) == RIGHT && (mShadowSide & TOP) == TOP && (mShadowSide & BOTTOM) != BOTTOM) {//左边上边和右边
                    //画左边阴影
                    drawRectLinearGradient(canvas, mShadowRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mShadowRadius, mShadowRadius, mViewHeight));

                    //画上边阴影
                    drawRectLinearGradient(canvas, 0, mShadowRadius, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mShadowRadius, 0, mViewWidth - mShadowRadius, mShadowRadius));

                    //画上边和左边角落的阴影

                    drawArcRadialGradient(canvas, mShadowRadius, mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * mShadowRadius, 2 * mShadowRadius), 180, 90);

                    //画右边阴影
                    drawRectLinearGradient(canvas, mViewWidth - mShadowRadius, 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - mShadowRadius, mShadowRadius, mViewWidth, mViewHeight));

                    //画上边和右边角落的阴影
                    drawArcRadialGradient(canvas, mViewWidth - mShadowRadius, mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - 2 * mShadowRadius, 0, mViewWidth, 2 * mShadowRadius), 270, 90);

                } else if ((mShadowSide & LEFT) == LEFT && (mShadowSide & RIGHT) != RIGHT && (mShadowSide & TOP) == TOP && (mShadowSide & BOTTOM) == BOTTOM) {//左边上边和下边
                    //画左边阴影
                    drawRectLinearGradient(canvas, mShadowRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mShadowRadius, mShadowRadius, mViewHeight - mShadowRadius));

                    //画上边阴影
                    drawRectLinearGradient(canvas, 0, mShadowRadius, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mShadowRadius, 0, mViewWidth, mShadowRadius));

                    //画上边和左边角落的阴影

                    drawArcRadialGradient(canvas, mShadowRadius, mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * mShadowRadius, 2 * mShadowRadius), 180, 90);

                    //画下边阴影
                    drawRectLinearGradient(canvas, 0, mViewHeight - mShadowRadius, 0, mViewHeight, colors, floats, Shader.TileMode.CLAMP, new RectF(mShadowRadius, mViewHeight - mShadowRadius, mViewWidth, mViewHeight));

                    //画左边和下边角落的阴影
                    drawArcRadialGradient(canvas, mShadowRadius, mViewHeight - mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mViewHeight - 2 * mShadowRadius, 2 * mShadowRadius, mViewHeight), 90, 90);

                } else if ((mShadowSide & LEFT) == LEFT && (mShadowSide & RIGHT) == RIGHT && (mShadowSide & TOP) != TOP && (mShadowSide & BOTTOM) == BOTTOM) {//左边右边和下边
                    //画左边阴影
                    drawRectLinearGradient(canvas, mShadowRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, mShadowRadius, mViewHeight - mShadowRadius));

                    //画右边阴影
                    drawRectLinearGradient(canvas, mViewWidth - mShadowRadius, 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - mShadowRadius, 0, mViewWidth, mViewHeight - mShadowRadius));

                    //画下边和右边角落的阴影
                    drawArcRadialGradient(canvas, mViewWidth - mShadowRadius, mViewHeight - mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - 2 * mShadowRadius, mViewHeight - 2 * mShadowRadius, mViewWidth, mViewHeight), 0, 90);

                    //画下边阴影
                    drawRectLinearGradient(canvas, 0, mViewHeight - mShadowRadius, 0, mViewHeight, colors, floats, Shader.TileMode.CLAMP, new RectF(mShadowRadius, mViewHeight - mShadowRadius, mViewWidth - mShadowRadius, mViewHeight));

                    //画左边和下边角落的阴影
                    drawArcRadialGradient(canvas, mShadowRadius, mViewHeight - mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mViewHeight - 2 * mShadowRadius, 2 * mShadowRadius, mViewHeight), 90, 90);

                } else if ((mShadowSide & LEFT) != LEFT && (mShadowSide & RIGHT) == RIGHT && (mShadowSide & TOP) == TOP && (mShadowSide & BOTTOM) != BOTTOM) {//上右

                    //画上边阴影
                    drawRectLinearGradient(canvas, 0, mShadowRadius, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, mViewWidth - mShadowRadius, mShadowRadius));

                    //画右边阴影
                    drawRectLinearGradient(canvas, mViewWidth - mShadowRadius, 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - mShadowRadius, mShadowRadius, mViewWidth, mViewHeight));

                    //画上边和右边角落的阴影
                    drawArcRadialGradient(canvas, mViewWidth - mShadowRadius, mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - 2 * mShadowRadius, 0, mViewWidth, 2 * mShadowRadius), 270, 90);

                } else if ((mShadowSide & LEFT) != LEFT && (mShadowSide & RIGHT) != RIGHT && (mShadowSide & TOP) == TOP && (mShadowSide & BOTTOM) == BOTTOM) {//上下

                    //画上边阴影
                    drawRectLinearGradient(canvas, 0, mShadowRadius, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, mViewWidth, mShadowRadius));
                    //画下边阴影
                    drawRectLinearGradient(canvas, 0, mViewHeight - mShadowRadius, 0, mViewHeight, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mViewHeight - mShadowRadius, mViewWidth, mViewHeight));

                } else if ((mShadowSide & LEFT) != LEFT && (mShadowSide & RIGHT) == RIGHT && (mShadowSide & TOP) == TOP && (mShadowSide & BOTTOM) == BOTTOM) {//右上下

                    //画上边阴影
                    drawRectLinearGradient(canvas, 0, mShadowRadius, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, mViewWidth - mShadowRadius, mShadowRadius));

                    //画右边阴影
                    drawRectLinearGradient(canvas, mViewWidth - mShadowRadius, 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - mShadowRadius, mShadowRadius, mViewWidth, mViewHeight - mShadowRadius));

                    //画上边和右边角落的阴影
                    drawArcRadialGradient(canvas, mViewWidth - mShadowRadius, mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - 2 * mShadowRadius, 0, mViewWidth, 2 * mShadowRadius), 270, 90);

                    //画下边阴影
                    drawRectLinearGradient(canvas, 0, mViewHeight - mShadowRadius, 0, mViewHeight, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mViewHeight - mShadowRadius, mViewWidth - mShadowRadius, mViewHeight));

                    //画右边和下边角落的阴影
                    drawArcRadialGradient(canvas, mViewWidth - mShadowRadius, mViewHeight - mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - 2 * mShadowRadius, mViewHeight - 2 * mShadowRadius, mViewWidth, mViewHeight), 0, 90);

                } else if ((mShadowSide & LEFT) != LEFT && (mShadowSide & RIGHT) == RIGHT && (mShadowSide & TOP) != TOP && (mShadowSide & BOTTOM) == BOTTOM) {//右下

                    //画右边阴影
                    drawRectLinearGradient(canvas, mViewWidth - mShadowRadius, 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - mShadowRadius, 0, mViewWidth, mViewHeight - mShadowRadius));

                    //画下边阴影
                    drawRectLinearGradient(canvas, 0, mViewHeight - mShadowRadius, 0, mViewHeight, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mViewHeight - mShadowRadius, mViewWidth - mShadowRadius, mViewHeight));

                    //画右边和下边角落的阴影
                    drawArcRadialGradient(canvas, mViewWidth - mShadowRadius, mViewHeight - mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - 2 * mShadowRadius, mViewHeight - 2 * mShadowRadius, mViewWidth, mViewHeight), 0, 90);

                } else if ((mShadowSide & LEFT) == LEFT && (mShadowSide & RIGHT) == RIGHT && (mShadowSide & TOP) == TOP && (mShadowSide & BOTTOM) == BOTTOM) {//左上右下
                    //画左边阴影
                    drawRectLinearGradient(canvas, mShadowRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mShadowRadius, mShadowRadius, mViewHeight - mShadowRadius));

                    //画上边阴影
                    drawRectLinearGradient(canvas, 0, mShadowRadius, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mShadowRadius, 0, mViewWidth - mShadowRadius, mShadowRadius));

                    //画上边和左边角落的阴影
                    drawArcRadialGradient(canvas, mShadowRadius, mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * mShadowRadius, 2 * mShadowRadius), 180, 90);

                    //画右边阴影
                    drawRectLinearGradient(canvas, mViewWidth - mShadowRadius, 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - mShadowRadius, mShadowRadius, mViewWidth, mViewHeight - mShadowRadius));

                    //画上边和右边角落的阴影
                    drawArcRadialGradient(canvas, mViewWidth - mShadowRadius, mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - 2 * mShadowRadius, 0, mViewWidth, 2 * mShadowRadius), 270, 90);

                    //画下边阴影
                    drawRectLinearGradient(canvas, 0, mViewHeight - mShadowRadius, 0, mViewHeight, colors, floats, Shader.TileMode.CLAMP, new RectF(mShadowRadius, mViewHeight - mShadowRadius, mViewWidth - mShadowRadius, mViewHeight));

                    //画右边和下边角落的阴影
                    drawArcRadialGradient(canvas, mViewWidth - mShadowRadius, mViewHeight - mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - 2 * mShadowRadius, mViewHeight - 2 * mShadowRadius, mViewWidth, mViewHeight), 0, 90);

                    //画左边和下边角落的阴影
                    drawArcRadialGradient(canvas, mShadowRadius, mViewHeight - mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mViewHeight - 2 * mShadowRadius, 2 * mShadowRadius, mViewHeight), 90, 90);
                }

            }

        } else if (mShadowShape == SHAPE_ROUND_RECTANGLE) {

            float[] floats = new float[]{0f, 1f};

            if ((mShadowSide & LEFT) == LEFT && (mShadowSide & TOP) != TOP && (mShadowSide & RIGHT) != RIGHT && (mShadowSide & BOTTOM) != BOTTOM) {
                //画左边阴影
                if ((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM) {

                    floats = creatThreePositionFloat();

                    colors = creatThreePositionColor();

                    drawLeftTopDownArcGradient(canvas,colors,floats);

                    drawLeftSide(canvas,colors,floats,0,mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewHeight);


                } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM){

                    floats = creatThreePositionFloat();

                    colors = creatThreePositionColor();

                    drawLeftSide(canvas,colors,floats,0,0,mShadowRadius+mRoundCornerRadius,mViewHeight-mRoundCornerRadius);

                    drawLeftBottomUpArcGradient(canvas,colors,floats);

                } else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM){

                    floats = creatThreePositionFloat();

                    colors = creatThreePositionColor();

                    drawLeftSide(canvas,colors,floats,0,mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewHeight-mRoundCornerRadius);

                    drawLeftBottomUpArcGradient(canvas,colors,floats);

                    drawLeftTopDownArcGradient(canvas,colors,floats);

                }
            } else if ((mShadowSide & LEFT) != LEFT && (mShadowSide & TOP) == TOP && (mShadowSide & RIGHT) != RIGHT && (mShadowSide & BOTTOM) != BOTTOM) {
                //画上边阴影
                if ((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP) {

                    floats = creatThreePositionFloat();

                    colors = creatThreePositionColor();

                    drawTopSide(canvas,colors,floats,mRoundCornerRadius,0,mViewWidth-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius);

                    drawLeftTopUpArcGradient(canvas,colors,floats);

                    drawRightTopUpArcGradient(canvas,colors,floats);

                } else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                    floats = creatThreePositionFloat();

                    colors = creatThreePositionColor();

                    drawTopSide(canvas,colors,floats,mRoundCornerRadius,0,mViewWidth,mShadowRadius+mRoundCornerRadius);

                    drawLeftTopUpArcGradient(canvas,colors,floats);

                } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP){

                    floats = creatThreePositionFloat();

                    colors = creatThreePositionColor();

                    drawTopSide(canvas,colors,floats,0,0,mViewWidth-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius);

                    drawRightTopUpArcGradient(canvas,colors,floats);

                }
            } else if ((mShadowSide & LEFT) != LEFT && (mShadowSide & TOP) != TOP && (mShadowSide & RIGHT) == RIGHT && (mShadowSide & BOTTOM) != BOTTOM) {
                //画右边阴影
                if ((mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM) {

                    floats = creatThreePositionFloat();

                    colors = creatThreePositionColor();

                    drawRightTopDownArcGradient(canvas,colors,floats);

                    drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mRoundCornerRadius,mViewWidth,mViewHeight-mRoundCornerRadius);

                    drawRightBottomUpArcGradient(canvas,colors,floats);

                } else if((mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM){

                    floats = creatThreePositionFloat();

                    colors = creatThreePositionColor();

                    drawRightTopDownArcGradient(canvas,colors,floats);

                    drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mRoundCornerRadius,mViewWidth,mViewHeight);

                } else if((mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM){

                    floats = creatThreePositionFloat();

                    colors = creatThreePositionColor();

                    drawRightBottomUpArcGradient(canvas,colors,floats);

                    drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,0,mViewWidth,mViewHeight-mRoundCornerRadius);

                }
            } else if ((mShadowSide & LEFT) != LEFT && (mShadowSide & TOP) != TOP && (mShadowSide & RIGHT) != RIGHT && (mShadowSide & BOTTOM) == BOTTOM) {
                //画下边阴影
                if ((mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM) {

                    floats = creatThreePositionFloat();

                    colors = creatThreePositionColor();

                    drawLeftBottomDownArcGradient(canvas,colors,floats);

                    drawBottomSide(canvas, colors, floats, mRoundCornerRadius, mViewHeight - mShadowRadius - mRoundCornerRadius, mViewWidth - mRoundCornerRadius, mViewHeight);

                    drawRightBottomDownArcGradient(canvas,colors,floats);

                } else if((mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM){

                    floats = creatThreePositionFloat();

                    colors = creatThreePositionColor();

                    drawLeftBottomDownArcGradient(canvas,colors,floats);

                    drawBottomSide(canvas, colors, floats, mRoundCornerRadius, mViewHeight - mShadowRadius - mRoundCornerRadius, mViewWidth, mViewHeight);

                } else if((mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM){

                    floats = creatThreePositionFloat();

                    colors = creatThreePositionColor();

                    drawBottomSide(canvas, colors, floats, 0, mViewHeight - mShadowRadius - mRoundCornerRadius, mViewWidth - mRoundCornerRadius, mViewHeight);

                    drawRightBottomDownArcGradient(canvas,colors,floats);
                }
            } else {

                if ((mShadowSide & LEFT) == LEFT && (mShadowSide & TOP) == TOP && (mShadowSide & RIGHT) != RIGHT && (mShadowSide & BOTTOM) != BOTTOM) {//左边和上边


                    if ((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP) {

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0,mShadowRadius+mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewHeight-mRoundCornerRadius);

                        drawTopSide(canvas,colors,floats,mRoundCornerRadius+mShadowRadius,0,mViewWidth-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius);

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawLeftBottomUpArcGradient(canvas,colors,floats);

                        drawRightTopUpArcGradient(canvas,colors,floats);

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP) {

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();
                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius + mRoundCornerRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight));
                        //画左上角阴影
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * (mShadowRadius + mRoundCornerRadius), 2 * (mShadowRadius + mRoundCornerRadius)), 180, 90);
                        //画上边阴影
                        drawRectLinearGradient(canvas, 0, mShadowRadius + mRoundCornerRadius, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mShadowRadius + mRoundCornerRadius, 0, mViewWidth, mShadowRadius + mRoundCornerRadius));

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP) {

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0,mShadowRadius,mShadowRadius+mRoundCornerRadius,mViewHeight-mRoundCornerRadius);

                        drawTopSide(canvas,colors,floats,mShadowRadius,0,mViewWidth,mShadowRadius+mRoundCornerRadius);

                        drawLeftBottomUpArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftTopRightAngle(canvas,colors,floats);

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP) {

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0,mShadowRadius,mShadowRadius+mRoundCornerRadius,mViewHeight);

                        drawTopSide(canvas,colors,floats,mShadowRadius,0,mViewWidth-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius);

                        drawRightTopUpArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftTopRightAngle(canvas,colors,floats);

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP) {

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();
                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius + mRoundCornerRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight));
                        //画上边和左边角落的阴影
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * (mShadowRadius + mRoundCornerRadius), 2 * (mShadowRadius + mRoundCornerRadius)), 180, 90);
                        //画上边阴影
                        drawRectLinearGradient(canvas, 0, mShadowRadius + mRoundCornerRadius, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mShadowRadius + mRoundCornerRadius, 0, mViewWidth - (mRoundCornerRadius), mShadowRadius));
                        //画右上角
                        RectF rf = new RectF(mViewWidth - (2 * mRoundCornerRadius), 0, mViewWidth, 2 * (mShadowRadius + mRoundCornerRadius));
                        drawArcRadialGradient(canvas, mViewWidth - mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 270, 90);
                    } else if ((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP) {

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();
                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius + mRoundCornerRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius)));
                        //画上边阴影
                        drawRectLinearGradient(canvas, 0, mShadowRadius + mRoundCornerRadius, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mShadowRadius + mRoundCornerRadius, 0, mViewWidth, mShadowRadius));
                        //画上边和左边角落的阴影
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * (mShadowRadius + mRoundCornerRadius), 2 * (mShadowRadius + mRoundCornerRadius)), 180, 90);
                        //画左下角阴影
                        RectF rf = new RectF(0, mViewHeight - (2 * mRoundCornerRadius), 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight);
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 90, 90);

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP) {

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawTopSide(canvas,colors,floats,mShadowRadius,0,mViewWidth-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius);

                        drawLeftSide(canvas,colors,floats,0,mShadowRadius,mShadowRadius+mRoundCornerRadius,mViewHeight-mRoundCornerRadius);

                        drawLeftBottomUpArcGradient(canvas,colors,floats);

                        drawRightTopUpArcGradient(canvas,colors,floats);


                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftTopRightAngle(canvas,colors,floats);

                    } else {
                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mShadowRadius, mShadowRadius, mViewHeight));
                        //画上边阴影
                        drawRectLinearGradient(canvas, 0, mShadowRadius, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mShadowRadius, 0, mViewWidth, mShadowRadius));
                        //画上边和左边角落的阴影
                        drawArcRadialGradient(canvas, mShadowRadius, mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * mShadowRadius, 2 * mShadowRadius), 180, 90);
                    }


                } else if ((mShadowSide & LEFT) == LEFT && (mShadowSide & RIGHT) == RIGHT && (mShadowSide & TOP) != TOP && (mShadowSide & BOTTOM) != BOTTOM) {//左边和右边

                    if ((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM) {

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();
                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius + mRoundCornerRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius)));
                        //画左上角阴影
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * (mShadowRadius + mRoundCornerRadius), 2 * (mRoundCornerRadius)), 180, 90);

                        //画左下角阴影
                        RectF rf = new RectF(0, mViewHeight - (2 * mRoundCornerRadius), 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight);
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 90, 90);
                        //画右上角阴影
                        rf = new RectF(mViewWidth - 2 * (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 2 * (mRoundCornerRadius));
                        drawArcRadialGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 270, 90);
                        //画右边阴影
                        drawRectLinearGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - (mShadowRadius + mRoundCornerRadius), mRoundCornerRadius, mViewWidth, mViewHeight - (mRoundCornerRadius)));

                        //画右下角阴影
                        rf = new RectF(mViewWidth - 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight - (2 * mRoundCornerRadius), mViewWidth, mViewHeight);
                        drawArcRadialGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), mViewHeight - (mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 0, 90);

//                        Paint paint = new Paint();
//                        paint.setColor(Color.GREEN);
//                        canvas.drawRect(rf,paint);

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM) {

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius + mRoundCornerRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight));
                        //画左上角阴影
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * (mShadowRadius + mRoundCornerRadius), 2 * (mRoundCornerRadius)), 180, 90);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();
                        //画右边阴影
                        drawRectLinearGradient(canvas, mViewWidth - mShadowRadius, 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - mShadowRadius, 0, mViewWidth, mViewHeight));
                    } else if ((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM) {

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius + mRoundCornerRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius)));
                        //画左下角阴影
                        RectF rf = new RectF(0, mViewHeight - (2 * mRoundCornerRadius), 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight);

                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 90, 90);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();
                        //画右边阴影
                        drawRectLinearGradient(canvas, mViewWidth - mShadowRadius, 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - mShadowRadius, 0, mViewWidth, mViewHeight));
                    } else if ((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM) {

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();
                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius + mRoundCornerRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius)));
                        //画左上角阴影
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * (mShadowRadius + mRoundCornerRadius), 2 * (mRoundCornerRadius)), 180, 90);

                        //画左下角阴影
                        RectF rf = new RectF(0, mViewHeight - (2 * mRoundCornerRadius), 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight);
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 90, 90);
                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();
                        //画右边阴影
                        drawRectLinearGradient(canvas, mViewWidth - mShadowRadius, 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - mShadowRadius, 0, mViewWidth, mViewHeight));

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM) {

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();
                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius + mRoundCornerRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius)));
                        //画左上角阴影
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * (mShadowRadius + mRoundCornerRadius), 2 * (mRoundCornerRadius)), 180, 90);

                        //画左下角阴影
                        RectF rf = new RectF(0, mViewHeight - (2 * mRoundCornerRadius), 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight);
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 90, 90);

                        //画右上角阴影
                        rf = new RectF(mViewWidth - 2 * (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 2 * (mRoundCornerRadius));
                        drawArcRadialGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 270, 90);
                        //画右边阴影
                        drawRectLinearGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - (mShadowRadius + mRoundCornerRadius), mRoundCornerRadius, mViewWidth, mViewHeight));

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM) {

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();
                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius + mRoundCornerRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius)));
                        //画左上角阴影
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * (mShadowRadius + mRoundCornerRadius), 2 * (mRoundCornerRadius)), 180, 90);

                        //画左下角阴影
                        RectF rf = new RectF(0, mViewHeight - (2 * mRoundCornerRadius), 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight);
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 90, 90);

                        //画右边阴影
                        drawRectLinearGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, mViewHeight - (mRoundCornerRadius)));

                        //画右下角阴影
                        rf = new RectF(mViewWidth - 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight - (2 * mRoundCornerRadius), mViewWidth, mViewHeight);
                        drawArcRadialGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), mViewHeight - (mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 0, 90);

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM) {


                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, mShadowRadius, mViewHeight));

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        //画右边阴影
                        drawRectLinearGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, mViewHeight - (mRoundCornerRadius)));

                        //画右下角阴影
                        RectF rf = new RectF(mViewWidth - 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight - (2 * mRoundCornerRadius), mViewWidth, mViewHeight);
                        drawArcRadialGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), mViewHeight - (mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 0, 90);

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM) {


                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, mShadowRadius, mViewHeight));

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        //画右上角阴影
                        RectF rf = new RectF(mViewWidth - 2 * (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 2 * (mRoundCornerRadius));
                        drawArcRadialGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 270, 90);
                        //画右边阴影
                        drawRectLinearGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - (mShadowRadius + mRoundCornerRadius), mRoundCornerRadius, mViewWidth, mViewHeight));

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM) {


                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, mShadowRadius, mViewHeight));

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        //画右上角阴影
                        RectF rf = new RectF(mViewWidth - 2 * (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 2 * (mRoundCornerRadius));
                        drawArcRadialGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 270, 90);
                        //画右边阴影
                        drawRectLinearGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - (mShadowRadius + mRoundCornerRadius), mRoundCornerRadius, mViewWidth, mViewHeight - (mRoundCornerRadius)));

                        //画右下角阴影
                        rf = new RectF(mViewWidth - 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight - (2 * mRoundCornerRadius), mViewWidth, mViewHeight);
                        drawArcRadialGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), mViewHeight - (mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 0, 90);

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM) {


                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius + mRoundCornerRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight));
                        //画左上角阴影
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * (mShadowRadius + mRoundCornerRadius), 2 * (mRoundCornerRadius)), 180, 90);


                        //画右上角阴影
                        RectF rf = new RectF(mViewWidth - 2 * (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 2 * (mRoundCornerRadius));
                        drawArcRadialGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 270, 90);
                        //画右边阴影
                        drawRectLinearGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - (mShadowRadius + mRoundCornerRadius), mRoundCornerRadius, mViewWidth, mViewHeight - (mRoundCornerRadius)));

                        //画右下角阴影
                        rf = new RectF(mViewWidth - 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight - (2 * mRoundCornerRadius), mViewWidth, mViewHeight);
                        drawArcRadialGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), mViewHeight - (mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 0, 90);

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM) {


                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius + mRoundCornerRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius)));
                        //画左下角阴影
                        RectF rf = new RectF(0, mViewHeight - (2 * mRoundCornerRadius), 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight);

                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 90, 90);


                        //画右上角阴影
                        rf = new RectF(mViewWidth - 2 * (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 2 * (mRoundCornerRadius));
                        drawArcRadialGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 270, 90);
                        //画右边阴影
                        drawRectLinearGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - (mShadowRadius + mRoundCornerRadius), mRoundCornerRadius, mViewWidth, mViewHeight - (mRoundCornerRadius)));

                        //画右下角阴影
                        rf = new RectF(mViewWidth - 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight - (2 * mRoundCornerRadius), mViewWidth, mViewHeight);
                        drawArcRadialGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), mViewHeight - (mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 0, 90);

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM) {


                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius + mRoundCornerRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius)));
                        //画左下角阴影
                        RectF rf = new RectF(0, mViewHeight - (2 * mRoundCornerRadius), 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight);

                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 90, 90);


                        //画右边阴影
                        drawRectLinearGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, mViewHeight - (mRoundCornerRadius)));

                        //画右下角阴影
                        rf = new RectF(mViewWidth - 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight - (2 * mRoundCornerRadius), mViewWidth, mViewHeight);
                        drawArcRadialGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), mViewHeight - (mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 0, 90);

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM) {


                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius + mRoundCornerRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius)));
                        //画左下角阴影
                        RectF rf = new RectF(0, mViewHeight - (2 * mRoundCornerRadius), 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight);

                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 90, 90);


                        //画右上角阴影
                        rf = new RectF(mViewWidth - 2 * (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 2 * (mRoundCornerRadius));
                        drawArcRadialGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 270, 90);
                        //画右边阴影
                        drawRectLinearGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - (mShadowRadius + mRoundCornerRadius), mRoundCornerRadius, mViewWidth, mViewHeight));

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM) {


                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius + mRoundCornerRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight));
                        //画左上角阴影
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * (mShadowRadius + mRoundCornerRadius), 2 * (mRoundCornerRadius)), 180, 90);


                        //画右上角阴影
                        RectF rf = new RectF(mViewWidth - 2 * (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 2 * (mRoundCornerRadius));
                        drawArcRadialGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 270, 90);
                        //画右边阴影
                        drawRectLinearGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - (mShadowRadius + mRoundCornerRadius), mRoundCornerRadius, mViewWidth, mViewHeight));

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM) {


                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius + mRoundCornerRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight));
                        //画左上角阴影
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * (mShadowRadius + mRoundCornerRadius), 2 * (mRoundCornerRadius)), 180, 90);


                        //画右边阴影
                        drawRectLinearGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, mViewHeight - (mRoundCornerRadius)));

                        //画右下角阴影
                        RectF rf = new RectF(mViewWidth - 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight - (2 * mRoundCornerRadius), mViewWidth, mViewHeight);
                        drawArcRadialGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), mViewHeight - (mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 0, 90);

                    } else {
                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, mShadowRadius, mViewHeight));

                        //画右边阴影
                        drawRectLinearGradient(canvas, mViewWidth - mShadowRadius, 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - mShadowRadius, 0, mViewWidth, mViewHeight));
                    }

                } else if ((mShadowSide & LEFT) == LEFT && (mShadowSide & RIGHT) != RIGHT && (mShadowSide & TOP) != TOP && (mShadowSide & BOTTOM) == BOTTOM) {//左边和下边
/////////////////////////////////////////////////////////////////////
                    if ((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM) {

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius + mRoundCornerRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mShadowRadius + mRoundCornerRadius)));
                        //画左上角阴影
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * (mShadowRadius + mRoundCornerRadius), 2 * (mRoundCornerRadius)), 180, 90);

                        //画下边阴影
                        drawRectLinearGradient(canvas, 0, mViewHeight -(mShadowRadius + mRoundCornerRadius), 0, mViewHeight, colors, floats, Shader.TileMode.CLAMP, new RectF(mShadowRadius + mRoundCornerRadius, mViewHeight-(mShadowRadius + mRoundCornerRadius), mViewWidth - (mRoundCornerRadius), mViewHeight));
//                        //画左下角阴影
                        RectF rf = new RectF(0, mViewHeight - 2 * (mShadowRadius + mRoundCornerRadius), 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight);
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mViewHeight - (mShadowRadius + mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 90, 90);
//                        //画右下角阴影
                        rf = new RectF(mViewWidth - 2*(mRoundCornerRadius), mViewHeight-2*(mShadowRadius + mRoundCornerRadius), mViewWidth, mViewHeight);
                        drawArcRadialGradient(canvas, mViewWidth - (mRoundCornerRadius), mViewHeight-(mShadowRadius + mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 0, 90);

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM) {

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius + mRoundCornerRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mShadowRadius)));
                        //画左上角阴影
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * (mShadowRadius + mRoundCornerRadius), 2*(mRoundCornerRadius)), 180, 90);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();
                        //画下边阴影
                        drawRectLinearGradient(canvas, 0, mViewHeight - mShadowRadius, 0, mViewHeight, colors, floats, Shader.TileMode.CLAMP, new RectF(mShadowRadius, mViewHeight - mShadowRadius, mViewWidth, mViewHeight));
                        //画左下直角的阴影
                        drawArcRadialGradient(canvas, mShadowRadius, mViewHeight - mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mViewHeight - 2 * mShadowRadius, 2 * mShadowRadius, mViewHeight), 90, 90);

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM) {

                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius , 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, mShadowRadius, mViewHeight - (mShadowRadius+mRoundCornerRadius)));

                        //画下边阴影
                        drawRectLinearGradient(canvas, 0, mViewHeight - mShadowRadius, 0, mViewHeight, colors, floats, Shader.TileMode.CLAMP, new RectF(mShadowRadius+mRoundCornerRadius, mViewHeight-mShadowRadius, mViewWidth, mViewHeight));

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();
                        //画左下角阴影
                        RectF rf = new RectF(0, mViewHeight - 2 * (mShadowRadius+mRoundCornerRadius), 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight);
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mViewHeight - (mShadowRadius+mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 90, 90);

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM) {

                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius , 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, mShadowRadius, mViewHeight - (mShadowRadius)));
                        //画下边阴影
                        drawRectLinearGradient(canvas, 0, mViewHeight - mShadowRadius, 0, mViewHeight, colors, floats, Shader.TileMode.CLAMP, new RectF(mShadowRadius, mViewHeight - mShadowRadius, mViewWidth-mRoundCornerRadius, mViewHeight));
                        //画左下直角的阴影
                        drawArcRadialGradient(canvas, mShadowRadius, mViewHeight - mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mViewHeight - 2 * mShadowRadius, 2 * mShadowRadius, mViewHeight), 90, 90);

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();
                        //画右下角阴影
                        RectF rf = new RectF(mViewWidth - 2*(mRoundCornerRadius), mViewHeight-2*(mShadowRadius + mRoundCornerRadius), mViewWidth, mViewHeight);
                        drawArcRadialGradient(canvas, mViewWidth - (mRoundCornerRadius), mViewHeight-(mShadowRadius + mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 0, 90);


                    } else if ((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM) {

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius + mRoundCornerRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mShadowRadius)));
                        //画左上角阴影
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * (mShadowRadius + mRoundCornerRadius), 2*(mRoundCornerRadius)), 180, 90);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();
                        //画下边阴影
                        drawRectLinearGradient(canvas, 0, mViewHeight - mShadowRadius, 0, mViewHeight, colors, floats, Shader.TileMode.CLAMP, new RectF(mShadowRadius, mViewHeight - mShadowRadius, mViewWidth-mRoundCornerRadius, mViewHeight));
                        //画左下直角的阴影
                        drawArcRadialGradient(canvas, mShadowRadius, mViewHeight - mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mViewHeight - 2 * mShadowRadius, 2 * mShadowRadius, mViewHeight), 90, 90);

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();
                        //画右下角阴影
                        RectF rf = new RectF(mViewWidth - 2*(mRoundCornerRadius), mViewHeight-2*(mShadowRadius + mRoundCornerRadius), mViewWidth, mViewHeight);
                        drawArcRadialGradient(canvas, mViewWidth - (mRoundCornerRadius), mViewHeight-(mShadowRadius + mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 0, 90);

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM) {

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius + mRoundCornerRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mShadowRadius + mRoundCornerRadius)));
                        //画左上角阴影
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * (mShadowRadius + mRoundCornerRadius), 2 * (mRoundCornerRadius)), 180, 90);

                        //画下边阴影
                        drawRectLinearGradient(canvas, 0, mViewHeight -(mShadowRadius + mRoundCornerRadius), 0, mViewHeight, colors, floats, Shader.TileMode.CLAMP, new RectF(mShadowRadius + mRoundCornerRadius, mViewHeight-(mShadowRadius + mRoundCornerRadius), mViewWidth, mViewHeight));
//                        //画左下角阴影
                        RectF rf = new RectF(0, mViewHeight - 2 * (mShadowRadius + mRoundCornerRadius), 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight);
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mViewHeight - (mShadowRadius + mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 90, 90);

                    } else if ((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM) {

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius + mRoundCornerRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, mShadowRadius + mRoundCornerRadius, mViewHeight - (mShadowRadius + mRoundCornerRadius)));

                        //画下边阴影
                        drawRectLinearGradient(canvas, 0, mViewHeight -(mShadowRadius + mRoundCornerRadius), 0, mViewHeight, colors, floats, Shader.TileMode.CLAMP, new RectF(mShadowRadius + mRoundCornerRadius, mViewHeight-(mShadowRadius + mRoundCornerRadius), mViewWidth - (mRoundCornerRadius), mViewHeight));
//                        //画左下角阴影
                        RectF rf = new RectF(0, mViewHeight - 2 * (mShadowRadius + mRoundCornerRadius), 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight);
                        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mViewHeight - (mShadowRadius + mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 90, 90);
//                        //画右下角阴影
                        rf = new RectF(mViewWidth - 2*(mRoundCornerRadius), mViewHeight-2*(mShadowRadius + mRoundCornerRadius), mViewWidth, mViewHeight);
                        drawArcRadialGradient(canvas, mViewWidth - (mRoundCornerRadius), mViewHeight-(mShadowRadius + mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 0, 90);

                    } else {
                        //画左边阴影
                        drawRectLinearGradient(canvas, mShadowRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, mShadowRadius, mViewHeight - mShadowRadius));

                        //画下边阴影
                        drawRectLinearGradient(canvas, 0, mViewHeight - mShadowRadius, 0, mViewHeight, colors, floats, Shader.TileMode.CLAMP, new RectF(mShadowRadius, mViewHeight - mShadowRadius, mViewWidth, mViewHeight));

                        //画左边和下边角落的阴影

                        drawArcRadialGradient(canvas, mShadowRadius, mViewHeight - mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mViewHeight - 2 * mShadowRadius, 2 * mShadowRadius, mViewHeight), 90, 90);
                    }
/////////////////////////////////////////////////////////////////////
                } else if ((mShadowSide & LEFT) != LEFT && (mShadowSide & RIGHT) == RIGHT && (mShadowSide & TOP) == TOP && (mShadowSide & BOTTOM) != BOTTOM) {//上边和右边
                    /////&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&////////
                    if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopUpArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mRoundCornerRadius,0,mViewWidth-(mShadowRadius+mRoundCornerRadius),mShadowRadius+mRoundCornerRadius);

                        drawRightTopArcGradient(canvas,colors,floats);

                        drawRightSide(canvas,colors,floats,mViewWidth-(mShadowRadius+mRoundCornerRadius),mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mRoundCornerRadius);

                        drawRightBottomUpArcGradient(canvas,colors,floats);
                    } else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopUpArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mRoundCornerRadius,0,mViewWidth-(mShadowRadius+mRoundCornerRadius),mShadowRadius+mRoundCornerRadius);

                        drawRightTopArcGradient(canvas,colors,floats);

                        drawRightSide(canvas,colors,floats,mViewWidth-(mShadowRadius+mRoundCornerRadius),mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopUpArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mRoundCornerRadius,0,mViewWidth-(mShadowRadius),mShadowRadius+mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-(mShadowRadius+mRoundCornerRadius),mShadowRadius,mViewWidth,mViewHeight-mRoundCornerRadius);

                        drawRightBottomUpArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();
                        //画上边和右边角落的阴影
                        drawArcRadialGradient(canvas, mViewWidth - mShadowRadius, mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - 2 * mShadowRadius, 0, mViewWidth, 2 * mShadowRadius), 270, 90);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawTopSide(canvas,colors,floats,0,0,mViewWidth-(mShadowRadius+mRoundCornerRadius),mShadowRadius+mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-(mShadowRadius+mRoundCornerRadius),mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mRoundCornerRadius);

                        drawRightBottomUpArcGradient(canvas,colors,floats);

                        drawRightTopArcGradient(canvas,colors,floats);


                    } else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopUpArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mRoundCornerRadius,0,mViewWidth-(mShadowRadius),mShadowRadius+mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-(mShadowRadius+mRoundCornerRadius),mShadowRadius,mViewWidth,mViewHeight);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();
                        //画上边和右边角落的阴影
                        drawArcRadialGradient(canvas, mViewWidth - mShadowRadius, mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - 2 * mShadowRadius, 0, mViewWidth, 2 * mShadowRadius), 270, 90);


                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawTopSide(canvas,colors,floats,0,0,mViewWidth-(mShadowRadius+mRoundCornerRadius),mShadowRadius+mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-(mShadowRadius+mRoundCornerRadius),mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight);

                        drawRightTopArcGradient(canvas,colors,floats);


                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawTopSide(canvas,colors,floats,0,0,mViewWidth-(mShadowRadius),mShadowRadius+mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-(mShadowRadius+mRoundCornerRadius),mShadowRadius,mViewWidth,mViewHeight-mRoundCornerRadius);

                        drawRightBottomUpArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();
                        //画上边和右边角落的阴影
                        drawArcRadialGradient(canvas, mViewWidth - mShadowRadius, mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - 2 * mShadowRadius, 0, mViewWidth, 2 * mShadowRadius), 270, 90);

                    }

                } else if((mShadowSide & LEFT) != LEFT && (mShadowSide & RIGHT) != RIGHT && (mShadowSide & TOP) == TOP && (mShadowSide & BOTTOM) == BOTTOM){//上下
                    if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopUpArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mRoundCornerRadius,0,mViewWidth-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius);

                        drawRightTopUpArcGradient(canvas,colors,floats);

                        drawLeftBottomDownArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius,mViewHeight - mRoundCornerRadius-mShadowRadius, mViewWidth-mRoundCornerRadius, mViewHeight);

                        drawRightBottomDownArcGradient(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawTopSide(canvas,colors,floats,0,0,mViewWidth-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius);

                        drawRightTopUpArcGradient(canvas,colors,floats);

                        drawLeftBottomDownArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius,mViewHeight - mRoundCornerRadius-mShadowRadius, mViewWidth-mRoundCornerRadius, mViewHeight);

                        drawRightBottomDownArcGradient(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopUpArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mRoundCornerRadius,0,mViewWidth,mShadowRadius+mRoundCornerRadius);

                        drawLeftBottomDownArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius,mViewHeight - mRoundCornerRadius-mShadowRadius, mViewWidth-mRoundCornerRadius, mViewHeight);

                        drawRightBottomDownArcGradient(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopUpArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mRoundCornerRadius,0,mViewWidth-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius);

                        drawRightTopUpArcGradient(canvas,colors,floats);

                        drawLeftBottomDownArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius,mViewHeight - mRoundCornerRadius-mShadowRadius, mViewWidth, mViewHeight);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopUpArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mRoundCornerRadius,0,mViewWidth-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius);

                        drawRightTopUpArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,0,mViewHeight - mRoundCornerRadius-mShadowRadius, mViewWidth-mRoundCornerRadius, mViewHeight);

                        drawRightBottomDownArcGradient(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawTopSide(canvas,colors,floats,0,0,mViewWidth,mShadowRadius+mRoundCornerRadius);

                        drawLeftBottomDownArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius,mViewHeight - mRoundCornerRadius-mShadowRadius, mViewWidth-mRoundCornerRadius, mViewHeight);

                        drawRightBottomDownArcGradient(canvas,colors,floats);
                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawTopSide(canvas,colors,floats,0,0,mViewWidth-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius);

                        drawRightTopUpArcGradient(canvas,colors,floats);

                        drawLeftBottomDownArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius,mViewHeight - mRoundCornerRadius-mShadowRadius, mViewWidth, mViewHeight);


                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawTopSide(canvas,colors,floats,0,0,mViewWidth-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius);

                        drawRightTopUpArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,0,mViewHeight - mRoundCornerRadius-mShadowRadius, mViewWidth-mRoundCornerRadius, mViewHeight);

                        drawRightBottomDownArcGradient(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopUpArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mRoundCornerRadius,0,mViewWidth,mShadowRadius+mRoundCornerRadius);

                        drawLeftBottomDownArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius,mViewHeight - mRoundCornerRadius-mShadowRadius, mViewWidth, mViewHeight);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopUpArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mRoundCornerRadius,0,mViewWidth,mShadowRadius+mRoundCornerRadius);

                        drawBottomSide(canvas,colors,floats,0,mViewHeight - mRoundCornerRadius-mShadowRadius, mViewWidth-mRoundCornerRadius, mViewHeight);

                        drawRightBottomDownArcGradient(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopUpArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mRoundCornerRadius,0,mViewWidth-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius);

                        drawRightTopUpArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,0,mViewHeight - mRoundCornerRadius-mShadowRadius, mViewWidth, mViewHeight);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawTopSide(canvas,colors,floats,0,0,mViewWidth,mShadowRadius+mRoundCornerRadius);

                        drawLeftBottomDownArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius,mViewHeight - mRoundCornerRadius-mShadowRadius, mViewWidth, mViewHeight);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawTopSide(canvas,colors,floats,0,0,mViewWidth,mShadowRadius+mRoundCornerRadius);

                        drawBottomSide(canvas,colors,floats,0,mViewHeight - mRoundCornerRadius-mShadowRadius, mViewWidth-mRoundCornerRadius, mViewHeight);

                        drawRightBottomDownArcGradient(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopUpArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mRoundCornerRadius,0,mViewWidth,mShadowRadius+mRoundCornerRadius);

                        drawBottomSide(canvas,colors,floats,0,mViewHeight - mRoundCornerRadius-mShadowRadius, mViewWidth, mViewHeight);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawTopSide(canvas,colors,floats,0,0,mViewWidth-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius);

                        drawRightTopUpArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,0,mViewHeight - mRoundCornerRadius-mShadowRadius, mViewWidth, mViewHeight);

                    }
                } else if((mShadowSide & LEFT) != LEFT && (mShadowSide & RIGHT) == RIGHT && (mShadowSide & TOP) != TOP && (mShadowSide & BOTTOM) == BOTTOM){//右下

                    if((mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawRightTopDownArcGradient(canvas,colors,floats);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mRoundCornerRadius,mViewWidth,mViewHeight-mRoundCornerRadius-mShadowRadius);

                        drawRightBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius-mRoundCornerRadius,mViewHeight);

                        drawLeftBottomDownArcGradient(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawRightTopDownArcGradient(canvas,colors,floats);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mRoundCornerRadius,mViewWidth,mViewHeight-mShadowRadius);

                        drawBottomSide(canvas,colors,floats,0,mViewHeight-mShadowRadius,mViewWidth-mShadowRadius,mViewHeight);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightBottomRightAngle(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,0,mViewWidth,mViewHeight-mRoundCornerRadius-mShadowRadius);

                        drawRightBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,0,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius-mRoundCornerRadius,mViewHeight);

                    } else if((mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,0,mViewWidth,mViewHeight-mShadowRadius);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius,mViewHeight);

                        drawLeftBottomDownArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightBottomRightAngle(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawRightTopDownArcGradient(canvas,colors,floats);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mRoundCornerRadius,mViewWidth,mViewHeight-mRoundCornerRadius-mShadowRadius);

                        drawRightBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,0,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius-mRoundCornerRadius,mViewHeight);


                    } else if((mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawRightTopDownArcGradient(canvas,colors,floats);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mRoundCornerRadius,mViewWidth,mViewHeight-mShadowRadius);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius,mViewHeight);

                        drawLeftBottomDownArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightBottomRightAngle(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,0,mViewWidth,mViewHeight-mRoundCornerRadius-mShadowRadius);

                        drawRightBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius-mRoundCornerRadius,mViewHeight);

                        drawLeftBottomDownArcGradient(canvas,colors,floats);
                    }

                }//2019/05/14
                else if ((mShadowSide & LEFT) == LEFT && (mShadowSide & RIGHT) == RIGHT && (mShadowSide & TOP) == TOP && (mShadowSide & BOTTOM) != BOTTOM) {//左边上边和右边

                    if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius));

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius + mRoundCornerRadius, 0, mViewWidth - (mShadowRadius + mRoundCornerRadius), mShadowRadius + mRoundCornerRadius);

                        drawLeftBottomUpArcGradient(canvas,colors,floats);

                        drawRightTopArcGradient(canvas,colors,floats);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mRoundCornerRadius);

                        drawRightBottomUpArcGradient(canvas,colors,floats);

                        ///////////////*********************/////////////
                    } else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight);

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius + mRoundCornerRadius, 0, mViewWidth - mShadowRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius,mViewWidth,mViewHeight);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightTopRightAngle(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius, mShadowRadius + mRoundCornerRadius, mViewHeight-mRoundCornerRadius);

                        drawLeftBottomUpArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius, 0, mViewWidth - mShadowRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius,mViewWidth,mViewHeight);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightTopRightAngle(canvas,colors,floats);

                        drawLeftTopRightAngle(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius, mShadowRadius + mRoundCornerRadius, mViewHeight);

                        drawTopSide(canvas,colors,floats,mShadowRadius, 0, mViewWidth - mShadowRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius,mViewWidth,mViewHeight-mRoundCornerRadius);

                        drawRightBottomUpArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightTopRightAngle(canvas,colors,floats);

                        drawLeftTopRightAngle(canvas,colors,floats);


                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius, mShadowRadius + mRoundCornerRadius, mViewHeight);

                        drawTopSide(canvas,colors,floats,mShadowRadius, 0, mViewWidth - mShadowRadius-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight);

                        drawRightTopArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftTopRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius));

                        drawTopSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius, 0, mViewWidth - mShadowRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius,mViewWidth,mViewHeight);

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawLeftBottomUpArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightTopRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight);

                        drawTopSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius, 0, mViewWidth - mShadowRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius,mViewWidth,mViewHeight-mRoundCornerRadius);

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawRightBottomUpArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightTopRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight);

                        drawTopSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius, 0, mViewWidth - mShadowRadius-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight);

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawRightTopArcGradient(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)!= CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius, mShadowRadius + mRoundCornerRadius, mViewHeight-mRoundCornerRadius);

                        drawTopSide(canvas,colors,floats,mShadowRadius, 0, mViewWidth - mShadowRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius,mViewWidth,mViewHeight-mRoundCornerRadius);

                        drawLeftBottomUpArcGradient(canvas,colors,floats);

                        drawRightBottomUpArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightTopRightAngle(canvas,colors,floats);

                        drawLeftTopRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius, mShadowRadius + mRoundCornerRadius, mViewHeight-mRoundCornerRadius);

                        drawTopSide(canvas,colors,floats,mShadowRadius, 0, mViewWidth - mShadowRadius-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight);

                        drawLeftBottomUpArcGradient(canvas,colors,floats);

                        drawRightTopArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftTopRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius, mShadowRadius, mViewHeight);

                        drawTopSide(canvas,colors,floats,mShadowRadius, 0, mViewWidth - mShadowRadius-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mRoundCornerRadius);

                        drawRightTopArcGradient(canvas,colors,floats);

                        drawRightBottomUpArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftTopRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)!= CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius));

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius + mRoundCornerRadius, 0, mViewWidth - mShadowRadius, mShadowRadius + mRoundCornerRadius);

                        drawLeftBottomUpArcGradient(canvas,colors,floats);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius,mViewWidth,mViewHeight-mRoundCornerRadius);

                        drawRightBottomUpArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightTopRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius));

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius + mRoundCornerRadius, 0, mViewWidth - mShadowRadius-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawLeftBottomUpArcGradient(canvas,colors,floats);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight);

                        drawRightTopArcGradient(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight);

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius + mRoundCornerRadius, 0, mViewWidth - mShadowRadius-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mRoundCornerRadius);

                        drawRightTopArcGradient(canvas,colors,floats);

                        drawRightBottomUpArcGradient(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius, mShadowRadius + mRoundCornerRadius, mViewHeight-mRoundCornerRadius);

                        drawTopSide(canvas,colors,floats,mShadowRadius , 0, mViewWidth - mShadowRadius-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mRoundCornerRadius);

                        drawRightTopArcGradient(canvas,colors,floats);

                        drawRightBottomUpArcGradient(canvas,colors,floats);

                        drawLeftBottomUpArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftTopRightAngle(canvas,colors,floats);
                    }


                } else if ((mShadowSide & LEFT) == LEFT && (mShadowSide & RIGHT) != RIGHT && (mShadowSide & TOP) == TOP && (mShadowSide & BOTTOM) == BOTTOM) {//左边上边和下边

                    if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mShadowRadius + mRoundCornerRadius));

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius + mRoundCornerRadius, 0, mViewWidth - mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mRoundCornerRadius,mViewHeight);

                        drawRightBottomDownArcGradient(canvas,colors,floats);

                        drawRightTopUpArcGradient(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - mShadowRadius);

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius + mRoundCornerRadius, 0, mViewWidth, mShadowRadius + mRoundCornerRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius,mViewHeight-mShadowRadius,mViewWidth,mViewHeight);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftBottomRightAngle(canvas,colors,floats);


                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - mShadowRadius-mRoundCornerRadius);

                        drawTopSide(canvas,colors,floats,mShadowRadius, 0, mViewWidth, mShadowRadius + mRoundCornerRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth,mViewHeight);

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftTopRightAngle(canvas,colors,floats);


                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - mShadowRadius);

                        drawTopSide(canvas,colors,floats,mShadowRadius, 0, mViewWidth, mShadowRadius + mRoundCornerRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mRoundCornerRadius,mViewHeight);

                        drawRightBottomDownArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftTopRightAngle(canvas,colors,floats);

                        drawLeftBottomRightAngle(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - mShadowRadius);

                        drawTopSide(canvas,colors,floats,mShadowRadius, 0, mViewWidth-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth,mViewHeight);

                        drawRightTopUpArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftTopRightAngle(canvas,colors,floats);

                        drawLeftBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mShadowRadius + mRoundCornerRadius));

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius + mRoundCornerRadius, 0, mViewWidth, mShadowRadius + mRoundCornerRadius);

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth,mViewHeight);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - mShadowRadius);

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius + mRoundCornerRadius, 0, mViewWidth, mShadowRadius + mRoundCornerRadius);

                        drawRightBottomDownArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mRoundCornerRadius,mViewHeight);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - mShadowRadius);

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius + mRoundCornerRadius, 0, mViewWidth-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth,mViewHeight);

                        drawRightTopUpArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)!= CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - mShadowRadius-mRoundCornerRadius);

                        drawTopSide(canvas,colors,floats,mShadowRadius, 0, mViewWidth, mShadowRadius + mRoundCornerRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mRoundCornerRadius,mViewHeight);

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawRightBottomDownArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftTopRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - mShadowRadius-mRoundCornerRadius);

                        drawTopSide(canvas,colors,floats,mShadowRadius, 0, mViewWidth-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth,mViewHeight);

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawRightTopUpArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftTopRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - mShadowRadius);

                        drawTopSide(canvas,colors,floats,mShadowRadius, 0, mViewWidth-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mRoundCornerRadius,mViewHeight);

                        drawRightBottomDownArcGradient(canvas,colors,floats);

                        drawRightTopUpArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftTopRightAngle(canvas,colors,floats);

                        drawLeftBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)!= CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mShadowRadius + mRoundCornerRadius));

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius + mRoundCornerRadius, 0, mViewWidth, mShadowRadius + mRoundCornerRadius);

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mRoundCornerRadius,mViewHeight);

                        drawRightBottomDownArcGradient(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mShadowRadius + mRoundCornerRadius));

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius + mRoundCornerRadius, 0, mViewWidth-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth,mViewHeight);

                        drawRightTopUpArcGradient(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - mShadowRadius);

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius + mRoundCornerRadius, 0, mViewWidth-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mRoundCornerRadius,mViewHeight);

                        drawRightTopUpArcGradient(canvas,colors,floats);

                        drawRightBottomDownArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mShadowRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - mShadowRadius-mRoundCornerRadius);

                        drawTopSide(canvas,colors,floats,mShadowRadius, 0, mViewWidth-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mRoundCornerRadius,mViewHeight);

                        drawRightTopUpArcGradient(canvas,colors,floats);

                        drawRightBottomDownArcGradient(canvas,colors,floats);

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftTopRightAngle(canvas,colors,floats);

                    }
                } else if ((mShadowSide & LEFT) == LEFT && (mShadowSide & RIGHT) == RIGHT && (mShadowSide & TOP) != TOP && (mShadowSide & BOTTOM) == BOTTOM) {//左边右边和下边
                    if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mShadowRadius+mRoundCornerRadius));

                        drawLeftTopDownArcGradient(canvas,colors,floats);

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius+mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mRoundCornerRadius-mShadowRadius,mViewHeight);

                        drawRightTopDownArcGradient(canvas,colors,floats);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mRoundCornerRadius,mViewWidth,mViewHeight-mRoundCornerRadius-mShadowRadius);

                        drawRightBottomArcGradient(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - mShadowRadius);

                        drawLeftTopDownArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,0,mViewWidth,mViewHeight-mShadowRadius);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftBottomRightAngle(canvas,colors,floats);

                        drawRightBottomRightAngle(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, 0, mShadowRadius + mRoundCornerRadius, mViewHeight - mShadowRadius-mRoundCornerRadius);

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,0,mViewWidth,mViewHeight-mShadowRadius);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightBottomRightAngle(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, 0, mShadowRadius + mRoundCornerRadius, mViewHeight - mShadowRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius-mRoundCornerRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,0,mViewWidth,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        drawRightBottomArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftBottomRightAngle(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, 0, mShadowRadius + mRoundCornerRadius, mViewHeight - mShadowRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mRoundCornerRadius,mViewWidth,mViewHeight-mShadowRadius);

                        drawRightTopDownArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftBottomRightAngle(canvas,colors,floats);

                        drawRightBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mShadowRadius+mRoundCornerRadius));

                        drawLeftTopDownArcGradient(canvas,colors,floats);

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius+mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,0,mViewWidth,mViewHeight-mShadowRadius);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mShadowRadius));

                        drawLeftTopDownArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius-mRoundCornerRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,0,mViewWidth,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        drawRightBottomArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mShadowRadius));

                        drawLeftTopDownArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mRoundCornerRadius,mViewWidth,mViewHeight-mShadowRadius);

                        drawRightTopDownArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftBottomRightAngle(canvas,colors,floats);

                        drawRightBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)!= CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, 0, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius+mShadowRadius));

                        drawBottomSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius-mRoundCornerRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,0,mViewWidth,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawRightBottomArcGradient(canvas,colors,floats);


                    }else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, 0, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius+mShadowRadius));

                        drawBottomSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mRoundCornerRadius,mViewWidth,mViewHeight-mShadowRadius);

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawRightTopDownArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, 0, mShadowRadius + mRoundCornerRadius, mViewHeight - mShadowRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius-mRoundCornerRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mRoundCornerRadius,mViewWidth,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        drawRightTopDownArcGradient(canvas,colors,floats);

                        drawRightBottomArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)!= CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mShadowRadius+mRoundCornerRadius));

                        drawLeftTopDownArcGradient(canvas,colors,floats);

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius+mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mRoundCornerRadius-mShadowRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,0,mViewWidth,mViewHeight-mRoundCornerRadius-mShadowRadius);

                        drawRightBottomArcGradient(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - (mShadowRadius+mRoundCornerRadius));

                        drawLeftTopDownArcGradient(canvas,colors,floats);

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius+mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mRoundCornerRadius,mViewWidth,mViewHeight-mShadowRadius);

                        drawRightTopDownArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mViewHeight - mShadowRadius);

                        drawLeftTopDownArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mRoundCornerRadius-mShadowRadius,mViewHeight);

                        drawRightTopDownArcGradient(canvas,colors,floats);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mRoundCornerRadius,mViewWidth,mViewHeight-mRoundCornerRadius-mShadowRadius);

                        drawRightBottomArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftSide(canvas,colors,floats,0, 0, mShadowRadius + mRoundCornerRadius, mViewHeight - (mShadowRadius+mRoundCornerRadius));

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius+mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mRoundCornerRadius-mShadowRadius,mViewHeight);

                        drawRightTopDownArcGradient(canvas,colors,floats);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mRoundCornerRadius,mViewWidth,mViewHeight-mRoundCornerRadius-mShadowRadius);

                        drawRightBottomArcGradient(canvas,colors,floats);
                    }
                } else if ((mShadowSide & LEFT) != LEFT && (mShadowSide & RIGHT) == RIGHT && (mShadowSide & TOP) == TOP && (mShadowSide & BOTTOM) == BOTTOM) {//右上下

                    if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopUpArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mRoundCornerRadius, 0, mViewWidth - (mShadowRadius + mRoundCornerRadius), mShadowRadius + mRoundCornerRadius);

                        drawLeftBottomDownArcGradient(canvas,colors,floats);

                        drawRightTopArcGradient(canvas,colors,floats);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mRoundCornerRadius-mShadowRadius);

                        drawRightBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mRoundCornerRadius-mShadowRadius,mViewHeight);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopUpArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mRoundCornerRadius, 0, mViewWidth - mShadowRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius,mViewWidth,mViewHeight-mShadowRadius);

                        drawBottomSide(canvas,colors,floats,0,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius,mViewHeight);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightBottomRightAngle(canvas,colors,floats);

                        drawRightTopRightAngle(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawTopSide(canvas,colors,floats,0, 0, mViewWidth - mShadowRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius,mViewWidth,mViewHeight-mShadowRadius);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius,mViewHeight);

                        drawLeftBottomDownArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightBottomRightAngle(canvas,colors,floats);

                        drawRightTopRightAngle(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawTopSide(canvas,colors,floats,0, 0, mViewWidth - mShadowRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius,mViewWidth,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        drawBottomSide(canvas,colors,floats,0,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius-mRoundCornerRadius,mViewHeight);

                        drawRightBottomArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightTopRightAngle(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawTopSide(canvas,colors,floats,0, 0, mViewWidth - mShadowRadius-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mShadowRadius);

                        drawBottomSide(canvas,colors,floats,0,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius,mViewHeight);

                        drawRightTopArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawTopSide(canvas,colors,floats,mRoundCornerRadius, 0, mViewWidth - mShadowRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius,mViewWidth,mViewHeight-mShadowRadius);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius,mViewHeight);

                        drawLeftTopUpArcGradient(canvas,colors,floats);

                        drawLeftBottomDownArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightBottomRightAngle(canvas,colors,floats);

                        drawRightTopRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawTopSide(canvas,colors,floats,mRoundCornerRadius, 0, mViewWidth - mShadowRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius,mViewWidth,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        drawBottomSide(canvas,colors,floats,0,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius-mRoundCornerRadius,mViewHeight);

                        drawLeftTopUpArcGradient(canvas,colors,floats);

                        drawRightBottomArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightTopRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawTopSide(canvas,colors,floats,mRoundCornerRadius, 0, mViewWidth - mShadowRadius-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mShadowRadius);

                        drawBottomSide(canvas,colors,floats,0,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius,mViewHeight);

                        drawLeftTopUpArcGradient(canvas,colors,floats);

                        drawRightTopArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)!= CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawTopSide(canvas,colors,floats,0, 0, mViewWidth - mShadowRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius,mViewWidth,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius-mRoundCornerRadius,mViewHeight);

                        drawRightBottomArcGradient(canvas,colors,floats);

                        drawLeftBottomDownArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightTopRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawTopSide(canvas,colors,floats,0, 0, mViewWidth - mShadowRadius- mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mShadowRadius);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius,mViewHeight);

                        drawRightTopArcGradient(canvas,colors,floats);

                        drawLeftBottomDownArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawTopSide(canvas,colors,floats,0, 0, mViewWidth - mShadowRadius- mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        drawBottomSide(canvas,colors,floats,0,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius-mRoundCornerRadius,mViewHeight);

                        drawRightTopArcGradient(canvas,colors,floats);

                        drawRightBottomArcGradient(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)!= CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopUpArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mRoundCornerRadius, 0, mViewWidth - mShadowRadius, mShadowRadius + mRoundCornerRadius);

                        drawLeftBottomDownArcGradient(canvas,colors,floats);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius,mViewWidth,mViewHeight-mRoundCornerRadius-mShadowRadius);

                        drawRightBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mRoundCornerRadius-mShadowRadius,mViewHeight);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightTopRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopUpArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mRoundCornerRadius, 0, mViewWidth - (mShadowRadius + mRoundCornerRadius), mShadowRadius + mRoundCornerRadius);

                        drawLeftBottomDownArcGradient(canvas,colors,floats);

                        drawRightTopArcGradient(canvas,colors,floats);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mShadowRadius);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius,mViewHeight);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopUpArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mRoundCornerRadius, 0, mViewWidth - (mShadowRadius + mRoundCornerRadius), mShadowRadius + mRoundCornerRadius);

                        drawRightTopArcGradient(canvas,colors,floats);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mRoundCornerRadius-mShadowRadius);

                        drawRightBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,0,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mRoundCornerRadius-mShadowRadius,mViewHeight);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawTopSide(canvas,colors,floats,0, 0, mViewWidth - (mShadowRadius + mRoundCornerRadius), mShadowRadius + mRoundCornerRadius);

                        drawLeftBottomDownArcGradient(canvas,colors,floats);

                        drawRightTopArcGradient(canvas,colors,floats);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mRoundCornerRadius-mShadowRadius);

                        drawRightBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mRoundCornerRadius-mShadowRadius,mViewHeight);

                    }

                }else if ((mShadowSide & LEFT) == LEFT && (mShadowSide & RIGHT) == RIGHT && (mShadowSide & TOP) == TOP && (mShadowSide & BOTTOM) == BOTTOM) {//左上右下

                    if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius, 0, mViewWidth - (mShadowRadius + mRoundCornerRadius), mShadowRadius + mRoundCornerRadius);

                        drawLeftSide(canvas,colors,floats,0,mShadowRadius+mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mRoundCornerRadius-mShadowRadius,mViewHeight);

                        drawRightBottomArcGradient(canvas,colors,floats);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mRoundCornerRadius-mShadowRadius);

                        drawRightTopArcGradient(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius, 0, mViewWidth - mShadowRadius, mShadowRadius + mRoundCornerRadius);

                        drawLeftSide(canvas,colors,floats,0,mShadowRadius+mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius,mViewHeight-mShadowRadius,mViewWidth-mShadowRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius,mViewWidth,mViewHeight-mShadowRadius);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftBottomRightAngle(canvas,colors,floats);

                        drawRightBottomRightAngle(canvas,colors,floats);

                        drawRightTopRightAngle(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius, 0, mViewWidth - mShadowRadius, mShadowRadius + mRoundCornerRadius);

                        drawLeftSide(canvas,colors,floats,0,mShadowRadius,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius,mViewWidth-mShadowRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius,mViewWidth,mViewHeight-mShadowRadius);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftTopRightAngle(canvas,colors,floats);

                        drawRightBottomRightAngle(canvas,colors,floats);

                        drawRightTopRightAngle(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawRightBottomArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius, 0, mViewWidth - mShadowRadius, mShadowRadius + mRoundCornerRadius);

                        drawLeftSide(canvas,colors,floats,0,mShadowRadius,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius,mViewHeight-mShadowRadius,mViewWidth-mShadowRadius-mRoundCornerRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius,mViewWidth,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftTopRightAngle(canvas,colors,floats);

                        drawLeftBottomRightAngle(canvas,colors,floats);

                        drawRightTopRightAngle(canvas,colors,floats);

                    } else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawRightTopArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius, 0, mViewWidth - mShadowRadius-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawLeftSide(canvas,colors,floats,0,mShadowRadius,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius,mViewHeight-mShadowRadius,mViewWidth-mShadowRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mShadowRadius);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftTopRightAngle(canvas,colors,floats);

                        drawLeftBottomRightAngle(canvas,colors,floats);

                        drawRightBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius, 0, mViewWidth - mShadowRadius, mShadowRadius + mRoundCornerRadius);

                        drawLeftSide(canvas,colors,floats,0,mShadowRadius+mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius,mViewWidth,mViewHeight-mShadowRadius);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightTopRightAngle(canvas,colors,floats);

                        drawRightBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) != CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawRightBottomArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius, 0, mViewWidth - mShadowRadius, mShadowRadius + mRoundCornerRadius);

                        drawLeftSide(canvas,colors,floats,0,mShadowRadius+mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius-mRoundCornerRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius,mViewWidth,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightTopRightAngle(canvas,colors,floats);

                        drawLeftBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawRightTopArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius, 0, mViewWidth - mShadowRadius-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawLeftSide(canvas,colors,floats,0,mShadowRadius+mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mShadowRadius);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightBottomRightAngle(canvas,colors,floats);

                        drawLeftBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)!= CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawRightBottomArcGradient(canvas,colors,floats);

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius, 0, mViewWidth - mShadowRadius, mShadowRadius + mRoundCornerRadius);

                        drawLeftSide(canvas,colors,floats,0,mShadowRadius,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius-mRoundCornerRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius,mViewWidth,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftTopRightAngle(canvas,colors,floats);

                        drawRightTopRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawRightTopArcGradient(canvas,colors,floats);

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius, 0, mViewWidth - mShadowRadius-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawLeftSide(canvas,colors,floats,0,mShadowRadius,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mShadowRadius);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftTopRightAngle(canvas,colors,floats);

                        drawRightBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){


                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawRightTopArcGradient(canvas,colors,floats);

                        drawRightBottomArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius, 0, mViewWidth - mShadowRadius-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawLeftSide(canvas,colors,floats,0,mShadowRadius,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius-mRoundCornerRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftTopRightAngle(canvas,colors,floats);

                        drawLeftBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)!= CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius, 0, mViewWidth - mShadowRadius, mShadowRadius + mRoundCornerRadius);

                        drawLeftSide(canvas,colors,floats,0,mShadowRadius+mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mRoundCornerRadius-mShadowRadius,mViewHeight);

                        drawRightBottomArcGradient(canvas,colors,floats);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius,mViewWidth,mViewHeight-mRoundCornerRadius-mShadowRadius);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightTopRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) != CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius, 0, mViewWidth - mShadowRadius-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawLeftSide(canvas,colors,floats,0,mShadowRadius+mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawBottomSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mShadowRadius);

                        drawRightTopArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawRightBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) == CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) != CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftTopArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius, 0, mViewWidth - mShadowRadius-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawLeftSide(canvas,colors,floats,0,mShadowRadius+mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius-mRoundCornerRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        drawRightTopArcGradient(canvas,colors,floats);

                        drawRightBottomArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftBottomRightAngle(canvas,colors,floats);

                    }else if((mCornerPosition & CORNER_LEFT_TOP) != CORNER_LEFT_TOP && (mCornerPosition & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM && (mCornerPosition & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM && (mCornerPosition & CORNER_RIGHT_TOP)== CORNER_RIGHT_TOP){

                        floats = creatThreePositionFloat();

                        colors = creatThreePositionColor();

                        drawLeftBottomArcGradient(canvas,colors,floats);

                        drawTopSide(canvas,colors,floats,mShadowRadius, 0, mViewWidth - mShadowRadius-mRoundCornerRadius, mShadowRadius + mRoundCornerRadius);

                        drawLeftSide(canvas,colors,floats,0,mShadowRadius,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        drawBottomSide(canvas,colors,floats,mShadowRadius+mRoundCornerRadius,mViewHeight-mShadowRadius-mRoundCornerRadius,mViewWidth-mShadowRadius-mRoundCornerRadius,mViewHeight);

                        drawRightSide(canvas,colors,floats,mViewWidth-mShadowRadius-mRoundCornerRadius,mShadowRadius+mRoundCornerRadius,mViewWidth,mViewHeight-mShadowRadius-mRoundCornerRadius);

                        drawRightTopArcGradient(canvas,colors,floats);

                        drawRightBottomArcGradient(canvas,colors,floats);

                        floats = creatTwoPositionFloat();

                        colors = creatTwoPositionColor();

                        drawLeftTopRightAngle(canvas,colors,floats);

                    }
                }
            }

        }
    }

    /**
     * 画左上角阴影
     * @param canvas
     * @param colors
     * @param floats
     */
    private void drawLeftTopArcGradient(Canvas canvas,int[] colors,float[] floats){

        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * (mShadowRadius + mRoundCornerRadius), 2 * (mShadowRadius + mRoundCornerRadius)), 180, 90);

    }

    /**
     * 画左上角阴影，圆角朝下
     * @param canvas
     * @param colors
     * @param floats
     */
    private void drawLeftTopDownArcGradient(Canvas canvas,int[] colors,float[] floats){

        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * (mShadowRadius + mRoundCornerRadius), 2 * (mRoundCornerRadius)), 180, 90);

    }

    /**
     * 画左上角阴影，圆角朝上
     * @param canvas
     * @param colors
     * @param floats
     */
    private void drawLeftTopUpArcGradient(Canvas canvas,int[] colors,float[] floats){

        drawArcRadialGradient(canvas, mRoundCornerRadius, mShadowRadius+mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, 0, 2 * (mRoundCornerRadius), 2 * (mShadowRadius + mRoundCornerRadius)), 180, 90);

    }

    /**
     * 画右上角阴影
     * @param canvas
     * @param colors
     * @param floats
     */
    private void drawRightTopArcGradient(Canvas canvas,int[] colors,float[] floats){

        RectF rf = new RectF(mViewWidth - 2*(mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 2*(mShadowRadius + mRoundCornerRadius));

        drawArcRadialGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 270, 90);

    }
    /**
     * 画右上角阴影，圆角朝上
     * @param canvas
     * @param colors
     * @param floats
     */
    private void drawRightTopUpArcGradient(Canvas canvas,int[] colors,float[] floats){

        RectF rf = new RectF(mViewWidth - (2 * mRoundCornerRadius), 0, mViewWidth, 2 * (mShadowRadius + mRoundCornerRadius));

        drawArcRadialGradient(canvas, mViewWidth - mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 270, 90);

    }
    /**
     * 画右上角阴影，圆角朝下
     * @param canvas
     * @param colors
     * @param floats
     */
    private void drawRightTopDownArcGradient(Canvas canvas,int[] colors,float[] floats){

        RectF rf = new RectF(mViewWidth - 2*(mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 2 * mRoundCornerRadius);

        drawArcRadialGradient(canvas, mViewWidth - mShadowRadius - mRoundCornerRadius, mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 270, 90);

    }

    /**
     * 画左下角阴影
     * @param canvas
     * @param colors
     * @param floats
     */
    private void drawLeftBottomArcGradient(Canvas canvas,int[] colors,float[] floats){

        RectF rf = new RectF(0, mViewHeight - 2 * (mShadowRadius + mRoundCornerRadius), 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight);

        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mViewHeight - (mShadowRadius + mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 90, 90);

    }

    /**
     * 画左下角阴影，圆角朝上
     * @param canvas
     * @param colors
     * @param floats
     */
    private void drawLeftBottomUpArcGradient(Canvas canvas,int[] colors,float[] floats){

        RectF rf = new RectF(0, mViewHeight - (2 * mRoundCornerRadius), 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight);

        drawArcRadialGradient(canvas, mShadowRadius + mRoundCornerRadius, mViewHeight - (mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 90, 90);

    }

    /**
     * 画左下角阴影，圆角朝下
     * @param canvas
     * @param colors
     * @param floats
     */
    private void drawLeftBottomDownArcGradient(Canvas canvas,int[] colors,float[] floats){

        RectF rf = new RectF(0, mViewHeight - 2 * (mShadowRadius + mRoundCornerRadius), 2 * mRoundCornerRadius, mViewHeight);

        drawArcRadialGradient(canvas,  mRoundCornerRadius, mViewHeight - (mShadowRadius + mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 90, 90);

    }

    /**
     * 画右下角阴影
     * @param canvas
     * @param colors
     * @param floats
     */
    private void drawRightBottomArcGradient(Canvas canvas,int[] colors,float[] floats){

        RectF rf = new RectF(mViewWidth - 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight - 2 * (mShadowRadius + mRoundCornerRadius), mViewWidth, mViewHeight);

        drawArcRadialGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), mViewHeight - (mShadowRadius + mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 0, 90);

    }

    /**
     * 画右下角阴影，圆角朝上
     * @param canvas
     * @param colors
     * @param floats
     */
    private void drawRightBottomUpArcGradient(Canvas canvas,int[] colors,float[] floats){

        RectF rf = new RectF(mViewWidth - 2 * (mShadowRadius + mRoundCornerRadius), mViewHeight - (2 * mRoundCornerRadius), mViewWidth, mViewHeight);

        drawArcRadialGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), mViewHeight - mRoundCornerRadius, mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 0, 90);

    }
    /**
     * 画右下角阴影，圆角朝下
     * @param canvas
     * @param colors
     * @param floats
     */
    private void drawRightBottomDownArcGradient(Canvas canvas,int[] colors,float[] floats){

        RectF rf = new RectF(mViewWidth - 2 * mRoundCornerRadius, mViewHeight - 2 * (mShadowRadius + mRoundCornerRadius), mViewWidth, mViewHeight);

        drawArcRadialGradient(canvas, mViewWidth - mRoundCornerRadius, mViewHeight - (mShadowRadius + mRoundCornerRadius), mShadowRadius + mRoundCornerRadius, colors, floats, Shader.TileMode.CLAMP, rf, 0, 90);

    }

    /**
     * 圆角矩形，左边阴影
     * @param canvas
     * @param colors
     * @param floats
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    private void drawLeftSide(Canvas canvas,int[] colors,float[] floats,float startX,float startY,float endX,float endY){

        drawRectLinearGradient(canvas, mShadowRadius + mRoundCornerRadius, 0, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(startX, startY, endX, endY));

    }

    /**
     * 圆角矩形，下边阴影
     * @param canvas
     * @param colors
     * @param floats
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    private void drawBottomSide(Canvas canvas,int[] colors,float[] floats,float startX,float startY,float endX,float endY){

        drawRectLinearGradient(canvas, 0, mViewHeight -(mShadowRadius + mRoundCornerRadius), 0, mViewHeight, colors, floats, Shader.TileMode.CLAMP, new RectF(startX, startY, endX, endY));

    }

    /**
     * 圆角矩形，右边阴影
     * @param canvas
     * @param colors
     * @param floats
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    private void drawRightSide(Canvas canvas,int[] colors,float[] floats,float startX,float startY,float endX,float endY){


        drawRectLinearGradient(canvas, mViewWidth - (mShadowRadius + mRoundCornerRadius), 0, mViewWidth, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(startX, startY, endX, endY));

    }

    /**
     * 圆角矩形，上边阴影
     * @param canvas
     * @param colors
     * @param floats
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    private void drawTopSide(Canvas canvas,int[] colors,float[] floats,float startX,float startY,float endX,float endY){

        drawRectLinearGradient(canvas, 0, mShadowRadius + mRoundCornerRadius, 0, 0, colors, floats, Shader.TileMode.CLAMP, new RectF(startX, startY, endX, endY));

    }

    /**
     * 画左下直角的阴影
     * @param canvas
     * @param colors
     * @param floats
     */
    private void drawLeftBottomRightAngle(Canvas canvas,int[] colors,float[] floats){
        drawArcRadialGradient(canvas, mShadowRadius, mViewHeight - mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0, mViewHeight - 2*mShadowRadius, 2*mShadowRadius, mViewHeight), 90, 90);
    }
    /**
     * 画右下直角的阴影
     * @param canvas
     * @param colors
     * @param floats
     */
    private void drawRightBottomRightAngle(Canvas canvas,int[] colors,float[] floats){

        drawArcRadialGradient(canvas, mViewWidth - mShadowRadius, mViewHeight-mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth - 2*mShadowRadius,mViewHeight-2*mShadowRadius,mViewWidth,mViewHeight), 0, 90);

    }

    /**
     * 画左上直角的阴影
     * @param canvas
     * @param colors
     * @param floats
     */
    private void drawLeftTopRightAngle(Canvas canvas,int[] colors,float[] floats){

        drawArcRadialGradient(canvas, mShadowRadius, mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(0,0,2*mShadowRadius,2*mShadowRadius), 180, 90);

    }
    /**
     * 画右上直角的阴影
     * @param canvas
     * @param colors
     * @param floats
     */
    private void drawRightTopRightAngle(Canvas canvas,int[] colors,float[] floats){

        drawArcRadialGradient(canvas, mViewWidth-mShadowRadius, mShadowRadius, mShadowRadius, colors, floats, Shader.TileMode.CLAMP, new RectF(mViewWidth-2*mShadowRadius,0,mViewWidth,2*mShadowRadius), 270, 90);

    }


    private float[] creatTwoPositionFloat() {

        float[] floats = new float[]{0.01f, 1f};

        return floats;

    }

    private float[] creatThreePositionFloat() {

        float[] floats = new float[]{mRoundCornerRadius / (mShadowRadius + mRoundCornerRadius), mRoundCornerRadius / (mShadowRadius + mRoundCornerRadius), 1f};

        return floats;
    }

    private int[] creatTwoPositionColor() {

        int[] colors = new int[]{mShadowColor, 0x00ffffff};

        return colors;

    }

    private int[] creatThreePositionColor() {

        int[] colors = new int[]{0x00ffffff, mShadowColor, 0x00ffffff};

        return colors;

    }

    private void drawRectLinearGradient(Canvas canvas, float xStart, float yStart, float xEnd, float yEnd, int[] colors, float[] positions, Shader.TileMode tile, RectF rectF) {

        LinearGradient linearGradient = new LinearGradient(xStart, yStart, xEnd, yEnd, colors, positions, tile);

        mPaint.setShader(linearGradient);

        canvas.drawRect(rectF, mPaint);

    }

    private void drawArcRadialGradient(Canvas canvas, float centerX, float centerY, float radius, int[] colors, float[] positions, Shader.TileMode tile, RectF rectF, float startAngle, float sweepAngle) {

        RadialGradient linearGradient = new RadialGradient(centerX, centerY, radius, colors, positions, tile);

        mPaint.setShader(linearGradient);

        canvas.drawArc(rectF, startAngle, sweepAngle, true, mPaint);
    }



    private float dip2px(float dpValue) {

        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();

        float scale = dm.density;

        return (dpValue * scale + 0.5F);

    }

    public void setShadowColor(int shadowColor) {

        mShadowColor = shadowColor;

        requestLayout();

        postInvalidate();

    }

    public void setShadowRadius(float shadowRadius) {

        mShadowRadius = shadowRadius;

        requestLayout();

        postInvalidate();

    }
}
