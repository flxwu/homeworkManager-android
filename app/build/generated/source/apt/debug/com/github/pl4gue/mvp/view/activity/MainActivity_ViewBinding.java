// Generated code from Butter Knife. Do not modify!
package com.github.pl4gue.mvp.view.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.github.pl4gue.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding<T extends MainActivity> implements Unbinder {
  protected T target;

  private View view2131427435;

  private View view2131427436;

  @UiThread
  public MainActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.showHomeworkButton, "method 'onShowHomework'");
    view2131427435 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onShowHomework();
      }
    });
    view = Utils.findRequiredView(source, R.id.addHomeworkButton, "method 'onAddHomework'");
    view2131427436 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onAddHomework();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    if (this.target == null) throw new IllegalStateException("Bindings already cleared.");

    view2131427435.setOnClickListener(null);
    view2131427435 = null;
    view2131427436.setOnClickListener(null);
    view2131427436 = null;

    this.target = null;
  }
}
