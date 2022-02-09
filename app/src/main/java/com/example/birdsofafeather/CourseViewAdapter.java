package com.example.birdsofafeather;


import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iteration1.model.db.Course;

import java.util.List;
import java.util.function.Consumer;


public class CourseViewAdapter extends RecyclerView.Adapter<CourseViewAdapter.ViewHolder> {
    private final List<Course> course;
    private final Consumer<Course> onNoteRemoved;

    public CourseViewAdapter(List<Course> course, Consumer<Course> onNoteRemoved) {
        super();
        this.course = course;
        this.onNoteRemoved = onNoteRemoved;
    }

//    @NonNull
//    @Override
//    public NotesViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_row, parent, false);
//        return null;
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_row, parent, false);
        return new ViewHolder(view, this::removeCourse, onNoteRemoved);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setCourse(course.get(position));

    }

    @Override
    public int getItemCount() {
        return this.course.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView courseTextView;
        private Course course;

        ViewHolder(View itemView){
            super(itemView);
            this.courseTextView = itemView.findViewById(R.id.course_row_text);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        ViewHolder(View itemView, Consumer<Integer> removeNote, Consumer<Course> onNoteRemoved) {
            super(itemView);
            this.courseTextView = itemView.findViewById(R.id.course_row_text);

            Button removeButton = itemView.findViewById(R.id.remove_course_button);
            removeButton.setOnClickListener((view) -> {
                removeNote.accept(this.getAdapterPosition());
                onNoteRemoved.accept(course);
            });
        }

        public void setCourse(Course course){
            this.course = course;
            this.courseTextView.setText(course.year +" " + course.quarter + " " + course.courseName + " " + course.courseNum);
        }
    }

    public void addCourse(Course course){
        this.course.add(course);
        this.notifyItemInserted(this.course.size() -1 );
    }

    public void removeCourse(int position){
        this.course.remove(position);
        this.notifyItemRemoved(position);
    }


}

