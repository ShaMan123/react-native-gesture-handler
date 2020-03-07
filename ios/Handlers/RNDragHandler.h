//
//  RNDragHandler.h
//  RNGestureHandler
//
//  Created by ShaMan123 on 07/03/2020.
//

#import "RNGestureHandler.h"
#import "RNPanHandler.h"

@interface RNDragGestureHandler : RNPanGestureHandler
@property (nonatomic, readonly, nullable) UIDragInteraction *dragInteraction API_AVAILABLE(ios(11.0));
@end
