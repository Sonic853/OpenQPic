package android.support.v4.widget;

import android.os.Parcel;
import android.os.Parcelable.Creator;

/** Parcel creator for the drawer’s saved state. */
final class f implements Creator {
    public DrawerLayout.SavedState a(Parcel var1) {
        return new DrawerLayout.SavedState(var1);
    }

    public DrawerLayout.SavedState[] a(int var1) {
        return new DrawerLayout.SavedState[var1];
    }

    @Override
    public Object createFromParcel(Parcel var1) {
        return this.a(var1);
    }

    @Override
    public Object[] newArray(int var1) {
        return this.a(var1);
    }
}
