package com.swmansion.gesturehandler.multiwindowexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.swmansion.gesturehandler.DropGestureHandler;
import com.swmansion.gesturehandler.GestureHandler;
import com.swmansion.gesturehandler.GestureHandlerInteractionManager;
import com.swmansion.gesturehandler.GestureHandlerOrchestrator;
import com.swmansion.gesturehandler.GestureHandlerRegistryImpl;
import com.swmansion.gesturehandler.PointerEventsConfig;
import com.swmansion.gesturehandler.ViewConfigurationHelper;
import com.swmansion.gesturehandler.example.dragdrop.DragDropUtil;
import com.swmansion.gesturehandler.example.dragdrop.GHRootView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GHRootView wrapper;
    private ScrollView scrollView;
    private Button button;
    private SeekBar seekBar;
    private View block;
    private View largeBlock;
    private View blockChild;
    private View blockChild2;
    private Switch switchView;
    private TextView textView;
    private boolean enableShadow = true;

    private final GestureHandlerRegistryImpl mRegistry = new GestureHandlerRegistryImpl() {
        @Override
        public <T extends GestureHandler> T registerHandlerForView(View view, T handler) {
            handler.setTag((int) (-view.getId() * Math.random()));
            return super.registerHandlerForView(view, handler);
        }
    };
    private GestureHandlerOrchestrator mOrchestrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wrapper = findViewById(R.id.wrapper);
        blockChild = findViewById(R.id.block_child);
        largeBlock = findViewById(R.id.large_block);
        final View[] shadows = new View[]{button, switchView, largeBlock, null};

        ArrayList<Integer> dragTypes = new ArrayList<>();
        dragTypes.add(0);
        dragTypes.add(1);

        mOrchestrator = new GestureHandlerOrchestrator(
                wrapper, mRegistry, new ViewConfigurationHelper() {
            @Override
            public PointerEventsConfig getPointerEventsConfigForView(View view) {
                return PointerEventsConfig.AUTO;
            }

            @Override
            public View getChildInDrawingOrderAtIndex(ViewGroup parent, int index) {
                return parent.getChildAt(index);
            }

            @Override
            public boolean isViewClippingChildren(ViewGroup view) {
                return false;
            }

        });
        mOrchestrator.setMinimumAlphaForTraversal(0.1f);
        wrapper.init(mOrchestrator, mRegistry);

        GestureHandlerRegistryImpl registry = mRegistry;
        GestureHandlerInteractionManager interactionManager = new GestureHandlerInteractionManager();

        DropGestureHandler dropHandler = new DropGestureHandler<String[]>(this)
                .setDataResolver(new DragDropUtil.DataResolverStringImpl(this))
                .setOnTouchEventListener(new DragDropUtil.DragDropEventListener<String[], DropGestureHandler<String[]>>());

        registry.registerHandlerForView(blockChild, dropHandler);
    }
}
