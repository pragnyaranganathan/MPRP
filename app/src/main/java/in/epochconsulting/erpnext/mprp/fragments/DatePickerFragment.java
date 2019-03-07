package in.epochconsulting.erpnext.mprp.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import in.epochconsulting.erpnext.mprp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    public DatePickerFragment() {
        // Required empty public constructor
    }
    private OnChildFragmentInteractionListener mRequestItemsParentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (getActivity() instanceof OnChildFragmentInteractionListener) {
            mRequestItemsParentListener = (OnChildFragmentInteractionListener) getActivity();
        } else {
            throw new RuntimeException("The parent fragment must implement OnChildFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mRequestItemsParentListener = null;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        //return new DatePickerDialog(getParentFragment().getContext(), this, year, month, day);

        return  new DatePickerDialog(getContext(),this,year,month,day);

    }

    public void onDateSet(DatePicker view, int year, int month, int day)  {
        // Do something with the date chosen by the user
        //have to set the date in the request items fragment edit text
        try{

        String formattedDate;
        Date parsedDate;

        Date date = new GregorianCalendar(year, month, day).getTime();
        String input = date.toString();
        SimpleDateFormat parser = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",Locale.ENGLISH);

        parsedDate= parser.parse(input);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        formattedDate = formatter.format(parsedDate);

        mRequestItemsParentListener.messageFromChildToParent(formattedDate);}
        catch (ParseException e){
            Log.i("date_format_error","There is a Parse exception when getting the date object");
        }
    }

    //interface to pass message from the dateFragment to RequestItems fragment
    public interface OnChildFragmentInteractionListener {
        void messageFromChildToParent(String myString);
    }

}
