package com.example.test2.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test2.Database.Tables.Mesec;
import com.example.test2.Database.Tables.Racun;
import com.example.test2.Database.Tables.Trgovina;
import com.example.test2.Database.ViewModels.KuponkoViewModel;
import com.example.test2.Izdelek;
import com.example.test2.R;
import com.example.test2.RecyclerView.HomeAdapter;
import com.example.test2.TrgovinaProcess;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private View RootView;

    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FloatingActionButton addBtn;

    private KuponkoViewModel viewModel;

    private Mesec currentMonth;

    private GraphView graphView;

    private Uri uri;
    private Bitmap bitmap;
    private TrgovinaProcess myShops;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RootView = inflater.inflate(R.layout.fragment_home, container, false);

        buildViewModel();

        getCurrentMonth();
        getRecipts();

        setTitleText();

        graphView = RootView.findViewById(R.id.home_graph);
        GraphData();

        addBtn = RootView.findViewById(R.id.home_fragment_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRecipt();
            }
        });

        buildRecyclerView(RootView);

        return RootView;
    }

    private void getRecipts(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentMonth.getDatum());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date from = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date to = cal.getTime();

        currentMonth.setRacuni((ArrayList<Racun>) viewModel.getAllRacunsByMonth(from, to));
    }

    public void AddRecipt(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        View view = LayoutInflater.from(RootView.getContext())
                .inflate(R.layout.alert_dialog_insert_racun, (ConstraintLayout) getActivity()
                        .findViewById(R.id.alert_dialog_insert));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.alert_dialog_insert_title))
                .setText(getResources().getString(R.string.alert_dialog_insert_title));
        ((Button) view.findViewById(R.id.alert_dialog_ročno_btn))
                .setText(getResources().getString(R.string.alert_dialog_insert_manual_btn));
        ((Button) view.findViewById(R.id.alert_dialog_slika_btn))
                .setText(getResources().getString(R.string.alert_dialog_insert_slika_btn));

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.alert_dialog_ročno_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VstaviRacunRocno();
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.alert_dialog_slika_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VstaviRacunKotSliko();
                alertDialog.dismiss();
            }
        });

        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();

        // ostat more nakonc da updatas recycler view
        //adapter.notifyItemInserted(currentMonth.getRacuni().size()-1);
    }

    public void RemoveRecipt(final int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        final View view = LayoutInflater.from(RootView.getContext())
                .inflate(R.layout.alert_dialog_warning, (ConstraintLayout) getActivity()
                        .findViewById(R.id.alert_dialog_warning));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.alert_dialog_warning_title))
                .setText(getResources().getString(R.string.alert_dialog_warning_title));
        ((TextView) view.findViewById(R.id.alert_dialog_warning_description))
                .setText(getResources().getString(R.string.alert_dialog_warning_description_racuni));
        ((Button) view.findViewById(R.id.alert_dialog_false_btn))
                .setText(getResources().getString(R.string.alert_dialog_btn_false));
        ((Button) view.findViewById(R.id.alert_dialog_true_btn))
                .setText(getResources().getString(R.string.alert_dialog_btn_true));

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.alert_dialog_false_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.alert_dialog_true_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteRacun(currentMonth.getRacuni().get(pos));
                currentMonth.getRacuni().remove(pos);
                // da updejtas recycler view
                adapter.notifyItemRemoved(pos);
                Toast.makeText(view.getContext(), "Račun izbrisan", Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
            }
        });

        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

    private void buildRecyclerView(final View view){
        recyclerView = view.findViewById(R.id.home_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        adapter = new HomeAdapter();
        adapter.setRacuni(currentMonth.getRacuni());
        adapter.setViewModel(viewModel);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                RemoveRecipt(position);
            }
        });
    }

    private void buildViewModel(){
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getActivity().getApplication())).get(KuponkoViewModel.class);
    }

    private void OpenRecipt(int pos){
        Racun racun = currentMonth.getRacuni().get(pos);
        RacunOverviewFragment rof = new RacunOverviewFragment();
        Bundle args = new Bundle();
        args.putInt("idRacuna", racun.getId());
        rof.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, rof).commit();
    }

    private void getCurrentMonth(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date datum = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
        Date to = cal.getTime();
        currentMonth = viewModel.getMonthByDate(datum);
        if(currentMonth == null){
            currentMonth = new Mesec(datum, 0);
            currentMonth.setRacuni(new ArrayList<Racun>());
            viewModel.insertMesec(currentMonth);
        }else {
            //viewModel.deleteAllRacuns();
            //viewModel.deleteAllMesec();
            currentMonth.setRacuni((ArrayList<Racun>) viewModel.getAllRacunsByMonth(datum,to));
        }
    }

    private void setTitleText(){
        TextView homeMesec = RootView.findViewById(R.id.home_mesec);
        homeMesec.setText(currentMonth.getDisplayDate());

        TextView stroski = RootView.findViewById(R.id.home_stroski);
        stroski.setText("STROŠKI: " + String.format("%.2f", currentMonth.getStroski()) +"€");
    }

    private void GraphData(){
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentMonth.getDatum());
        float stroski;
        for(int i = 1; i <=  cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++){
            stroski = currentMonth.getStroskiByDay(i);
            if(stroski != 0)
                series.appendData(new DataPoint(i,stroski),true,31);
        }
        graphView.addSeries(series);
        graphView.getViewport().setMinX(1);
        graphView.getViewport().setMaxX(currentMonth.getLastDayOfMonth());
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(12);
        graphView.getViewport().setXAxisBoundsManual(true);
        GridLabelRenderer glr = graphView.getGridLabelRenderer();
        glr.setVerticalAxisTitle("Stroški €");
        glr.setHorizontalAxisTitle("Dan v mesecu");
        glr.setVerticalAxisTitleTextSize(45);
        glr.setPadding(80);
        glr.setNumHorizontalLabels(7);
    }

    private void VstaviRacunKotSliko(){
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                uri = result.getUri();

                try {
                    bitmap = getThumbnail(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                TextRecognizer textRecognizer = new TextRecognizer.Builder(getActivity().getApplicationContext()).build();

                if (!textRecognizer.isOperational()) {
                    Log.w("HomeFragment", "Detector dependencies are not yet available");
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = textRecognizer.detect(frame);
                    StringBuilder stringBuilder = new StringBuilder();

                    for (int i=0; i<items.size();++i) {
                        TextBlock item = items.valueAt(i);
                        stringBuilder.append(item.getValue());
                        stringBuilder.append("\n");
                    }

                    Log.w("izpis",stringBuilder.toString());
                    myShops = new TrgovinaProcess();

                    if (myShops.isSet(stringBuilder) && !myShops.error) {
                        Trgovina trgovina = new Trgovina(myShops.ime, myShops.naslov);
                        myShops.processIzdelki(stringBuilder);
                        viewModel.insertTrgovina(trgovina);
                        Trgovina trgovinaNew = viewModel.getTrgovinaByNameAndAddress(myShops.ime, myShops.naslov);
                        if (trgovinaNew == null)
                            trgovinaNew = trgovina;

                        Date cal = Calendar.getInstance().getTime();
                        Racun racun = new Racun(cal, trgovinaNew.getId(), myShops.znesek, myShops.izdelki);
                        viewModel.insertRacun(racun);
                        Racun racunNew = viewModel.getRacunByDate(cal);

                        RacunOverviewFragment rof = new RacunOverviewFragment();
                        Bundle args = new Bundle();
                        args.putInt("idRacuna", racunNew.getId());
                        rof.setArguments(args);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, rof).commit();
                    } else {
                        Toast.makeText(getContext(), "Trgovina ni podprta!", Toast.LENGTH_LONG).show();
                    }
                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException{
        InputStream input = getActivity().getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inDither = true;
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;
        input = getActivity().getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private void VstaviRacunRocno(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        final View view = LayoutInflater.from(RootView.getContext())
                .inflate(R.layout.alert_dialog_new_racun, (ConstraintLayout) getActivity()
                        .findViewById(R.id.alert_dialog_new_racun));
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.alert_dialog_new_racun_naprej).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText trgovina = view.findViewById(R.id.alert_dialog_new_racun_trgovina);
                EditText naslov = view.findViewById(R.id.alert_dialog_new_racun_naslov);

                if(trgovina.getText().toString().trim().length() != 0
                        && naslov.getText().toString().trim().length() != 0 ){

                    RacunOverviewFragment fragment = new RacunOverviewFragment();
                    Bundle args = new Bundle();
                    args.putString("racun_trgovina", trgovina.getText().toString().trim());
                    args.putString("racun_naslov", naslov.getText().toString().trim());
                    fragment.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack("tag3")
                            .commit();
                    alertDialog.dismiss();
                }else {
                    Toast.makeText(getContext(), "Vpišite vsa polja", Toast.LENGTH_LONG);
                }

            }
        });

        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

}
