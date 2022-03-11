package com.example.birdsofafeather;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.model.db.Person;

import java.util.List;

public class FavViewAdapter extends RecyclerView.Adapter<FavViewAdapter.ViewHolder> {
    private final List<Person> persons;

    public FavViewAdapter(List<Person> persons) {
        super();
        this.persons = persons;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fav_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setPerson(persons.get(position));
    }

    @Override
    public int getItemCount() {
        return this.persons.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final TextView personNameView;
        private Person person;

        ViewHolder(View itemView) {
            super(itemView);
            this.personNameView = itemView.findViewById(R.id.enterName);
            itemView.setOnClickListener(this);
        }

        public void setPerson(Person person) {
            this.person = person;
            this.personNameView.setText(person.personName);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, CourseActivity.class);
            intent.putExtra("person_name", this.person.personName);
            intent.putExtra("person_id", this.person.personId);
            //intent.putExtra("photo_url", this.person.getUrl());

            context.startActivity(intent);
        }
    }
}