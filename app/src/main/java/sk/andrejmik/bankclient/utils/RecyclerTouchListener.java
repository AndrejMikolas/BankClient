package sk.andrejmik.bankclient.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener
{
    private Context mContext;
    private RecyclerView mRecyclerView;
    private ClickListener mClickListener;
    private GestureDetector mGestureDetector;
    
    public RecyclerTouchListener(Context context, RecyclerView recyclerView, ClickListener clickListener)
    {
        mContext = context;
        mRecyclerView = recyclerView;
        mClickListener = clickListener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                return true;
            }
        });
    }
    
    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e)
    {
        View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
        if (child != null && mGestureDetector.onTouchEvent(e))
        {
            mClickListener.onClick(child, mRecyclerView.getChildAdapterPosition(child));
        }
        return false;
    }
    
    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e)
    {
    
    }
    
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
    {
    
    }
    
    public interface ClickListener
    {
        void onClick(View view, int position);
    }
}
