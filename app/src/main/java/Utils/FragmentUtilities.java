package Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by Saif on 10/13/2016.
 */
public class FragmentUtilities {
    private FragmentActivity activity;

    /*Constructors*/
    public FragmentUtilities(AppCompatActivity activity) {
        this.activity = activity;
    }

    public FragmentUtilities(FragmentActivity activity) {
        this.activity = activity;

    }


    /*Add new Fragment
     * @param int fragmentLayout -the layout to be displayed with the fragment
     * @param Fragment - The instance of the fragment to be added
     */
    public void addFragment(int fragmentLayout, Fragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .add(fragmentLayout, fragment)
                .commit();
    }

    /*Replace new Fragment with old one
     * @param int fragmentLayout -the layout to be displayed with the fragment
     * @param Fragment - The instance of the fragment to be replaced
     */
    public void replaceFragment(int fragmentLayout, Fragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .replace(fragmentLayout, fragment)
                // Add this transaction to the back stack
                .addToBackStack(null)
                .commit();
    }

    /*Replace Fragment without keeping a back trace
     * @param int fragmentLayout -the layout to be displayed with the fragment
     * @param Fragment - The instance of the fragment to be replaced
     */
    public void replaceFragmentWithoutBackTrace(int fragmentLayout, Fragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .replace(fragmentLayout, fragment)
                .commit();
    }
}
