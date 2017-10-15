// Generated code from Butter Knife. Do not modify!
package com.github.pl4gue.mvp.view.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.github.pl4gue.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class GSheetsActivity_ViewBinding<T extends GSheetsActivity> implements Unbinder {
  protected T target;

  @UiThread
  public GSheetsActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.mHomeworkListRecyclerView = Utils.findRequiredViewAsType(source, R.id.homeworkList, "field 'mHomeworkListRecyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mHomeworkListRecyclerView = null;

    this.target = null;
  }
}
