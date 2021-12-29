package project.rew.imnuritineretcahul.ui.updates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.hymns.UpdateHymnsTask;

public class UpdatesFragment extends Fragment {
    private ProgressBar progressBar;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_updates, container, false);
        Button btn = root.findViewById(R.id.btnHymnsUpdate);
        progressBar = new ProgressBar(getContext());
        btn.setOnClickListener(v -> {
            new UpdateHymnsTask(getContext(), getActivity()).execute();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
