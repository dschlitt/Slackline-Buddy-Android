package edu.bc.schlitda.slacbuddy;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;


public class HowToLandingPage extends Activity {
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.how_to_landing_page);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.howto, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.videoLink:
			Intent vidIntent = new Intent(Intent.ACTION_VIEW);
			vidIntent.setData(Uri
					.parse("http://youtu.be/K7GrfqPKTYg"));
			startActivity(vidIntent);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);

	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 5 total pages.
			return 5;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = null;
			Integer screen = getArguments().getInt(ARG_SECTION_NUMBER);

			switch (screen) {
			case 1:
				rootView = inflater.inflate(R.layout.ht1, container, false);
				break;
			case 2:
				rootView = inflater.inflate(R.layout.ht2, container, false);
				break;
			case 3:
				rootView = inflater.inflate(R.layout.ht3, container, false);
				break;
			case 4:
				rootView = inflater.inflate(R.layout.ht4, container, false);
				break;
			case 5:
				rootView = inflater.inflate(R.layout.ht5, container, false);
				break;
			default:
				rootView = inflater.inflate(R.layout.fragment_how_to,
						container, false);
				break;
			}
			
			TranslateAnimation animation1 = new TranslateAnimation(0, -150, 0, 0);
			TranslateAnimation animation2 = new TranslateAnimation(0, 150, 0, 0);
			
			animation1.setDuration(1500);
			animation1.setInterpolator(new BounceInterpolator());
			animation1.setFillAfter(true);
			animation2.setDuration(1500);
			animation2.setInterpolator(new BounceInterpolator());
			
			rootView.startAnimation(animation1);
			rootView.startAnimation(animation2);
			
			return rootView;
		}
	}
}
