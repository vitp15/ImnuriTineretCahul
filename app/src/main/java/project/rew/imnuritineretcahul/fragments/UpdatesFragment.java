package project.rew.imnuritineretcahul.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import project.rew.imnuritineretcahul.R;
import project.rew.imnuritineretcahul.enums.Type;
import project.rew.imnuritineretcahul.utils.UpdateAllFilesTask;
import project.rew.imnuritineretcahul.utils.UpdateFilesTask;

public class UpdatesFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_updates, container, false);

        Button btn = root.findViewById(R.id.btnHymnsUpdate);
        Button btn1 = root.findViewById(R.id.btnAudioUpdate);
        Button btn2 = root.findViewById(R.id.btnPdfUpdate);
        Button btn3 = root.findViewById(R.id.btnHymnsUpdate_ru);
        Button btn4 = root.findViewById(R.id.btnAudioUpdate_ru);
        Button btn5 = root.findViewById(R.id.btnPdfUpdate_ru);

        /*Button btn6 = root.findViewById(R.id.btnHymnsUpdateN);
        Button btn7 = root.findViewById(R.id.btnAudioUpdateN);
        Button btn8 = root.findViewById(R.id.btnPdfUpdateN);
        Button btn9 = root.findViewById(R.id.btnHymnsUpdate_ruN);
        Button btn10 = root.findViewById(R.id.btnAudioUpdate_ruN);
        Button btn11 = root.findViewById(R.id.btnPdfUpdate_ruN);*/


        btn.setOnClickListener(v -> {
            new UpdateFilesTask(getContext(), getActivity(), Type.HYMN).execute();
        });

        btn1.setOnClickListener(v -> {
            new UpdateFilesTask(getContext(), getActivity(), Type.AUDIO).execute();
        });
        btn2.setOnClickListener(v -> {
            new UpdateFilesTask(getContext(), getActivity(), Type.PDF).execute();
        });
        btn3.setOnClickListener(v -> {
            new UpdateAllFilesTask(getContext(), getActivity()).execute();
        });
        /*btn4.setOnClickListener(v -> {
            new UpdateFilesTask(getContext(), getActivity(), Type.AUDIO, Language.RU).execute();
        });
        btn5.setOnClickListener(v -> {
            new UpdateFilesTask(getContext(), getActivity(), Type.PDF, Language.RU).execute();
        });*/


       /* btn6.setOnClickListener(v -> {
            new UpdateFilesTask(getContext(), getActivity(), Type.HYMN, Language.RO).execute();
        });
        btn7.setOnClickListener(v -> {
            new UpdateFilesTask(getContext(), getActivity(), Type.AUDIO, Language.RO).execute();
        });
        btn8.setOnClickListener(v -> {
            new UpdateFilesTask(getContext(), getActivity(), Type.PDF, Language.RO).execute();
        });
        btn9.setOnClickListener(v -> {
            new UpdateFilesTask(getContext(), getActivity(), Type.HYMN, Language.RU).execute();
        });
        btn10.setOnClickListener(v -> {
            new UpdateFilesTask(getContext(), getActivity(), Type.AUDIO, Language.RU).execute();
        });
        btn11.setOnClickListener(v -> {
            new UpdateFilesTask(getContext(), getActivity(), Type.PDF, Language.RU).execute();
        });*/
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
