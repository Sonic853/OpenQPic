.class Lcom/alensw/updater/UpdateView$1;
.super Ljava/lang/Object;
.source "UpdateView.java"

# interfaces
.implements Landroid/preference/Preference$OnPreferenceClickListener;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/alensw/updater/UpdateView;->onCreate(Landroid/os/Bundle;)V
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x0
    name = null
.end annotation


# instance fields
.field final synthetic this$0:Lcom/alensw/updater/UpdateView;


# direct methods
.method constructor <init>(Lcom/alensw/updater/UpdateView;)V
    .locals 0

    iput-object p1, p0, Lcom/alensw/updater/UpdateView$1;->this$0:Lcom/alensw/updater/UpdateView;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onPreferenceClick(Landroid/preference/Preference;)Z
    .locals 2
    iget-object v0, p0, Lcom/alensw/updater/UpdateView$1;->this$0:Lcom/alensw/updater/UpdateView;
    const/4 v1, 0x1
    invoke-static {v0, v1}, Lcom/alensw/updater/GitHubUpdates;->check(Landroid/app/Activity;Z)V
    return v1
.end method
