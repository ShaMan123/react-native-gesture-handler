
//
//  Created by ShaMan123 on 07/03/2020.
//
#import <React/RCTLog.h>
#import <Foundation/Foundation.h>
#import "RNDragHandler.h"
#import "RNPanHandler.h"
#import <UIKit/UIGestureRecognizerSubclass.h>

API_AVAILABLE(ios(11.0)) @interface RNDragDelegate : RNBetterPanGestureRecognizer <UIDragInteractionDelegate>
//-(instancetype)initWithView:(UIView *)view;
//@property (nonatomic, weak) UIView *view;
@end

@implementation RNDragDelegate
/*
-(instancetype)initWithView:(UIView *)view
{
    self = [super init];
    _view = view;
    return self;
}
*/
-(NSArray<UIDragItem *> *)dragInteraction:(UIDragInteraction *)interaction itemsForBeginningSession:(id<UIDragSession>)session
{
    RCTLog(@"drag dtaa");
    NSItemProvider *item = [[NSItemProvider alloc] initWithObject:@"Hello World"];
    UIDragItem *dragItem = [[UIDragItem alloc] initWithItemProvider:item];
    NSArray *dragItems = [[NSArray alloc] initWithObjects:dragItem, nil];
    return dragItems;
}

-(UITargetedDragPreview *)dragInteraction:(UIDragInteraction *)interaction previewForLiftingItem:(UIDragItem *)item session:(id<UIDragSession>)session
{
    RCTLog(@"hello everyone %@",[session items]);
    UITargetedDragPreview *preview = [[UITargetedDragPreview alloc] initWithView:self.view];
    return preview;
}

-(UITargetedDragPreview *)dragInteraction:(UIDragInteraction *)interaction previewForCancellingItem:(UIDragItem *)item withDefault:(UITargetedDragPreview *)defaultPreview
{
    UITargetedDragPreview *preview = [[UITargetedDragPreview alloc] initWithView:self.view];
    return preview;
}

-(BOOL)dragInteraction:(UIDragInteraction *)interaction sessionIsRestrictedToDraggingApplication:(id<UIDragSession>)session
{
    return NO;
}

-(void)dragInteraction:(UIDragInteraction *)interaction sessionWillBegin:(id<UIDragSession>)session
{
    RCTLog(@"hello everyone");
}

-(void)dragInteraction:(UIDragInteraction *)interaction session:(id<UIDragSession>)session didEndWithOperation:(UIDropOperation)operation
{
    //operation == UIDropOperationCopy
}

-(NSArray<UIDragItem *> *)dragInteraction:(UIDragInteraction *)interaction itemsForAddingToSession:(id<UIDragSession>)session withTouchAtPoint:(CGPoint)point
{
    NSItemProvider *item = [[NSItemProvider alloc] initWithObject:@"Hello World"];
    UIDragItem *dragItem = [[UIDragItem alloc] initWithItemProvider:item];
    NSArray *dragItems = [[NSArray alloc] initWithObjects:dragItem, nil];
    return dragItems;
}
/*
-(void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event
{
    self.state = UIGestureRecognizerStatePossible;

}

- (void)touchesMoved:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event
{
    
}
*/
@end


@implementation RNDragGestureHandler
- (instancetype)initWithTag:(NSNumber *)tag
{
    if ((self = [super initWithTag:tag])) {
#if !TARGET_OS_TV
        if (@available(iOS 11.0, *)) {
            RNDragDelegate *delegate = [[RNDragDelegate alloc] initWithGestureHandler:self];
            _recognizer = delegate;
            _dragInteraction = [[UIDragInteraction alloc] initWithDelegate:delegate];
        } else {
            // Fallback on earlier versions
        }
#endif
    }
    return self;
}

#if !TARGET_OS_TV
- (RNGestureHandlerEventExtraData *)eventExtraData:(UIRotationGestureRecognizer *)recognizer
{
    return [super eventExtraData:recognizer];
}
#endif

- (void)bindToView:(UIView *)view
{
    
    if (@available(iOS 11.0, *)) {
        [view setUserInteractionEnabled:YES];
        [view addInteraction:_dragInteraction];
    } else {
        // Fallback on earlier versions
    }
    view.userInteractionEnabled = YES;
    self.recognizer.delegate = self;

}

- (void)unbindFromView
{
    if (@available(iOS 11.0, *)) {
        //[_dragInteraction.view removeInteraction:_dragInteraction];
    } else {
        // Fallback on earlier versions
    }
    [super unbindFromView];
}

-(BOOL)shouldCancelWhenOutside
{
    return NO;
}

@end
