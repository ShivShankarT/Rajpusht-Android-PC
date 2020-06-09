package in.rajpusht.pc.ui.pregnancy_graph;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import in.rajpusht.pc.R;
import in.rajpusht.pc.databinding.PregnancyGraphFragmentBinding;
import in.rajpusht.pc.ui.animation.CounsellingAnimationFragment;
import in.rajpusht.pc.utils.FragmentUtils;

public class PregnancyGraphFragment extends Fragment implements OnChartValueSelectedListener {

    private PregnancyGraphFragmentBinding pregnancyGraphFragmentBinding;
    private PregnancyGraphViewModel mViewModel;

    public static PregnancyGraphFragment newInstance() {
        return new PregnancyGraphFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        pregnancyGraphFragmentBinding = PregnancyGraphFragmentBinding.inflate(inflater, container, false);
        //return inflater.inflate(R.layout.pregnancy_graph_fragment, container, false);
        return pregnancyGraphFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PregnancyGraphViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pregnancyGraphFragmentBinding.toolbarLy.toolbar.setTitle("PW Women Weight ");
        pregnancyGraphFragmentBinding.toolbarLy.toolbar.setNavigationOnClickListener((v)->{
            requireActivity().onBackPressed();
        });

        pregnancyGraphFragmentBinding.nxtBtn.setOnClickListener(v -> {

            FragmentUtils.replaceFragment((AppCompatActivity) requireActivity(),
                    CounsellingAnimationFragment.newInstance(3), R.id.fragment_container,
                    true, true, FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);
        });


        setWeightGainGraph();
    }

    private void setWeightGainGraph() {
        LineChart chart = pregnancyGraphFragmentBinding.chart1;
        chart.setOnChartValueSelectedListener(this);
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);



        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = chart.getAxisLeft();
        //leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        //rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(5, false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)


        ArrayList<Entry> values1 = new ArrayList<>();
        values1.add(new Entry(4, 1.5f));
        values1.add(new Entry(5, 1.5f));
        values1.add(new Entry(6, 2));
        values1.add(new Entry(7, 2));
        values1.add(new Entry(8, 2));
        values1.add(new Entry(9, 1.5f));

        LineDataSet d1 = new LineDataSet(values1, "Ideal Weight Gain");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);

        ArrayList<Entry> values2 = new ArrayList<>();
        values2.add(new Entry(4, 1));
        values2.add(new Entry(5, 1));
        LineDataSet d2 = new LineDataSet(values2, "Women Weight Gain");
        d2.setLineWidth(2.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d2);

        LineData data = new LineData(sets);
        chart.setData(data);
        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });
        xAxis.setLabelCount(values1.size(),true);
        chart.invalidate();
        chart.animateXY(2000, 2000);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
