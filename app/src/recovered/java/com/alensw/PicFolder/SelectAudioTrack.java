package com.alensw.PicFolder;

import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import com.alensw.utils.GalleryUtils;
class SelectAudioTrack implements OnMenuItemClickListener {
   final int a;
   final AudioTrackMenu b;

   SelectAudioTrack(AudioTrackMenu var1, int var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public boolean onMenuItemClick(MenuItem var1) {
      if (this.b.a.l != null) {
         try {
            this.b.a.l.selectTrack(this.a);
         } catch (Throwable var2) {
            GalleryUtils.a(this.b.a, var2);
         }
      }

      return true;
   }
}
