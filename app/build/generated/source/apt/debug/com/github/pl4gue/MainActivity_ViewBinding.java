// Generated code from Butter Knife. Do not modify!
package com.github.pl4gue;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.pl4gue.mvp.view.activity.MainActivity;

import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding<T extends MainActivity> implements Unbinder {
  protected T target;

  private View view2131427431;

  @UiThread
  public MainActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.showHomeworkButton, "field 'mCallApiButton' and method 'onCallAPIButtonClick'");
    target.mCallApiButton = Utils.castView(view, R.id.showHomeworkButton, "field 'mCallApiButton'", Button.class);
    view2131427431 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onCallAPIButtonClick();
      }
    });
    target.mOutputTextView = Utils.findRequiredViewAsType(source, R.id.OutputRecyclerView, "field 'mOutputTextView'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mCallApiButton = null;
    target.mOutputTextView = null;

    view2131427431.setOnClickListener(null);
    view2131427431 = null;

    this.target = null;
  }
}
