package Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.saif.htclone.R;

import Utils.FragmentUtilities;

public class FragmentLogIn extends Fragment implements View.OnClickListener {
    private View view;
    private EditText etPhone;
    private TextView tvSignUp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_log_in, container, false);
        tvSignUp=view.findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tvSignUp:
                FragmentSignup fragmentSignup=new FragmentSignup();
                new FragmentUtilities(getActivity()).replaceFragment(R.id.container,fragmentSignup);
        }

    }
}
