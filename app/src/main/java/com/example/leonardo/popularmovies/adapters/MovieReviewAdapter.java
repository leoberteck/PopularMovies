package com.example.leonardo.popularmovies.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leonardo.popularmovies.R;
import com.example.leonardo.popularmovies.databinding.ListReviewItemBinding;
import com.example.leonardo.popularmovies.entity.Review;
import com.example.leonardo.popularmovies.entity.ReviewPaginatedResult;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ViewHolder> {

    private ReviewPaginatedResult dataSet;
    private final String locale;

    public MovieReviewAdapter(ReviewPaginatedResult dataSet, String locale) {
        this.dataSet = dataSet;
        this.locale = locale;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListReviewItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext())
                , R.layout.list_review_item
                , parent
                , false
        );
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(dataSet.get(position));
    }

    public void addPage(ReviewPaginatedResult page){
        if(dataSet == null){
            dataSet = page;
        } else if (dataSet.getPage() < page.getPage()) {
            dataSet.addNextPage(page);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataSet != null ? dataSet.size() : 0;
    }

    public int getCurrentPage(){
        return dataSet != null ? dataSet.getPage() : 0;
    }

    public int getTotalPages(){
        return dataSet != null ? dataSet.getTotalPages() : 0;
    }

    public String getLocale() {
        return locale;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ListReviewItemBinding binding;
        private String initials;
        private String author;
        private String content;

        public ViewHolder(@NonNull View itemView, @NonNull ListReviewItemBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        private void bind(Review review){
            initials = getInitials(review.getAuthor());
            author = review.getAuthor();
            content = review.getContent();
            binding.setViewHolder(this);
        }

        @NonNull
        private String getInitials(String name){
            StringBuilder initials = new StringBuilder();
            String[] names = name.split(" ");
            for (String _name : names) {
                initials.append(_name.toUpperCase().toCharArray()[0]);
            }
            return initials.toString();
        }

        public String getInitials() {
            return initials;
        }

        public void setInitials(String initials) {
            this.initials = initials;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
