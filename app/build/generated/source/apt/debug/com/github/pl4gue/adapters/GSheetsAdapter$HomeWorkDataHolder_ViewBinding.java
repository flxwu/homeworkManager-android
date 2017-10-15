// Generated code from Butter Knife. Do not modify!
package com.github.pl4gue.adapters;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.github.pl4gue.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class GSheetsAdapter$HomeWorkDataHolder_ViewBinding<T extends GSheetsAdapter.HomeWorkDataHolder> implements Unbinder {
  protected T target;

  @UiThread
  public GSheetsAdapter$HomeWorkDataHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.mHomeWork = Utils.findRequiredViewAsType(source, R.id.homework, "field 'mHomeWork'", TextView.class);
    target.mHomeWorkDueDate = Utils.findRequiredViewAsType(source, R.id.homeworkDueDate, "field 'mHomeWorkDueDate'", TextView.class);
    target.mHomeWorkEntryDate = Utils.findRequiredViewAsType(source, R.id.homeworkEntryDate, "field 'mHomeWorkEntryDate'", TextView.class);
    target.mHomeWorkSubject = Utils.findRequiredViewAsType(source, R.id.homeworkSubject, "field 'mHomeWorkSubject'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mHomeWork = null;
    target.mHomeWorkDueDate = null;
    target.mHomeWorkEntryDate = null;
    target.mHomeWorkSubject = null;

    this.target = null;
  }
}
