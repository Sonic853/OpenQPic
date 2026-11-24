package android.support.a.a;

import android.R;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/** Action-bar reflection and legacy home-image lookup. */
public class k {
    private k() {}

    private static final int[] a = {R.attr.homeAsUpIndicator};

    public static l a(l lVar, Activity activity, int i) {
        if (lVar == null) {
            lVar = new l(activity);
        }
        if (lVar.a != null) {
            try {
                ActionBar actionBar = activity.getActionBar();
                lVar.b.invoke(actionBar, Integer.valueOf(i));
                if (Build.VERSION.SDK_INT <= 19) {
                    actionBar.setSubtitle(actionBar.getSubtitle());
                }
            } catch (Exception e) {
                Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set content description via JB-MR2 API", e);
            }
        }
        return lVar;
    }

    public static l a(l lVar, Activity activity, Drawable drawable, int i) {
        if (lVar == null) {
            lVar = new l(activity);
        }
        if (lVar.a != null) {
            try {
                ActionBar actionBar = activity.getActionBar();
                lVar.a.invoke(actionBar, drawable);
                lVar.b.invoke(actionBar, Integer.valueOf(i));
            } catch (Exception e) {
                Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set home-as-up indicator via JB-MR2 API", e);
            }
        } else if (lVar.c != null) {
            lVar.c.setImageDrawable(drawable);
        } else {
            Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set home-as-up indicator");
        }
        return lVar;
    }

    public static ImageView a(Activity activity, View view) {
        if (view == null) {
            view = activity.findViewById(R.id.home);
        }
        if (view != null) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup.getChildCount() == 2) {
                View childAt = viewGroup.getChildAt(0);
                View childAt2 = viewGroup.getChildAt(1);
                if (childAt.getId() != 16908332) {
                    childAt2 = childAt;
                }
                if (childAt2 instanceof ImageView) {
                    return (ImageView) childAt2;
                }
            }
        }
        int identifier = activity.getResources().getIdentifier("up", "id", "android");
        View viewFindViewById = identifier != 0 ? activity.findViewById(identifier) : null;
        if (viewFindViewById instanceof ImageView) {
            return (ImageView) viewFindViewById;
        }
        return null;
    }
}
