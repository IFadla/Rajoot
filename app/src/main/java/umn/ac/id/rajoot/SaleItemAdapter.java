package umn.ac.id.rajoot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

//Manage view every product on home screen
public class SaleItemAdapter extends RecyclerView.Adapter<SaleItemAdapter.ItemViewHolder> {
    private ArrayList<ItemModel> dataList;
    public SaleItemAdapter(ArrayList<ItemModel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public SaleItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_sell, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleItemAdapter.ItemViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CraftDetailsActivity.class);
                intent.putExtra("title", dataList.get(position).getTitle());
                intent.putExtra("price", dataList.get(position).getPrice());
                intent.putExtra("phone", dataList.get(position).getPhone());
                intent.putExtra("path", dataList.get(position).getPath());
                intent.putExtra("condition", dataList.get(position).getCondition());
                intent.putExtra("description", dataList.get(position).getDescription());
                v.getContext().startActivity(intent);
            }
        });
        holder.title.setText(dataList.get(position).getTitle());
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        formatRupiah.setMinimumFractionDigits(0);
        holder.price.setText(formatRupiah.format(Double.parseDouble(dataList.get(position).getPrice())));
        holder.condition.setText(dataList.get(position).getCondition());
        new DownLoadImageTask(holder.imageView).execute(dataList.get(position).getPath());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView title, price, condition;
        private ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            title = (TextView) itemView.findViewById(R.id.text_title_item);
            price = (TextView) itemView.findViewById(R.id.text_price_item);
            condition = (TextView) itemView.findViewById(R.id.text_condition_item);
        }
    }

    private class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}
