package in.rajpusht.pc.ui.pregnancy_graph;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.FragmentManager;
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
import in.rajpusht.pc.ui.benef_list.BeneficiaryFragment;
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
    private int pos;
    private List<CounsellingMedia> counsellingMediaArrayList;

    public static PregnancyGraphFragment newInstance(int pos) {
        PregnancyGraphFragment pregnancyGraphFragment = new PregnancyGraphFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("data", pos);
        pregnancyGraphFragment.setArguments(bundle);
        return pregnancyGraphFragment;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        counsellingMediaArrayList = CounsellingMedia.counsellingMediaData();
        isChild = !CounsellingMedia.counsellingSubstage.contains("PW");
        if (getArguments() != null) {
            pos = getArguments().getInt("data");
        }
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

        int nextCounPos = pos + 1;
        if (!(nextCounPos < counsellingMediaArrayList.size())) {
            pregnancyGraphFragmentBinding.nxtBtn.setText(R.string.finish);
        }

        pregnancyGraphFragmentBinding.nxtBtn.setOnClickListener(v -> {
            if (nextCounPos < counsellingMediaArrayList.size()) {
                CounsellingMedia nextCounsellingMedia = counsellingMediaArrayList.get(nextCounPos);

                if (nextCounsellingMedia.getType() == CounsellingMedia.IMAGE_MEDIA || nextCounsellingMedia.getType() == CounsellingMedia.VIDEO_MEDIA) {
                    FragmentUtils.replaceFragment(requireActivity(), CounsellingAnimationFragment.newInstance(nextCounPos), R.id.fragment_container, true, true, FragmentUtils.TRANSITION_POP);
                } else {
                    FragmentUtils.replaceFragment(requireActivity(), PregnancyGraphFragment.newInstance(nextCounPos), R.id.fragment_container, true, true, FragmentUtils.TRANSITION_POP);
                }


            } else {

                showAlertDialog(getString(R.string.counselling_completed), new Runnable() {
                    @Override
                    public void run() {
                        requireActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        FragmentUtils.replaceFragment(requireActivity(), new BeneficiaryFragment(), R.id.fragment_container, false, true, FragmentUtils.TRANSITION_FADE_IN_OUT);
                    }
                });
            }
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

        ArrayList<Entry> values1 = new ArrayList<>();
        Pair<ChildEntity, List<LMMonitorEntity>> childEntityListPair = null;
        if (isChild) {

            float childBirthWeight = 3;
            float childWeight = 10;
            if (CounsellingMedia.counsellingChildId != 0) {
                childEntityListPair = dataRepository.getChild(CounsellingMedia.counsellingChildId).toSingle()
                        .zipWith(dataRepository.lmMonitorsByChildId(CounsellingMedia.counsellingChildId), Pair::new).blockingGet();
                if (childEntityListPair.first.getBirthWeight() != null) {
                    double birthWeight = childEntityListPair.first.getBirthWeight();
                    childBirthWeight = (float) birthWeight;
                }

                if ("M".equals(childEntityListPair.first.getChildSex())) {
                    childWeight = 11;
                }

            }

            values1.add(new Entry(0, 0));
            values1.add(new Entry(1, childBirthWeight + 0.5f));
            values1.add(new Entry(2, childBirthWeight + 1.0f));
            values1.add(new Entry(3, childBirthWeight + 1.5f));
            values1.add(new Entry(5, childBirthWeight * 2));
            values1.add(new Entry(12, 3 * childBirthWeight));
            values1.add(new Entry(24, childWeight));

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
        d1.setLineWidth(4.5f);
        d1.setCircleRadius(7.5f);
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
                values2.add(new Entry(0, 0));
                values2.add(new Entry(4, 1));
                values2.add(new Entry(5, 1.6f));
                values2.add(new Entry(9, 2.9f));

            }
        }


        LineDataSet d2 = new LineDataSet(values2, title);
        d2.setLineWidth(4.5f);
        d2.setCircleRadius(7.5f);
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
        values2.add(new Entry(0, 0));
        Double lastWeight = 0D;

        for (PWMonitorEntity pwMonitorEntity : pwMonitorEntities) {
            if (!pwMonitorEntity.getAvailable())
                continue;
            int lmpMonth = HUtil.daysBetween(lmpda, pwMonitorEntity.getLastWeightCheckDate()) / 30;
            if (lastWeight != 0) {
                double v = Math.abs(lastWeight - pwMonitorEntity.getLastWeightInMamta());
                values2.add(new Entry(lmpMonth, (float) v));
            }
            lastWeight = pwMonitorEntity.getLastWeightInMamta();
        }

        return values2;
    }

    private ArrayList<Entry> getLMWeightDiff(Pair<ChildEntity, List<LMMonitorEntity>> pair) {
        ChildEntity childEntity = pair.first;
        List<LMMonitorEntity> lmMonitorEntities = pair.second;
        Date dob = childEntity.getDob();
        ArrayList<Entry> values2 = new ArrayList<>();


        for (LMMonitorEntity lmMonitorEntity : lmMonitorEntities) {
            Double childWeight = lmMonitorEntity.getChildWeight();
            if (!lmMonitorEntity.getAvailable() || lmMonitorEntity.getCreatedAt() == null || childWeight == null)
                continue;
            Date date = AppDateTimeUtils.convertServerTimeStampDate(lmMonitorEntity.getCreatedAt());
            if (date != null) {
                int lmpMonth = HUtil.daysBetween(dob, date) / 30;
                double childWeight1 = childWeight;
                values2.add(new Entry(lmpMonth, (float) childWeight1));
                Log.i("dssss", "getLMWeightDiff: " + lmpMonth + " === " + childWeight1);
            }

        }

        return values2;
    }

    @Override
    public void onNothingSelected() {

    }
}
