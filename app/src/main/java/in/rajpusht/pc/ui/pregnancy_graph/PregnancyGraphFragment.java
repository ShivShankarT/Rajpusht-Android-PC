package in.rajpusht.pc.ui.pregnancy_graph;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProvider;

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
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import in.rajpusht.pc.R;
import in.rajpusht.pc.ViewModelProviderFactory;
import in.rajpusht.pc.custom.utils.HUtil;
import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import in.rajpusht.pc.data.local.db.entity.LMMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PWMonitorEntity;
import in.rajpusht.pc.databinding.PregnancyGraphFragmentBinding;
import in.rajpusht.pc.model.CounsellingMedia;
import in.rajpusht.pc.ui.animation.CounsellingAnimationFragment;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.utils.AppDateTimeUtils;
import in.rajpusht.pc.utils.FragmentUtils;
import in.rajpusht.pc.utils.rx.SchedulerProvider;

import static in.rajpusht.pc.model.CounsellingMedia.counsellingPregLmp;

public class PregnancyGraphFragment extends BaseFragment<PregnancyGraphFragmentBinding, PregnancyGraphViewModel> implements OnChartValueSelectedListener {


    @Inject
    ViewModelProviderFactory factory;

    @Inject
    DataRepository dataRepository;
    @Inject
    SchedulerProvider schedulerProvider;
    boolean isChild;

    public static PregnancyGraphFragment newInstance() {
        return new PregnancyGraphFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.pregnancy_graph_fragment;
    }

    @Override
    public PregnancyGraphViewModel getViewModel() {
        return new ViewModelProvider(this, factory).get(PregnancyGraphViewModel.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isChild = !CounsellingMedia.counsellingSubstage.contains("PW");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PregnancyGraphFragmentBinding pregnancyGraphFragmentBinding = getViewDataBinding();
        int pw_women_weight;
        if (isChild)
            pw_women_weight = R.string.Child_Weight;
        else
            pw_women_weight = R.string.PW_Women_Weight;

        pregnancyGraphFragmentBinding.toolbarLy.toolbar.setTitle(pw_women_weight);
        pregnancyGraphFragmentBinding.toolbarLy.toolbar.setNavigationOnClickListener((v) -> {
            requireActivity().onBackPressed();
        });

        pregnancyGraphFragmentBinding.nxtBtn.setOnClickListener(v -> {

            FragmentUtils.replaceFragment((AppCompatActivity) requireActivity(),
                    CounsellingAnimationFragment.newInstance(3), R.id.fragment_container,
                    true, true, FragmentUtils.TRANSITION_POP);
        });


        setWeightGainGraph();
    }

    private void setWeightGainGraph() {
        LineChart chart = getViewDataBinding().chart1;
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


        ///1st Month - +500 GM
        //2nd Month - +500 GM
        //3rd Month - +500 GM
        //6th Month - Double of Birth Weight
        //1 Yr - 3 times of Birth Weight
        //2 Yr - (Boy : 10 - 12 KG , Girl : 9 -11 KG)
        ArrayList<Entry> values1 = new ArrayList<>();
        Pair<ChildEntity, List<LMMonitorEntity>> childEntityListPair = null;
        if (isChild) {

            double childWeight = 3;
            if (CounsellingMedia.counsellingChildId != 0) {
                childEntityListPair = dataRepository.getChild(CounsellingMedia.counsellingChildId).toSingle()
                        .zipWith(dataRepository.lmMonitorsByChildId(CounsellingMedia.counsellingChildId), Pair::new).blockingGet();
                if (childEntityListPair.first.getBirthWeight() != null)
                    childWeight = childEntityListPair.first.getBirthWeight();
            }

            values1.add(new Entry(1, 0.5f));
            values1.add(new Entry(2, 0.5f));
            values1.add(new Entry(3, 0.5f));
            values1.add(new Entry(6, (float) (childWeight * 2)));
            values1.add(new Entry(12, (float) (childWeight * 3)));
            values1.add(new Entry(24, 10));

        } else {
            values1.add(new Entry(4, 1.5f));
            values1.add(new Entry(5, 1.5f));
            values1.add(new Entry(6, 2));
            values1.add(new Entry(7, 2));
            values1.add(new Entry(8, 2));
            values1.add(new Entry(9, 1.5f));
        }

        String string = getString(R.string.Ideal_Weight_Gain);
        String title;
        if (isChild)
            title = getString(R.string.Child_Weight_Gain);
        else
            title = getString(R.string.Women_Weight_Gain);

        LineDataSet d1 = new LineDataSet(values1, string);
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);

        ArrayList<Entry> values2 = new ArrayList<>();


        if (!isChild)
            if (CounsellingMedia.counsellingPregId != 0) {
                List<PWMonitorEntity> listSingle = dataRepository.pwMonitorData(CounsellingMedia.counsellingPregId).blockingGet();
                values2 = getPwWeightDiff(listSingle);
                if (values2.isEmpty())
                    values2.add(new Entry(1, 0));
            } else {
                values2.add(new Entry(4, 1));
                values2.add(new Entry(5, 1));
            }

        if (isChild) {
            if (CounsellingMedia.counsellingChildId != 0 && childEntityListPair != null) {

                values2 = getLMWeightDiff(childEntityListPair);
                if (values2.isEmpty())
                    values2.add(new Entry(4, 0));

            } else {
                values2.add(new Entry(4, 1));
                values2.add(new Entry(5, 1));
                values2.add(new Entry(9, 1.9f));
            }
        }


        LineDataSet d2 = new LineDataSet(values2, title);
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
        xAxis.setLabelCount(values1.size(), true);
        chart.invalidate();
        chart.animateXY(2000, 2000);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    private ArrayList<Entry> getPwWeightDiff(List<PWMonitorEntity> pwMonitorEntities) {
        Date lmpda = counsellingPregLmp;
        ArrayList<Entry> values2 = new ArrayList<>();
        Double lastWeight = 0D;

        for (PWMonitorEntity pwMonitorEntity : pwMonitorEntities) {
            if (!pwMonitorEntity.getAvailable())
                continue;
            int lmpMonth = HUtil.daysBetween(lmpda, pwMonitorEntity.getLastWeightCheckDate()) / 30;
            if (lastWeight != 0) {
                double v = Math.abs(lastWeight - pwMonitorEntity.getLastWeightInMamta());
                values2.add(new Entry(lmpMonth, (float) v));
                Log.i("dssss", "getPWWeightDiff: " + lmpMonth + " === " + v);
            }
            lastWeight = pwMonitorEntity.getLastWeightInMamta();
        }

        return values2;
    }

    private ArrayList<Entry> getLMWeightDiff(Pair<ChildEntity, List<LMMonitorEntity>> pair) {
        ChildEntity childEntity = pair.first;
        List<LMMonitorEntity> lmMonitorEntities = pair.second;
        Date lmpda = childEntity.getDob();
        ArrayList<Entry> values2 = new ArrayList<>();
        Double lastWeight = childEntity.getBirthWeight();

        for (LMMonitorEntity lmMonitorEntity : lmMonitorEntities) {
            Double childWeight = lmMonitorEntity.getChildWeight();
            if (!lmMonitorEntity.getAvailable() || lmMonitorEntity.getCreatedAt() == null || childWeight == null)
                continue;
            Date date = AppDateTimeUtils.convertServerTimeStampDate(lmMonitorEntity.getCreatedAt());
            int lmpMonth = HUtil.daysBetween(lmpda, date) / 30;
            if (lastWeight != null && lastWeight != 0) {
                double v = Math.abs(lastWeight - childWeight);
                values2.add(new Entry(lmpMonth, (float) v));
                Log.i("dssss", "getLMWeightDiff: " + lmpMonth + " === " + v);
            }
            lastWeight = childWeight;
        }

        return values2;
    }

    @Override
    public void onNothingSelected() {

    }
}
