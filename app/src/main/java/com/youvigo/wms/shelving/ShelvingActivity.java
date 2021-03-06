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

package com.youvigo.wms.shelving;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.youvigo.wms.R;
import com.youvigo.wms.base.BaseActivity;
import com.youvigo.wms.data.entities.Material;
import com.youvigo.wms.data.entities.Shelving;

import java.util.List;

import timber.log.Timber;

public class ShelvingActivity extends BaseActivity {
    private ProgressBar progressBar;
    private TextView materialDocument;
    private TextView sourceUnit;
    private TextView date;
    private ShelvingAdapter adapter;

    private ShelvingViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        observeData();
    }

    private void init() {
        MaterialButton materialButton = findViewById(R.id.mbt_query);
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        sourceUnit = findViewById(R.id.tv_source_unit_description);
        date = findViewById(R.id.tv_date_description);
        materialDocument = findViewById(R.id.tv_materials_description);
        progressBar = findViewById(R.id.progress_bar);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShelvingAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void observeData() {
        viewModel = ViewModelProviders.of(this).get(ShelvingViewModel.class);
        viewModel.material().observe(this, new Observer<Material>() {
            @Override
            public void onChanged(Material material) {
                if (material != null) {
                    materialDocument.setText(material.materialDocument);
                    sourceUnit.setText(material.sourceUnit);
                    date.setText(material.date);
                }
            }
        });
        viewModel.isLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isActive) {
                progressBar.setVisibility(isActive ? View.VISIBLE : View.GONE);
            }
        });
        viewModel.shelvings().observe(this, new Observer<List<Shelving>>() {
            @Override
            public void onChanged(List<Shelving> shelvings) {
                if (shelvings != null && !shelvings.isEmpty()) {
                    adapter.submitList(shelvings);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Timber.d("onActivityResult");

        if (searchResult != null) {
            viewModel.handleData(searchResult);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.shelving_activity;
    }
}
