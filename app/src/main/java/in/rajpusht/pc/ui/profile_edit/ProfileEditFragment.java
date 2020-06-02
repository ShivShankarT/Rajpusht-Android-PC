package in.rajpusht.pc.ui.profile_edit;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

import in.rajpusht.pc.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileEditFragment extends Fragment {

    private TextInputLayout first_name_tly;
    private TextInputLayout last_name_tly;
    private TextInputLayout username_tly;
    private TextInputLayout email_tly;
    private TextInputLayout phone_tly;

    public ProfileEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar=view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
        EditText first_name_et = view.findViewById(R.id.first_name_et);
        EditText last_name_et = view.findViewById(R.id.last_name_et);
        EditText email_et = view.findViewById(R.id.email_et);
        EditText user_name_et = view.findViewById(R.id.user_name_et);
        EditText phone_et = view.findViewById(R.id.phone_et);
        Button submit_btn = view.findViewById(R.id.submit_btn);

        first_name_tly = view.findViewById(R.id.first_name_tly);
        last_name_tly = view.findViewById(R.id.last_name_tly);
        username_tly = view.findViewById(R.id.username_tly);
        email_tly = view.findViewById(R.id.email_tly);
        phone_tly = view.findViewById(R.id.phone_tly);


        submit_btn.setOnClickListener(v -> {

            check(first_name_et.getText().toString(),
                    last_name_et.getText().toString(),
                    email_et.getText().toString(),
                    user_name_et.getText().toString(),
                    phone_et.getText().toString()
            );
        });


    }


    private void check(String firstName, String lastName, String email, String userName, String phone) {
        boolean isInvalid = false;

        if (firstName.length() < 5) {
            isInvalid = true;
            first_name_tly.setError(getResources().getString(R.string.error_firstname));
        } else
            first_name_tly.setError(null);

        if (lastName.length() < 1) {
            isInvalid = true;
            last_name_tly.setError(getResources().getString(R.string.error_lastname));

        } else
            last_name_tly.setError(null);

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isInvalid = true;
            email_tly.setError(getResources().getString(R.string.error_email));
        } else
            email_tly.setError(null);

        if (userName.length() < 5) {
            isInvalid = true;
            username_tly.setError(getResources().getString(R.string.error_username));
        } else
            username_tly.setError(null);

        if (phone.length() < 10) {
            isInvalid = true;
            phone_tly.setError(getResources().getString(R.string.error_phoneno));
        } else
            phone_tly.setError(null);

        if (isInvalid) {
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("firstName", firstName);
        params.put("lastName", lastName);
        params.put("username", userName);
        params.put("email", email);
        params.put("mobile", phone);


    }


}
