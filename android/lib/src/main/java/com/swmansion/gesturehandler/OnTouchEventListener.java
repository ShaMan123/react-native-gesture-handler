package com.swmansion.gesturehandler;

import android.view.DragEvent;
import android.view.MotionEvent;

public interface OnTouchEventListener<T extends GestureHandler> {
  void onTouchEvent(T handler, MotionEvent event);
  void onDragEvent(T handler, DragEvent event);
  void onStateChange(T handler, int newState, int oldState);
}
