// Generated code from Butter Knife. Do not modify!
package com.usr.usrsimplebleassistent;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class GattDetailActivity$$ViewBinder<T extends com.usr.usrsimplebleassistent.GattDetailActivity> extends com.usr.usrsimplebleassistent.MyBaseActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131230746, "field 'btnOptions' and method 'onOptionsClick'");
    target.btnOptions = finder.castView(view, 2131230746, "field 'btnOptions'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onOptionsClick();
        }
      });
    view = finder.findRequiredView(source, 2131230744, "field 'btnOption' and method 'onOptionClick'");
    target.btnOption = finder.castView(view, 2131230744, "field 'btnOption'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onOptionClick();
        }
      });
    view = finder.findRequiredView(source, 2131230812, "field 'rvMsg'");
    target.rvMsg = finder.castView(view, 2131230812, "field 'rvMsg'");
    view = finder.findRequiredView(source, 2131230901, "field 'tvProperties'");
    target.tvProperties = finder.castView(view, 2131230901, "field 'tvProperties'");
    view = finder.findRequiredView(source, 2131230782, "field 'etWrite'");
    target.etWrite = finder.castView(view, 2131230782, "field 'etWrite'");
    view = finder.findRequiredView(source, 2131230749, "field 'btnSend' and method 'onSendClick'");
    target.btnSend = finder.castView(view, 2131230749, "field 'btnSend'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onSendClick();
        }
      });
    view = finder.findRequiredView(source, 2131230742, "field 'btnGgpl' and method 'onGgpl'");
    target.btnGgpl = finder.castView(view, 2131230742, "field 'btnGgpl'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onGgpl();
        }
      });
    view = finder.findRequiredView(source, 2131230848, "field 'rlWrite'");
    target.rlWrite = finder.castView(view, 2131230848, "field 'rlWrite'");
    view = finder.findRequiredView(source, 2131230845, "field 'rlContent'");
    target.rlContent = finder.castView(view, 2131230845, "field 'rlContent'");
    view = finder.findRequiredView(source, 2131230844, "field 'rlBottom'");
    target.rlBottom = finder.castView(view, 2131230844, "field 'rlBottom'");
    view = finder.findRequiredView(source, 2131230911, "field 'bottomShadow'");
    target.bottomShadow = view;
    view = finder.findRequiredView(source, 2131230915, "field 'topShadow'");
    target.topShadow = view;
    view = finder.findRequiredView(source, 2131230912, "field 'filterView'");
    target.filterView = view;
    view = finder.findRequiredView(source, 2131230748, "field 'btnSelect'");
    target.btnSelect = finder.castView(view, 2131230748, "field 'btnSelect'");
    view = finder.findRequiredView(source, 2131230810, "field 'listview'");
    target.listview = finder.castView(view, 2131230810, "field 'listview'");
    view = finder.findRequiredView(source, 2131230776, "field 'editAddr'");
    target.editAddr = finder.castView(view, 2131230776, "field 'editAddr'");
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.btnOptions = null;
    target.btnOption = null;
    target.rvMsg = null;
    target.tvProperties = null;
    target.etWrite = null;
    target.btnSend = null;
    target.btnGgpl = null;
    target.rlWrite = null;
    target.rlContent = null;
    target.rlBottom = null;
    target.bottomShadow = null;
    target.topShadow = null;
    target.filterView = null;
    target.btnSelect = null;
    target.listview = null;
    target.editAddr = null;
  }
}
