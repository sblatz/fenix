package org.mozilla.fenix.components.toolbar

import mozilla.components.lib.state.Action
import mozilla.components.lib.state.State
import mozilla.components.lib.state.Store

class BrowserStore(initialState: BrowserState) :
    Store<BrowserState, BrowserAction>(initialState, ::browserStateReducer)

/**
 * The state for the Browser Screen
 * @property quickActionSheetState: state of the quick action sheet
 */
data class BrowserState(
    val quickActionSheetState: QuickActionSheetState
) : State

/**
 * The state for the QuickActionSheet
 * @property readable Whether or not the current session can display a reader view
 * @property bookmarked Whether or not the current session is already bookmarked
 * @property readerActive Whether or not the current session is in reader mode
 * @property bounceNeeded Whether or not the quick action sheet should bounce
 */
data class QuickActionSheetState(
    val readable: Boolean,
    val bookmarked: Boolean,
    val readerActive: Boolean,
    val bounceNeeded: Boolean,
    val isAppLink: Boolean
) : State

sealed class BrowserAction : Action

/**
 * Actions to dispatch through the [QuickActionSheetStore] to modify [QuickActionSheetState] through the reducer.
 */
sealed class QuickActionSheetAction : BrowserAction() {
    data class BookmarkedStateChange(val bookmarked: Boolean) : QuickActionSheetAction()
    data class ReadableStateChange(val readable: Boolean) : QuickActionSheetAction()
    data class ReaderActiveStateChange(val active: Boolean) : QuickActionSheetAction()
    data class AppLinkStateChange(val isAppLink: Boolean) : QuickActionSheetAction()
    object BounceNeededChange : QuickActionSheetAction()
}

/**
 * Reducers for [BrowserStore].
 *
 * A top level reducer that receives the current [BrowserState] and an [Action] and then delegates to the proper child
 *
 */
fun browserStateReducer(
    state: BrowserState,
    action: BrowserAction
): BrowserState {
    return when (action) {
        is QuickActionSheetAction -> {
            QuickActionSheetStateReducer.reduce(state, action)
        }
    }
}

/**
 * Reduces [QuickActionSheetAction]s to update [BrowserState].
 */
internal object QuickActionSheetStateReducer {
    fun reduce(state: BrowserState, action: QuickActionSheetAction): BrowserState {
        return when (action) {
            is QuickActionSheetAction.BookmarkedStateChange ->
                state.copy(quickActionSheetState = state.quickActionSheetState.copy(bookmarked = action.bookmarked))
            is QuickActionSheetAction.ReadableStateChange ->
                state.copy(quickActionSheetState = state.quickActionSheetState.copy(readable = action.readable))
            is QuickActionSheetAction.ReaderActiveStateChange ->
                state.copy(quickActionSheetState = state.quickActionSheetState.copy(readerActive = action.active))
            is QuickActionSheetAction.BounceNeededChange ->
                state.copy(quickActionSheetState = state.quickActionSheetState.copy(bounceNeeded = true))
            is QuickActionSheetAction.AppLinkStateChange -> {
                state.copy(quickActionSheetState = state.quickActionSheetState.copy(isAppLink = action.isAppLink))
            }
        }
    }
}
