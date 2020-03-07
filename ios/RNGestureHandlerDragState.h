#import <Foundation/Foundation.h>

typedef NS_ENUM(NSInteger, RNGestureHandlerDragState) {
    RNGestureHandlerDragStateBegan = 1,
    RNGestureHandlerDragStateActive,
    RNGestureHandlerDragStateDrop,
    RNGestureHandlerDragStateEnd,
    RNGestureHandlerDragStateEnter,
    RNGestureHandlerDragStateExit
};

typedef NS_ENUM(NSInteger, RNGestureHandlerDragMode) {
    RNGestureHandlerDragModeMove = 0,
    RNGestureHandlerDragModeCopy
};
