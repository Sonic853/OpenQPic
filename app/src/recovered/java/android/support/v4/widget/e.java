package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;

/** Drawer child layout parameters, including the slide offset and open/peeking state. */
public class e extends ViewGroup.MarginLayoutParams {

    public int a;

    float b;
    boolean c;
    boolean d;
    int e;

    public e(int i, int i2) {
        super(i, i2);
        this.a = 0;
        this.e = 0;
    }

    public e(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.a = 0;
        this.e = 0;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, DrawerLayout.c());
        this.a = typedArrayObtainStyledAttributes.getInt(0, 0);
        typedArrayObtainStyledAttributes.recycle();
    }

    public e(e eVar) {
        super((ViewGroup.MarginLayoutParams) eVar);
        this.a = 0;
        this.e = 0;
        this.a = eVar.a;
    }

    public e(ViewGroup.LayoutParams layoutParams) {
        super(layoutParams);
        this.a = 0;
        this.e = 0;
    }

    public e(ViewGroup.MarginLayoutParams marginLayoutParams) {
        super(marginLayoutParams);
        this.a = 0;
        this.e = 0;
    }
}
