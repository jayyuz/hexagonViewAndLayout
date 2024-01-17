package com.room.layoutmanagerdemo.ui

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.Animation.AnimationListener
import androidx.recyclerview.widget.RecyclerView

class CustomItemAnimator : RecyclerView.ItemAnimator() {
    private var animator: ObjectAnimator? = null

    /**
     * Called by the RecyclerView when a ViewHolder has disappeared from the layout.
     *
     *
     * This means that the View was a child of the LayoutManager when layout started but has
     * been removed by the LayoutManager. It might have been removed from the adapter or simply
     * become invisible due to other factors. You can distinguish these two cases by checking
     * the change flags that were passed to
     * [.recordPreLayoutInformation].
     *
     *
     * Note that when a ViewHolder both changes and disappears in the same layout pass, the
     * animation callback method which will be called by the RecyclerView depends on the
     * ItemAnimator's decision whether to re-use the same ViewHolder or not, and also the
     * LayoutManager's decision whether to layout the changed version of a disappearing
     * ViewHolder or not. RecyclerView will call
     * [ animateChange][.animateChange] instead of `animateDisappearance` if and only if the ItemAnimator
     * returns `false` from
     * [canReuseUpdatedViewHolder][.canReuseUpdatedViewHolder] and the
     * LayoutManager lays out a new disappearing view that holds the updated information.
     * Built-in LayoutManagers try to avoid laying out updated versions of disappearing views.
     *
     *
     * If LayoutManager supports predictive animations, it might provide a target disappear
     * location for the View by laying it out in that location. When that happens,
     * RecyclerView will call [.recordPostLayoutInformation] and the
     * response of that call will be passed to this method as the `postLayoutInfo`.
     *
     *
     * ItemAnimator must call [.dispatchAnimationFinished] when the animation
     * is complete (or instantly call [.dispatchAnimationFinished] if it
     * decides not to animate the view).
     *
     * @param viewHolder    The ViewHolder which should be animated
     * @param preLayoutInfo The information that was returned from
     * [.recordPreLayoutInformation].
     * @param postLayoutInfo The information that was returned from
     * [.recordPostLayoutInformation]. Might be
     * null if the LayoutManager did not layout the item.
     *
     * @return true if a later call to [.runPendingAnimations] is requested,
     * false otherwise.
     */
    override fun animateDisappearance(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo,
        postLayoutInfo: ItemHolderInfo?
    ): Boolean {
        return false
    }

    /**
     * Called by the RecyclerView when a ViewHolder is added to the layout.
     *
     *
     * In detail, this means that the ViewHolder was **not** a child when the layout started
     * but has  been added by the LayoutManager. It might be newly added to the adapter or
     * simply become visible due to other factors.
     *
     *
     * ItemAnimator must call [.dispatchAnimationFinished] when the animation
     * is complete (or instantly call [.dispatchAnimationFinished] if it
     * decides not to animate the view).
     *
     * @param viewHolder     The ViewHolder which should be animated
     * @param preLayoutInfo  The information that was returned from
     * [.recordPreLayoutInformation].
     * Might be null if Item was just added to the adapter or
     * LayoutManager does not support predictive animations or it could
     * not predict that this ViewHolder will become visible.
     * @param postLayoutInfo The information that was returned from [                       ][.recordPreLayoutInformation].
     *
     * @return true if a later call to [.runPendingAnimations] is requested,
     * false otherwise.
     */
    override fun animateAppearance(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo?,
        postLayoutInfo: ItemHolderInfo
    ): Boolean {
        val view = viewHolder.itemView
        view.alpha = 0f
        animator = ObjectAnimator.ofFloat(view, View.ALPHA, 1f)
        animator!!.duration = 600
        animator!!.addListener(object : Animator.AnimatorListener {
            /**
             *
             * Notifies the start of the animation.
             *
             * @param animation The started animation.
             */
            override fun onAnimationStart(animation: Animator) {
                dispatchAnimationStarted(viewHolder)
            }

            /**
             *
             * Notifies the end of the animation. This callback is not invoked
             * for animations with repeat count set to INFINITE.
             *
             * @param animation The animation which reached its end.
             */
            override fun onAnimationEnd(animation: Animator) {
                dispatchAnimationFinished(viewHolder)
            }

            /**
             *
             * Notifies the cancellation of the animation. This callback is not invoked
             * for animations with repeat count set to INFINITE.
             *
             * @param animation The animation which was canceled.
             */
            override fun onAnimationCancel(animation: Animator) {
                dispatchAnimationFinished(viewHolder)
            }

            /**
             *
             * Notifies the repetition of the animation.
             *
             * @param animation The animation which was repeated.
             */
            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        animator!!.start()
        return true
    }

    /**
     * Called by the RecyclerView when a ViewHolder is present in both before and after the
     * layout and RecyclerView has not received a [Adapter.notifyItemChanged] call
     * for it or a [Adapter.notifyDataSetChanged] call.
     *
     *
     * This ViewHolder still represents the same data that it was representing when the layout
     * started but its position / size may be changed by the LayoutManager.
     *
     *
     * If the Item's layout position didn't change, RecyclerView still calls this method because
     * it does not track this information (or does not necessarily know that an animation is
     * not required). Your ItemAnimator should handle this case and if there is nothing to
     * animate, it should call [.dispatchAnimationFinished] and return
     * `false`.
     *
     *
     * ItemAnimator must call [.dispatchAnimationFinished] when the animation
     * is complete (or instantly call [.dispatchAnimationFinished] if it
     * decides not to animate the view).
     *
     * @param viewHolder     The ViewHolder which should be animated
     * @param preLayoutInfo  The information that was returned from
     * [.recordPreLayoutInformation].
     * @param postLayoutInfo The information that was returned from [                       ][.recordPreLayoutInformation].
     *
     * @return true if a later call to [.runPendingAnimations] is requested,
     * false otherwise.
     */
    override fun animatePersistence(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo,
        postLayoutInfo: ItemHolderInfo
    ): Boolean {
        return false
    }

    /**
     * Called by the RecyclerView when an adapter item is present both before and after the
     * layout and RecyclerView has received a [Adapter.notifyItemChanged] call
     * for it. This method may also be called when
     * [Adapter.notifyDataSetChanged] is called and adapter has stable ids so that
     * RecyclerView could still rebind views to the same ViewHolders. If viewType changes when
     * [Adapter.notifyDataSetChanged] is called, this method **will not** be called,
     * instead, [.animateAppearance] will be
     * called for the new ViewHolder and the old one will be recycled.
     *
     *
     * If this method is called due to a [Adapter.notifyDataSetChanged] call, there is
     * a good possibility that item contents didn't really change but it is rebound from the
     * adapter. [DefaultItemAnimator] will skip animating the View if its location on the
     * screen didn't change and your animator should handle this case as well and avoid creating
     * unnecessary animations.
     *
     *
     * When an item is updated, ItemAnimator has a chance to ask RecyclerView to keep the
     * previous presentation of the item as-is and supply a new ViewHolder for the updated
     * presentation (see: [.canReuseUpdatedViewHolder].
     * This is useful if you don't know the contents of the Item and would like
     * to cross-fade the old and the new one ([DefaultItemAnimator] uses this technique).
     *
     *
     * When you are writing a custom item animator for your layout, it might be more performant
     * and elegant to re-use the same ViewHolder and animate the content changes manually.
     *
     *
     * When [Adapter.notifyItemChanged] is called, the Item's view type may change.
     * If the Item's view type has changed or ItemAnimator returned `false` for
     * this ViewHolder when [.canReuseUpdatedViewHolder] was called, the
     * `oldHolder` and `newHolder` will be different ViewHolder instances
     * which represent the same Item. In that case, only the new ViewHolder is visible
     * to the LayoutManager but RecyclerView keeps old ViewHolder attached for animations.
     *
     *
     * ItemAnimator must call [.dispatchAnimationFinished] for each distinct
     * ViewHolder when their animation is complete
     * (or instantly call [.dispatchAnimationFinished] if it decides not to
     * animate the view).
     *
     *
     * If oldHolder and newHolder are the same instance, you should call
     * [.dispatchAnimationFinished] **only once**.
     *
     *
     * Note that when a ViewHolder both changes and disappears in the same layout pass, the
     * animation callback method which will be called by the RecyclerView depends on the
     * ItemAnimator's decision whether to re-use the same ViewHolder or not, and also the
     * LayoutManager's decision whether to layout the changed version of a disappearing
     * ViewHolder or not. RecyclerView will call
     * `animateChange` instead of
     * [ animateDisappearance][.animateDisappearance] if and only if the ItemAnimator returns `false` from
     * [canReuseUpdatedViewHolder][.canReuseUpdatedViewHolder] and the
     * LayoutManager lays out a new disappearing view that holds the updated information.
     * Built-in LayoutManagers try to avoid laying out updated versions of disappearing views.
     *
     * @param oldHolder     The ViewHolder before the layout is started, might be the same
     * instance with newHolder.
     * @param newHolder     The ViewHolder after the layout is finished, might be the same
     * instance with oldHolder.
     * @param preLayoutInfo  The information that was returned from
     * [.recordPreLayoutInformation].
     * @param postLayoutInfo The information that was returned from [                       ][.recordPreLayoutInformation].
     *
     * @return true if a later call to [.runPendingAnimations] is requested,
     * false otherwise.
     */
    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo,
        postLayoutInfo: ItemHolderInfo
    ): Boolean {
        return false
    }

    /**
     * Called when there are pending animations waiting to be started. This state
     * is governed by the return values from
     * [ animateAppearance()][.animateAppearance],
     * [ animateChange()][.animateChange]
     * [ animatePersistence()][.animatePersistence], and
     * [ animateDisappearance()][.animateDisappearance], which inform the RecyclerView that the ItemAnimator wants to be
     * called later to start the associated animations. runPendingAnimations() will be scheduled
     * to be run on the next frame.
     */
    override fun runPendingAnimations() {

    }

    /**
     * Method called when an animation on a view should be ended immediately.
     * This could happen when other events, like scrolling, occur, so that
     * animating views can be quickly put into their proper end locations.
     * Implementations should ensure that any animations running on the item
     * are canceled and affected properties are set to their end values.
     * Also, [.dispatchAnimationFinished] should be called for each finished
     * animation since the animations are effectively done when this method is called.
     *
     * @param item The item for which an animation should be stopped.
     */
    override fun endAnimation(item: RecyclerView.ViewHolder) {

    }

    /**
     * Method called when all item animations should be ended immediately.
     * This could happen when other events, like scrolling, occur, so that
     * animating views can be quickly put into their proper end locations.
     * Implementations should ensure that any animations running on any items
     * are canceled and affected properties are set to their end values.
     * Also, [.dispatchAnimationFinished] should be called for each finished
     * animation since the animations are effectively done when this method is called.
     */
    override fun endAnimations() {

    }

    /**
     * Method which returns whether there are any item animations currently running.
     * This method can be used to determine whether to delay other actions until
     * animations end.
     *
     * @return true if there are any item animations currently running, false otherwise.
     */
    override fun isRunning(): Boolean {
        return animator?.isRunning == true
    }
}