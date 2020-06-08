package in.rajpusht.pc.ui.login;

import android.os.Bundle;

import androidx.annotation.Nullable;

import dagger.android.support.DaggerAppCompatActivity;
import in.rajpusht.pc.R;
import in.rajpusht.pc.utils.FragmentUtils;

public class LoginActivity extends DaggerAppCompatActivity {//todo

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FragmentUtils.replaceFragment(this, new LoginFragment(), R.id.container, false, false, FragmentUtils.TRANSITION_NONE);
    }

}
