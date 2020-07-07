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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import in.rajpusht.pc.R;
import in.rajpusht.pc.RajpushtApp;
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
import static in.rajpusht.pc.ui.animation.CounsellingAnimationFragment.updateCounsellingTracking;

public class PregnancyGraphFragment extends BaseFragment<PregnancyGraphFragmentBinding, PregnancyGraphViewModel> implements OnChartValueSelectedListener {


    private static final String TAG = PregnancyGraphFragment.class.getName();
    @Inject
    ViewModelProviderFactory factory;

    @Inject
    DataRepository dataRepository;
    @Inject
    SchedulerProvider schedulerProvider;
    boolean isChild;
    boolean isWeightMode = true;
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
            CounsellingMedia counsellingMedia = counsellingMediaArrayList.get(pos);
            if (counsellingMedia.getType() == CounsellingMedia.GRAPH_HEIGHT_MEDIA) {
                isWeightMode = false;
            } else {
                isWeightMode = true;
            }
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
            pw_women_weight = isWeightMode ? R.string.Child_Weight : R.string.Child_Height;
        else
            pw_women_weight = R.string.PW_Women_Weight;

        pregnancyGraphFragmentBinding.toolbarLy.toolbar.setTitle(pw_women_weight);
        pregnancyGraphFragmentBinding.toolbarLy.toolbar.setNavigationOnClickListener((v) -> {
            requireActivity().onBackPressed();
        });

        pregnancyGraphFragmentBinding.titleTv.setText("Graph");

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
                updateCounsellingTracking(pos, (RajpushtApp) requireContext().getApplicationContext(), true);
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
        updateCounsellingTracking(pos, (RajpushtApp) requireContext().getApplicationContext(), false);
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

        ArrayList<Entry> idealValues1 = new ArrayList<>();
        ArrayList<Entry> minValues1 = new ArrayList<>();
        List<Entry> futureValue = new ArrayList<>();
        Pair<ChildEntity, List<LMMonitorEntity>> childEntityListPair = null;


        double birthWeight = 0.d;
        if (isChild) {
            boolean isMale = true;
            if (CounsellingMedia.counsellingChildId != 0) {
                childEntityListPair = dataRepository.getChild(CounsellingMedia.counsellingChildId).toSingle()
                        .zipWith(dataRepository.lmMonitorsByChildId(CounsellingMedia.counsellingChildId), Pair::new).blockingGet();
                if ("M".equals(childEntityListPair.first.getChildSex())) {
                    isMale = false;
                }
                if (childEntityListPair.first.getBirthWeight() != null)
                    birthWeight = childEntityListPair.first.getBirthWeight();

            }


            if (isWeightMode) {//weight
                setWeightData(idealValues1, minValues1, isMale);
            } else {


                if (isMale) {

                    idealValues1.add(new Entry(0, 49.9f));
                    idealValues1.add(new Entry(3, 61.4f));
                    idealValues1.add(new Entry(6, 67.6f));
                    idealValues1.add(new Entry(9, 72f));
                    idealValues1.add(new Entry(12, 75.7f));
                    idealValues1.add(new Entry(15, 79.1f));
                    idealValues1.add(new Entry(18, 82.3f));
                    idealValues1.add(new Entry(21, 85.1f));
                    idealValues1.add(new Entry(24, 87.8f));


                    minValues1.add(new Entry(0, 46.1f));
                    minValues1.add(new Entry(3, 57.3f));
                    minValues1.add(new Entry(6, 63.3f));
                    minValues1.add(new Entry(9, 67.5f));
                    minValues1.add(new Entry(12, 71f));
                    minValues1.add(new Entry(15, 74.1f));
                    minValues1.add(new Entry(18, 76.9f));
                    minValues1.add(new Entry(21, 79.4f));
                    minValues1.add(new Entry(24, 81.7f));

                } else {


                    idealValues1.add(new Entry(0, 45.4f));
                    idealValues1.add(new Entry(3, 55.6f));
                    idealValues1.add(new Entry(6, 61.2f));
                    idealValues1.add(new Entry(9, 65.3f));
                    idealValues1.add(new Entry(12, 68.9f));
                    idealValues1.add(new Entry(15, 72f));
                    idealValues1.add(new Entry(18, 74.9f));
                    idealValues1.add(new Entry(21, 77.5f));
                    idealValues1.add(new Entry(24, 80f));


                    minValues1.add(new Entry(0, 49.1f));
                    minValues1.add(new Entry(3, 59.8f));
                    minValues1.add(new Entry(6, 65.7f));
                    minValues1.add(new Entry(9, 70.1f));
                    minValues1.add(new Entry(12, 74f));
                    minValues1.add(new Entry(15, 77.5f));
                    minValues1.add(new Entry(18, 80.7f));
                    minValues1.add(new Entry(21, 83.7f));
                    minValues1.add(new Entry(24, 86.4f));

                }

            }


        } else {

            float initialWeight = 60f;
            idealValues1.add(new Entry(0, initialWeight));
            initialWeight += +1.5;
            idealValues1.add(new Entry(4, initialWeight));
            initialWeight += +1.5;
            idealValues1.add(new Entry(5, initialWeight));
            initialWeight += 2;
            idealValues1.add(new Entry(6, initialWeight));
            initialWeight += 2;
            idealValues1.add(new Entry(7, initialWeight));
            initialWeight += 2;
            idealValues1.add(new Entry(8, initialWeight));
            initialWeight += 1.5;
            idealValues1.add(new Entry(9, initialWeight));

        }


        String string = isWeightMode ? "Ideal Weight" : " Ideal Height";

        LineDataSet d1 = new LineDataSet(idealValues1, string);
        d1.setLineWidth(4.5f);
        d1.setCircleRadius(5.5f);
        d1.setHighLightColor(Color.rgb(0, 100, 0));
        d1.setColor(Color.rgb(0, 100, 0));
        d1.setCircleColor(Color.rgb(0, 100, 0));
        d1.setDrawValues(false);

        LineDataSet minDataset = new LineDataSet(minValues1, isWeightMode ? "Min Weight" : "Min Height");
        minDataset.setLineWidth(4.5f);
        minDataset.setCircleRadius(5.5f);
        minDataset.setHighLightColor(Color.rgb(255, 99, 71));
        minDataset.setColor(Color.rgb(255, 99, 71));
        minDataset.setCircleColor(Color.rgb(255, 99, 71));
        minDataset.setDrawValues(false);


        ArrayList<Entry> values2 = new ArrayList<>();


        if (!isChild)
            if (CounsellingMedia.counsellingPregId != 0) {
                List<PWMonitorEntity> listSingle = dataRepository.pwMonitorData(CounsellingMedia.counsellingPregId).blockingGet();
                values2 = getPwWeightDiff(listSingle);
                if (values2.isEmpty())
                    values2.add(new Entry(1, 0));
                else {
                    Entry entry = values2.get(values2.size() - 1);
                    futureValue = getFutueValue(idealValues1, entry);
                }
            } else {
                values2.add(new Entry(4, 44));
                values2.add(new Entry(5, 48));

                Entry entry = values2.get(values2.size() - 1);
                futureValue = getFutueValue(idealValues1, entry);
            }


        if (isChild) {
            if (CounsellingMedia.counsellingChildId != 0 && childEntityListPair != null) {

                values2 = isWeightMode ? getLMWeightDiff(birthWeight, childEntityListPair) : getLMWHeightDiff(childEntityListPair);
                if (values2.isEmpty())
                    values2.add(new Entry(4, 0));
                else {
                    Entry entry = values2.get(values2.size() - 1);
                    futureValue = getFutueValue(idealValues1, entry);
                }

            } else {
                if (isWeightMode) {
                    values2.add(new Entry(0, 3.0f));
                    values2.add(new Entry(3, 6.0f));
                    values2.add(new Entry(6, 6.8f));
                } else {

                    values2.add(new Entry(0, 41.4f));
                    values2.add(new Entry(3, 56.6f));
                    values2.add(new Entry(6, 60.2f));
                }

                Entry entry = values2.get(values2.size() - 1);
                futureValue = getFutueValue(idealValues1, entry);
            }
        }

        String title;
        if (isChild)
            title = isWeightMode ? "Child Weight" : " Child Height";
        else
            title = "Women Weight";

        LineDataSet d2 = new LineDataSet(values2, title);
        d2.setLineWidth(4.5f);
        d2.setCircleRadius(5.5f);
        d2.setHighLightColor(Color.rgb(0, 0, 139));
        d2.setCircleColor(Color.rgb(0, 0, 139));
        d2.setColor(Color.rgb(0, 0, 139));
        d2.setDrawValues(false);

        LineDataSet d3 = new LineDataSet(futureValue, "Trending");
        d3.setLineWidth(4.5f);
        d3.setCircleRadius(5.5f);
        d3.enableDashedLine(10,5,0);
        d3.setHighLightColor(Color.rgb(250, 0, 139));
        d3.setCircleColor(Color.rgb(250, 0, 139));
        d3.setColor(Color.rgb(250, 0, 139));
        d3.setDrawValues(false);


        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d3);
        sets.add(d2);
        sets.add(minDataset);

        LineData data = new LineData(sets);
        chart.setData(data);
        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });
        xAxis.setLabelCount(idealValues1.size(), true);
        chart.invalidate();
        chart.animateXY(2000, 2000);


    }

    private void setWeightData(ArrayList<Entry> idealValues1, ArrayList<Entry> minValues1, boolean isMale) {
        if (isMale) {

            idealValues1.add(new Entry(0, 3.3f));
            idealValues1.add(new Entry(3, 6.4f));
            idealValues1.add(new Entry(6, 7.9f));
            idealValues1.add(new Entry(9, 8.90f));
            idealValues1.add(new Entry(12, 9.6f));
            idealValues1.add(new Entry(15, 10.3f));
            idealValues1.add(new Entry(18, 10.9f));
            idealValues1.add(new Entry(21, 11.5f));
            idealValues1.add(new Entry(24, 12.2f));


            minValues1.add(new Entry(0, 2.5f));
            minValues1.add(new Entry(3, 5.0f));
            minValues1.add(new Entry(6, 6.40f));
            minValues1.add(new Entry(9, 7.1f));
            minValues1.add(new Entry(12, 7.7f));
            minValues1.add(new Entry(15, 8.3f));
            minValues1.add(new Entry(18, 8.8f));
            minValues1.add(new Entry(21, 9.2f));
            minValues1.add(new Entry(24, 9.7f));
        } else {


            idealValues1.add(new Entry(0, 3.2f));
            idealValues1.add(new Entry(3, 5.8f));
            idealValues1.add(new Entry(6, 7.3f));
            idealValues1.add(new Entry(9, 8.2f));
            idealValues1.add(new Entry(12, 8.9f));
            idealValues1.add(new Entry(15, 9.6f));
            idealValues1.add(new Entry(18, 10.2f));
            idealValues1.add(new Entry(21, 10.9f));
            idealValues1.add(new Entry(24, 11.5f));


            minValues1.add(new Entry(0, 2.4f));
            minValues1.add(new Entry(3, 4.5f));
            minValues1.add(new Entry(6, 5.7f));
            minValues1.add(new Entry(9, 6.5f));
            minValues1.add(new Entry(12, 7f));
            minValues1.add(new Entry(15, 7.6f));
            minValues1.add(new Entry(18, 8.1f));
            minValues1.add(new Entry(21, 8.6f));
            minValues1.add(new Entry(24, 9f));

        }
    }

    private List<Entry> getFutueValue(ArrayList<Entry> idealValues1, Entry entry) {
        List<Entry> futureValue = new ArrayList<>();
        futureValue.add(entry);
        double lastW = entry.getY();
        for (Entry v : getGainPercentageValueWithMonth(idealValues1)) {
            if (v.getX() > entry.getX()) {
                double v1 = lastW + ((lastW * v.getY()) / 100);
                futureValue.add(new Entry(v.getX(), (float) v1));
                lastW = v1;

            }
        }
        return futureValue;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    private ArrayList<Entry> getPwWeightDiff(List<PWMonitorEntity> pwMonitorEntities) {
        Date lmpda = counsellingPregLmp;
        ArrayList<Entry> values2 = new ArrayList<>();

        for (PWMonitorEntity pwMonitorEntity : pwMonitorEntities) {
            if (!pwMonitorEntity.getAvailable() || pwMonitorEntity.getCurrentWeight() == null)
                continue;

            int lmpMonth = 0;
            if (pwMonitorEntity.getCreatedAt() == null) {
                if (pwMonitorEntity.getSubStage().equals("PW1")) {
                    lmpMonth = 0;
                } else if (pwMonitorEntity.getSubStage().equals("PW2")) {
                    lmpMonth = 3;
                } else if (pwMonitorEntity.getSubStage().equals("PW3")) {
                    lmpMonth = 6;
                } else if (pwMonitorEntity.getSubStage().equals("PW4")) {
                    lmpMonth = 7;
                } else
                    continue;

            } else {
                Date date = AppDateTimeUtils.convertServerTimeStampDate(pwMonitorEntity.getCreatedAt());
                if (date == null)
                    continue;
                lmpMonth = HUtil.daysBetween(lmpda, date) / 30;
            }
            double v = pwMonitorEntity.getCurrentWeight();
            lmpMonth = Math.max(1, lmpMonth);
            values2.add(new Entry(lmpMonth, (float) v));

        }

        return values2;
    }

    private ArrayList<Entry> getLMWeightDiff(double birthWeight, Pair<ChildEntity, List<LMMonitorEntity>> pair) {
        ChildEntity childEntity = pair.first;
        List<LMMonitorEntity> lmMonitorEntities = pair.second;
        Date dob = childEntity.getDob();
        ArrayList<Entry> values2 = new ArrayList<>();

        values2.add(new Entry(0, (float) birthWeight));
        for (LMMonitorEntity lmMonitorEntity : lmMonitorEntities) {
            Double childWeight = lmMonitorEntity.getChildWeight();
            if (!lmMonitorEntity.getAvailable() || lmMonitorEntity.getCreatedAt() == null || childWeight == null)
                continue;
            Date date = AppDateTimeUtils.convertServerTimeStampDate(lmMonitorEntity.getCreatedAt());
            if (date != null) {
                int lmpMonth = HUtil.daysBetween(dob, date) / 30;
                lmpMonth = Math.max(1, lmpMonth);
                double childWeight1 = childWeight;
                values2.add(new Entry(lmpMonth, (float) childWeight1));
            }

        }

        return values2;
    }


    private ArrayList<Entry> getLMWHeightDiff(Pair<ChildEntity, List<LMMonitorEntity>> pair) {
        ChildEntity childEntity = pair.first;
        List<LMMonitorEntity> lmMonitorEntities = pair.second;
        Date dob = childEntity.getDob();
        ArrayList<Entry> values2 = new ArrayList<>();
        //values2.add(new Entry(0, (float) 0));
        for (LMMonitorEntity lmMonitorEntity : lmMonitorEntities) {
            Double height = lmMonitorEntity.getChildHeight();
            if (!lmMonitorEntity.getAvailable() || lmMonitorEntity.getCreatedAt() == null || height == null)
                continue;
            Date date = AppDateTimeUtils.convertServerTimeStampDate(lmMonitorEntity.getCreatedAt());
            if (date != null) {
                int lmpMonth = HUtil.daysBetween(dob, date) / 30;
                lmpMonth = Math.max(1, lmpMonth);
                double height1 = height;
                values2.add(new Entry(lmpMonth, (float) height1));
            }

        }

        return values2;
    }

    @Override
    public void onNothingSelected() {

    }

    private List<Entry> getGainPercentageValueWithMonth(ArrayList<Entry> idealValues1) {

        Entry prev = idealValues1.get(0);
        float wei = prev.getY();
        List<Entry> entries = new ArrayList<>();
        for (int i = 1; i < idealValues1.size(); i++) {
            Entry entry = idealValues1.get(i);
            float B3 = entry.getY();
            float B2 = wei;
            Entry e = new Entry(entry.getX(), ((B3 - B2) / B2) * 100);
            entries.add(e);
            Log.i(TAG, "getGainPercentageValueWithMonth: "+B3 +"---"+B2+"----"+e);
            wei = entry.getY();

        }
        return entries;
    }
}
