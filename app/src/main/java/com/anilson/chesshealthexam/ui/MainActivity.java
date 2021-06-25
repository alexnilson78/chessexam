package com.anilson.chesshealthexam.ui;

import com.google.android.material.snackbar.Snackbar;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import dagger.hilt.android.AndroidEntryPoint;

import com.anilson.chesshealthexam.R;
import com.anilson.chesshealthexam.databinding.ActivityMainBinding;
import com.anilson.chesshealthexam.viewmodels.PersonListViewModel;

import android.view.Menu;
import android.view.MenuItem;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private PersonListViewModel viewModel;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private boolean showSearch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        viewModel = new ViewModelProvider(this).get(PersonListViewModel.class);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            handleSearchIntent(getIntent());
        }

        setUpSearchObserver();
        subscribeToErrorLiveData();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setVisible(showSearch);
        menu.getItem(1).setVisible(!showSearch);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_reset) {
            viewModel.removeEveryone();
            return true;
        } else  if (id == R.id.action_search) {
            onSearchRequested();
            return true;
        } else  if (id == R.id.clear_search) {
            viewModel.setSearchName("");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            handleSearchIntent(intent);
        } else {
            super.onNewIntent(intent);
        }
    }

    private void handleSearchIntent(Intent intent) {
        String query = intent.getStringExtra(SearchManager.QUERY);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        navController.popBackStack();
        navController.navigate(R.id.action_global_ListFragment);
        viewModel.setSearchName(query);
    }

    private void setUpSearchObserver() {
        viewModel.getSearchTerm().observe(this, s -> {
            if(s != null && !s.isEmpty()) {
                showSearch = false;
            } else {
                showSearch = true;
            }
            invalidateOptionsMenu();
        });
    }

    private void subscribeToErrorLiveData() {
        viewModel.getErrorData().observe(this, stringResEvent -> {
            Integer value = stringResEvent.getContentIfNotHandledOrReturnNull();
            if(value != null) {
                Snackbar.make(this, binding.getRoot(), getString(value), Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}