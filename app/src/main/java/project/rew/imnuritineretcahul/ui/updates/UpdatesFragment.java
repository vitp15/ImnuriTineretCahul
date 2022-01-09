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
import project.rew.imnuritineretcahul.enums.Language;
import project.rew.imnuritineretcahul.enums.Type;
import project.rew.imnuritineretcahul.utils.UpdateFilesTask;

public class UpdatesFragment extends Fragment {
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_updates, container, false);

        Button btn = root.findViewById(R.id.btnHymnsUpdate);
        Button btn1 = root.findViewById(R.id.btnAudioUpdate);
        Button btn2 = root.findViewById(R.id.btnPdfUpdate);
        progressBar = new ProgressBar(getContext());
        btn.setOnClickListener(v -> {
            new UpdateFilesTask(getContext(), getActivity(), Type.HYMN, Language.RO).execute();
        });
        btn1.setOnClickListener(v -> {
            new UpdateFilesTask(getContext(), getActivity(), Type.AUDIO, Language.RO).execute();
        });
        btn2.setOnClickListener(v -> {
            new UpdateFilesTask(getContext(), getActivity(), Type.PDF, Language.RO).execute();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
