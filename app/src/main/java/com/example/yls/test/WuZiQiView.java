package com.example.yls.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yls on 2017/6/8.
 */

public class WuZiQiView extends View{

    private int mPanelWidth;
    private float mLineHeight;
    private int MAX_LINE = 10;

    private Paint mPaint = new Paint();

    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    private float ratioPieceOfLineheight = 3*1.0f/4;

    private boolean mIsWhite = true;
    private List<Point> mWhiteArray = new ArrayList<>();
    private List<Point> mBlackArray = new ArrayList<>();

    public WuZiQiView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setBackgroundColor(0x44ff0000);
        init();
    }

    private void init() {
        mPaint.setColor(0x99000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mWhitePiece = BitmapFactory.decodeResource(getResources(),R.drawable.stone_w2);
        mBlackPiece = BitmapFactory.decodeResource(getResources(),R.drawable.stone_b1);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize,heightSize);

        if(widthMode==MeasureSpec.UNSPECIFIED){
            width = heightSize;
        }else if(heightMode==MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }

        setMeasuredDimension(width,width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mPanelWidth = w;
        mLineHeight = mPanelWidth*1.0f/MAX_LINE;

        int pieceWidth = (int) (mLineHeight*ratioPieceOfLineheight);

        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece,pieceWidth,pieceWidth,false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece,pieceWidth,pieceWidth,false);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        if(action==MotionEvent.ACTION_UP){
            int x = (int) event.getX();
            int y = (int) event.getY();

            Point p = getValidPoint(x,y);

            if(mWhiteArray.contains(p) || mBlackArray.contains(p)){
                return false;
            }

            if(mIsWhite){
                mWhiteArray.add(p);
            }else {
                mBlackArray.add(p);
            }
            invalidate();
            mIsWhite=!mIsWhite;

        }


        return true;
    }

    private Point getValidPoint(int x, int y) {

        return new Point((int)(x/mLineHeight),(int)(y/mLineHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPieces(canvas);
    }

    private void drawPieces(Canvas canvas) {

       for(int i=0;i<mWhiteArray.size();i++){
            Point whitePiece = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece,
                    (whitePiece.x+(1-ratioPieceOfLineheight)/2)*mLineHeight,
                    (whitePiece.y+(1-ratioPieceOfLineheight)/2)*mLineHeight,null );
        }
        for(int i=0;i<mBlackArray.size();i++){
            Point blackPiece = mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece,
                    (blackPiece.x+(1-ratioPieceOfLineheight)/2)*mLineHeight,
                    (blackPiece.y+(1-ratioPieceOfLineheight)/2)*mLineHeight,null );
        }

    }

    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float LineHeight = mLineHeight;
        for(int i = 0;i<MAX_LINE;i++){
            int startX = (int) (LineHeight/2);
            int endX = (int) (w-LineHeight/2);
            int y = (int) ((0.5+i)*LineHeight);
            canvas.drawLine(startX,y,endX,y,mPaint);
            canvas.drawLine(y,startX,y,endX,mPaint);
        }
    }
}
