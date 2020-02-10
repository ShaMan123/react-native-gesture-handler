package com.swmansion.gesturehandler;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Canvas;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DragGestureHandler<T> extends DragDropGestureHandler<T> {

    private ArrayList<DropGestureHandler<T>> mDropHandlers = new ArrayList<>();
    private @Nullable DropGestureHandler<T> mDropHandler;

    @Override
    public int getDragTarget() {
        return getView() != null ? getView().getId() : View.NO_ID;
    }

    @Override
    public int getDropTarget() {
        return mDropHandler != null && mDropHandler.getView() != null ? mDropHandler.getView().getId() : View.NO_ID;
    }

    ClipData createClipData() {
        Intent intent = new Intent(Intent.ACTION_RUN);
        intent.putExtra(KEY_DRAG_TARGET, getView().getId());
        intent.putIntegerArrayListExtra(KEY_TYPE, mDTypes);
        intent.putExtra(KEY_SOURCE_APP, getView().getContext().getPackageName());
        if (mDataResolver != null) {
            intent.putExtra(KEY_DATA, mDataResolver.toString());
        }
        StringBuilder str = new StringBuilder(DRAG_MIME_TYPE + ":");
        for (int t: mDTypes) {
            str.append(t);
            str.append(",");
        }

        return new ClipData(
                DRAG_EVENT_NAME,
                new String[] {
                        ClipDescription.MIMETYPE_TEXT_INTENT,
                        str.toString()
                },
                new ClipData.Item(intent)
        );
    }

    private void startDragging() {
        mOrchestrator.mIsDragging = true;
        ClipData data = createClipData();
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(getView());
        //View.DragShadowBuilder shadowBuilder = new SyncedDragShadowBuilder(getView());
        int flags = DragGestureUtils.getFlags();
        getView().startDrag(data, shadowBuilder, null, flags);
    }

    void addDropHandler(DropGestureHandler<T> handler) {
        if (!mDropHandlers.contains(handler)) {
            mDropHandlers.add(handler);
        }
    }

    /**
     * once handling begins {@link android.view.View.OnDragListener} handles gesture decision making
     */
    @Override
    protected void onHandle(MotionEvent event) {
        if (getState() != STATE_BEGAN && getState() != STATE_ACTIVE) {
            super.onHandle(event);
            if (getState() == STATE_BEGAN) {
                startDragging();
            }
        }
    }

    @Override
    protected void onHandle(DragEvent event) {
        super.onHandle(event);
        if (event.getAction() != DragEvent.ACTION_DRAG_STARTED || event.getAction() != DragEvent.ACTION_DRAG_ENDED) {
            for (DropGestureHandler<T> handler: mDropHandlers) {
                if (handler.isActive()) {
                    mDropHandler = handler;
                    break;
                } else if (mDropHandler != null && handler == mDropHandler) {
                    mDropHandler = null;
                }
            }
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        mDropHandlers.clear();
    }

    private static class SyncedDragShadowBuilder extends View.DragShadowBuilder {
        private View mView;
        SyncedDragShadowBuilder(View view) {
            mView = view;
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            mView.draw(canvas);
        }
    }
}
