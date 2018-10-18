// Generated code from Butter Knife. Do not modify!
package com.usr.usrsimplebleassistent;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MainActivity$$ViewBinder<T extends com.usr.usrsimplebleassistent.MainActivity> extends com.usr.usrsimplebleassistent.MyBaseActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131230840, "field 'recyclerView'");
    target.recyclerView = finder.castView(view, 2131230840, "field 'recyclerView'");
    view = finder.findRequiredView(source, 2131230764, "field 'collapsingToolbarLayout'");
    target.collapsingToolbarLayout = finder.castView(view, 2131230764, "field 'collapsingToolbarLayout'");
    view = finder.findRequiredView(source, 2131230786, "field 'fabSearch' and method 'searchDevice'");
    target.fabSearch = finder.castView(view, 2131230786, "field 'fabSearch'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.searchDevice();
        }
      });
    view = finder.findRequiredView(source, 2131230839, "field 'revealSearchView'");
    target.revealSearchView = finder.castView(view, 2131230839, "field 'revealSearchView'");
    view = finder.findRequiredView(source, 2131230842, "field 'revealBackgroundView'");
    target.revealBackgroundView = finder.castView(view, 2131230842, "field 'revealBackgroundView'");
    view = finder.findRequiredView(source, 2131230846, "field 'rlSearchInfo'");
    target.rlSearchInfo = finder.castView(view, 2131230846, "field 'rlSearchInfo'");
    view = finder.findRequiredView(source, 2131230902, "field 'tvSearchDeviceCount'");
    target.tvSearchDeviceCount = finder.castView(view, 2131230902, "field 'tvSearchDeviceCount'");
    view = finder.findRequiredView(source, 2131230761, "field 'clContent'");
    target.clContent = finder.castView(view, 2131230761, "field 'clContent'");
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.recyclerView = null;
    target.collapsingToolbarLayout = null;
    target.fabSearch = null;
    target.revealSearchView = null;
    target.revealBackgroundView = null;
    target.rlSearchInfo = null;
    target.tvSearchDeviceCount = null;
    target.clContent = null;
  }
}
