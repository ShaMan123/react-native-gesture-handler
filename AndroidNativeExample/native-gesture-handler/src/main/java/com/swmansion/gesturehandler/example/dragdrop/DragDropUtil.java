package com.swmansion.gesturehandler.example.dragdrop;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.swmansion.gesturehandler.DragDropGestureHandler;
import com.swmansion.gesturehandler.DragGestureUtils;
import com.swmansion.gesturehandler.GestureHandler;
import com.swmansion.gesturehandler.OnTouchEventListener;

import java.util.HashMap;

public class DragDropUtil {
    public static boolean isFinished(int state) {
        return state == GestureHandler.STATE_CANCELLED || state == GestureHandler.STATE_FAILED
                || state == GestureHandler.STATE_END;
    }

    public static class DragDropEventListener<O extends Object,T extends DragDropGestureHandler<O, T>> implements OnTouchEventListener<T> {

        private HashMap<Integer, Integer> actionToColor = new HashMap<>();
        private HashMap<Object, Integer> stateToColor = new HashMap<>();
        private Integer bgc = null;
        private Integer currentBgc = null;

        public DragDropEventListener<O, T> setColorForAction(int action, int color) {
            actionToColor.put(action, color);
            return this;
        }

        public DragDropEventListener<O, T> setColorForState(int state, int color) {
            stateToColor.put(state, color);
            return this;
        }

        public DragDropEventListener<O, T> setColorForState(int state, int oldState, int color) {
            stateToColor.put(state + "," + oldState, color);
            return this;
        }

        private void setBackgroundColor(View view, int color) {
            if (bgc == null) {
                Drawable background = view.getBackground();
                if (background instanceof ColorDrawable) {
                    bgc = ((ColorDrawable) background).getColor();
                }
            }
            view.setBackgroundColor(color);
            view.invalidate();
            currentBgc = color;
        }

        @Override
        public void onTouchEvent(T handler, MotionEvent event) {

        }

        @Override
        public void onDragEvent(T handler, DragEvent event) {
            String data = handler.getData() != null ? marshall((String[]) handler.getData()) : "";
            Log.d("Drag", "action " + event.getAction() + ", " + handler + ", " + data);
            int action = event.getAction();
            if (actionToColor.containsKey(action)) {
                setBackgroundColor(handler.getView(), actionToColor.get(action));
            }
        }

        @Override
        public void onStateChange(T handler, int newState, int oldState) {
            Log.d("Drag", "state " + GestureHandler.stateToString(newState) + " " + handler);
            Integer color = stateToColor.containsKey(newState + ',' + oldState) ?
                    stateToColor.get(newState + ',' + oldState) :
                    stateToColor.containsKey(newState) ?
                            stateToColor.get(newState) :
                            null;
            if (color != null) {
                setBackgroundColor(handler.getView(), color);
            }
        }
    }

    public static String marshall(@NonNull String[] data) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            builder.append(data[i]);
            builder.append(",");
        }
        return builder.toString();
    }

    public static class DataResolverStringImpl implements DragGestureUtils.DataResolver<String[]> {

        private final Activity mActivity;

        public DataResolverStringImpl(Activity activity) {
            super();
            mActivity = activity;
        }

        @Override
        public String[] parse(String source) {
            return source.split(",");
        }

        @Override
        public String[] data() {
            return new String[]{"a","b","c"};
        }

        @NonNull
        @Override
        public String stringify() {
            return marshall(data());
        }

        @Override
        public Activity getActivity() {
            return mActivity;
        }
    }

}
