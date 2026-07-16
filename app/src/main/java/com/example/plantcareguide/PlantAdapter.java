package com.example.plantcareguide;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    private Context context;
    private ArrayList<Plant> plantList;
    private DatabaseHelper dbHelper;

    public PlantAdapter(Context context, ArrayList<Plant> plantList) {
        this.context = context;
        this.plantList = plantList;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_plant, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plant = plantList.get(position);

        holder.tvName.setText(plant.getName());
        holder.tvWater.setText("الري: " + plant.getWaterSchedule());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlantDetailActivity.class);
                intent.putExtra("selected_plant", plant);
                context.startActivity(intent);
            }
        });


        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddPlantActivity.class);
                intent.putExtra("edit_mode", true);
                intent.putExtra("selected_plant", plant);
                context.startActivity(intent);
            }
        });


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos = holder.getAdapterPosition();

                if (currentPos != RecyclerView.NO_POSITION) {
                    Plant plantToDelete = plantList.get(currentPos);
                    boolean deleted = dbHelper.deletePlant(plantToDelete.getId());
                    if (deleted) {
                        plantList.remove(currentPos);
                        notifyItemRemoved(currentPos);
                        notifyItemRangeChanged(currentPos, plantList.size());
                        Toast.makeText(context, "تم حذف النبتة بنجاح!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "فشل حذف النبتة!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    public static class PlantViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvWater;
        ImageButton btnEdit, btnDelete;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemPlantName);
            tvWater = itemView.findViewById(R.id.tvItemWaterSchedule);
            btnEdit = itemView.findViewById(R.id.btnItemEdit);
            btnDelete = itemView.findViewById(R.id.btnItemDelete);
        }
    }
}