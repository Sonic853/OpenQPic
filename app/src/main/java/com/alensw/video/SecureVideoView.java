package com.alensw.video;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.SurfaceHolder;
import android.widget.VideoView;
import java.io.IOException;
import java.util.Map;

/** Use per-connection pinning only for gallery loopback capabilities. Other URIs use VideoView unchanged. */
public final class SecureVideoView extends VideoView implements SurfaceHolder.Callback {
    private Uri secure;
    private MediaPlayer player;
    private PinnedVideoSource source;
    private boolean prepared, play, surface, resolving;
    private int seek, request;
    private MediaPlayer.OnPreparedListener onPrepared;
    private MediaPlayer.OnCompletionListener onCompletion;
    private MediaPlayer.OnErrorListener onError;
    public SecureVideoView(Context c) { super(c); getHolder().addCallback(this); }
    @Override public void setVideoURI(Uri uri) { setVideoURI(uri,null); }
    @Override public void setVideoURI(Uri uri,Map<String,String> headers) {
        final int ticket=++request;
        releaseSecure();
        resolving=false; secure=VideoProxyService.ownsPlayback(uri)?uri:null;
        if(secure==null) { super.setVideoURI(uri,headers); return; }
        super.stopPlayback(); seek=0; play=false;
        if("https".equals(uri.getScheme())) { open(); return; }
        // A system chooser may hand this player the external HTTP URL. Creating
        // the separate TLS identity/listener must never block the Activity thread.
        resolving=true;
        Thread t=new Thread(()->{
            Uri resolved=null;
            try { resolved=VideoProxyService.forInternalPlayer(uri); } catch(IOException ignored) {}
            final Uri target=resolved;
            post(()->{ if(request!=ticket) return; resolving=false; if(target==null) failed(); else { secure=target; open(); } });
        },"QP video TLS handoff");
        t.setDaemon(true); t.start();
    }
    private void open() {
        if(!surface || resolving || secure==null || player!=null) return;
        try {
            source=new PinnedVideoSource(secure);
            player=new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDisplay(getHolder()); player.setDataSource(source);
            player.setOnPreparedListener(p->{
                if(p!=player) return;
                prepared=true;
                int w=p.getVideoWidth(),h=p.getVideoHeight();
                if(w>0 && h>0) getHolder().setFixedSize(w,h);
                if(onPrepared!=null) onPrepared.onPrepared(p);
                if(seek>0) { p.seekTo(seek); seek=0; }
                if(play) p.start();
            });
            player.setOnCompletionListener(p->{ if(p==player) { play=false; if(onCompletion!=null) onCompletion.onCompletion(p); } });
            player.setOnErrorListener((p,w,e)->{ if(p!=player) return true; play=false; prepared=false;
                return onError!=null && onError.onError(p,w,e); });
            player.prepareAsync();
        } catch(IOException | RuntimeException e) { failed(); }
    }
    private void failed() {
        MediaPlayer p=player;
        if(onError!=null) onError.onError(p,MediaPlayer.MEDIA_ERROR_UNKNOWN,0);
        releaseSecure();
    }
    private void releaseSecure() {
        if(source!=null) { try { source.close(); } catch(IOException ignored) {} source=null; }
        if(player!=null) { player.release(); player=null; }
        prepared=false;
    }
    private void save() { if(prepared) seek=player.getCurrentPosition(); releaseSecure(); }
    @Override public void surfaceCreated(SurfaceHolder holder) { surface=true; open(); }
    @Override public void surfaceChanged(SurfaceHolder holder,int format,int w,int h) { }
    @Override public void surfaceDestroyed(SurfaceHolder holder) { surface=false; if(secure!=null) save(); }
    @Override public void start() { if(secure==null) super.start(); else { play=true; open(); if(prepared) player.start(); } }
    @Override public void pause() { if(secure==null) super.pause(); else { play=false; if(prepared) player.pause(); } }
    @Override public void seekTo(int ms) { if(secure==null) super.seekTo(ms); else if(prepared) player.seekTo(ms); else seek=ms; }
    @Override public boolean isPlaying() { return secure==null?super.isPlaying():prepared && player.isPlaying(); }
    @Override public int getDuration() { return secure==null?super.getDuration():prepared?player.getDuration():-1; }
    @Override public int getCurrentPosition() { return secure==null?super.getCurrentPosition():prepared?player.getCurrentPosition():seek; }
    @Override public int getBufferPercentage() { return secure==null?super.getBufferPercentage():prepared?100:0; }
    @Override public void suspend() { if(secure==null) super.suspend(); else save(); }
    @Override public void resume() { if(secure==null) super.resume(); else open(); }
    @Override public void stopPlayback() {
        ++request; resolving=false;
        if(secure==null) super.stopPlayback(); else { releaseSecure(); secure=null; play=false; seek=0; }
    }
    @Override public void setOnPreparedListener(MediaPlayer.OnPreparedListener l) { onPrepared=l; super.setOnPreparedListener(l); }
    @Override public void setOnCompletionListener(MediaPlayer.OnCompletionListener l) { onCompletion=l; super.setOnCompletionListener(l); }
    @Override public void setOnErrorListener(MediaPlayer.OnErrorListener l) { onError=l; super.setOnErrorListener(l); }
}
