// Generated code from Butter Knife. Do not modify!
package com.usr.usrsimplebleassistent;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MainActivity$$ViewBinder<T extends com.usr.usrsimplebleassistent.MainActivity> extends com.usr.usrsimplebleassistent.MyBaseActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131558523, "field 'recyclerView'");
    target.recyclerView = finder.castView(view, 2131558523, "field 'recyclerView'");
    view = finder.findRequiredView(source, 2131558522, "field 'collapsingToolbarLayout'");
    target.collapsingToolbarLayout = finder.castView(view, 2131558522, "field 'collapsingToolbarLayout'");
    view = finder.findRequiredView(source, 2131558526, "field 'fabSearch' and method 'searchDevice'");
    target.fabSearch = finder.castView(view, 2131558526, "field 'fabSearch'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.searchDevice();
        }
      });
    view = finder.findRequiredView(source, 2131558525, "field 'revealSearchView'");
    target.revealSearchView = finder.castView(view, 2131558525, "field 'revealSearchView'");
    view = finder.findRequiredView(source, 2131558524, "field 'revealBackgroundView'");
    target.revealBackgroundView = finder.castView(view, 2131558524, "field 'revealBackgroundView'");
    view = finder.findRequiredView(source, 2131558527, "field 'rlSearchInfo'");
    target.rlSearchInfo = finder.castView(view, 2131558527, "field 'rlSearchInfo'");
    view = finder.findRequiredView(source, 2131558529, "field 'tvSearchDeviceCount'");
    target.tvSearchDeviceCount = finder.castView(view, 2131558529, "field 'tvSearchDeviceCount'");
    view = finder.findRequiredView(source, 2131558520, "field 'clContent'");
    target.clContent = finder.castView(view, 2131558520, "field 'clContent'");
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
