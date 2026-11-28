package com.alensw.ui.activity;

import com.alensw.ui.drawable.AnimatedTrashIcon;

final class AnimateDeleteIcon implements Runnable {
   final AnimatedTrashIcon a;

   AnimateDeleteIcon(AnimatedTrashIcon var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.a();
   }
}
