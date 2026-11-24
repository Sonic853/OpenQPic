package android.support.v4.a;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;

/** Pager child layout parameters. */
public class v extends ViewGroup.LayoutParams {

    public boolean a;

    public int b;
    float c;
    boolean d;
    int e;
    int f;

    public v() {
        super(-1, -1);
        this.c = 0.0f;
    }

    public v(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.c = 0.0f;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, p.f());
        this.b = typedArrayObtainStyledAttributes.getInteger(0, 48);
        typedArrayObtainStyledAttributes.recycle();
    }
}
