package com.alensw.transfer;

import android.app.Activity;
import android.text.format.Formatter;
import android.view.*;
import android.widget.*;
import com.alensw.PicFolder.R;
import org.openqpic.localsend.LocalSend;

/** A full page in the existing transfer Activity. Selection belongs to the pending offer. */
final class ReceivePage {
    final View view;
    final TransferSessionService.Offer offer;
    private final Activity activity;
    private final TextView summary;
    private final Button accept;
    ReceivePage(Activity activity,ViewGroup parent,TransferSessionService.Offer offer,Runnable yes,Runnable no) {
        this.activity=activity; this.offer=offer;
        view=activity.getLayoutInflater().inflate(R.layout.ls_receive,parent,false);
        ((TextView)view.findViewById(R.id.sender)).setText(activity.getString(R.string.ls_offer_from,offer.peer.alias)+" · "+offer.peer.ip);
        ((TextView)view.findViewById(R.id.ls_destination)).setText(activity.getString(R.string.ls_folder,offer.folder));
        TextView skipped=view.findViewById(R.id.ls_skipped);
        if(offer.skipped==0) skipped.setVisibility(View.GONE);
        else skipped.setText(activity.getString(R.string.ls_skipped,offer.skipped));
        summary=view.findViewById(R.id.ls_selected); accept=view.findViewById(R.id.ls_accept);
        accept.setOnClickListener(v->yes.run()); view.findViewById(R.id.ls_decline).setOnClickListener(v->no.run());
        String[] names=new String[offer.files.size()];
        for(int i=0;i<names.length;i++) {
            LocalSend.Item item=offer.files.get(i);
            names[i]=TransferFiles.basename(item.name)+"\n"+item.type+" · "+Formatter.formatFileSize(activity,item.size);
        }
        ListView files=view.findViewById(R.id.list);
        files.setAdapter(new ArrayAdapter<>(activity,android.R.layout.simple_list_item_multiple_choice,names));
        for(int i=0;i<names.length;i++) files.setItemChecked(i,offer.selected.contains(offer.files.get(i).id));
        files.setOnItemClickListener((p,v,i,n)->{
            String key=offer.files.get(i).id;
            if(files.isItemChecked(i)) offer.selected.add(key); else offer.selected.remove(key);
            totals();
        });
        totals();
    }
    private void totals() {
        long bytes=0; for(LocalSend.Item f:offer.files) if(offer.selected.contains(f.id)) bytes+=f.size;
        accept.setEnabled(!offer.selected.isEmpty());
        summary.setText(activity.getString(R.string.ls_selected,offer.selected.size(),offer.files.size(),Formatter.formatFileSize(activity,bytes)));
    }
}
