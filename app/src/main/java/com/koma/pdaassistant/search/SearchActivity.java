/*
 * Copyright (c) 2020. komamj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.koma.pdaassistant.search;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.koma.pdaassistant.R;
import com.koma.pdaassistant.data.entities.Material;

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private MaterialButton materialButton;
    private ProgressBar progressBar;
    private AppCompatEditText editText;
    private RecyclerView recyclerView;
    private SearchAdapter adapter;

    private SearchViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_activity);

        initViews();

        observeData();
    }

    private void initViews() {
        materialButton = findViewById(R.id.mbt_query);
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText() != null) {
                    viewModel.query(editText.getText().toString());
                }
            }
        });
        editText = findViewById(R.id.edit_materials);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void observeData() {
        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

        viewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isActive) {
                progressBar.setVisibility(isActive ? View.VISIBLE : View.GONE);
            }
        });
        viewModel.materials.observe(this, new Observer<List<Material>>() {
            @Override
            public void onChanged(List<Material> materials) {

            }
        });
    }
}