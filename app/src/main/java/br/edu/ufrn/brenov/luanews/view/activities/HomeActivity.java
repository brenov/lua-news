package br.edu.ufrn.brenov.luanews.view.activities;

import android.os.Bundle;

import br.edu.ufrn.brenov.luanews.R;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        super.onCreateDrawer();
        // Update title
        setTitle("");
        this.navigationView.getMenu().getItem(0).setChecked(true);
    }
}