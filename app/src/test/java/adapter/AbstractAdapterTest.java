package adapter;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import hrv.band.app.BuildConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 24.01.2017
 */

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public abstract class AbstractAdapterTest {

    protected Fragment fragment;

    public abstract ListView getListView();
    public abstract int getSize();
    public abstract void checkViewElement(View view, int position);
    public abstract Object getItemAtIndex(int index);
    public abstract View getItemLayout();
    public abstract Fragment getFragment();

    @Before
    public void setUpFragment() throws Exception {
        fragment = getFragment();
        SupportFragmentTestUtil.startVisibleFragment(fragment);
    }

    @Test
    public void shouldNotBeNull() throws Exception {
        assertNotNull(getListView());
    }

    @Test
    public void getCountShouldReturnProperCount() throws Exception {
        assertEquals(getListView().getCount(), getSize());
    }

    @Test
    public void getItemShouldReturnProperItem() throws Exception {
        for (int index = 0; index < getSize(); index++) {
            assertEquals(getListView().getItemAtPosition(index), getItemAtIndex(index));
        }
    }

    @Test
    public void getItemIdShouldReturnProperItemId() throws Exception {
        for (int index = 0; index < getSize(); index++) {
            assertEquals(getListView().getItemIdAtPosition(index), index );
        }
    }

    @Test
    public void viewShouldNotBeNull() throws Exception {
        assertNotNull(getItemLayout());
    }

    @Test
    public void viewReturnedByGetViewShouldHaveName() throws Exception {
        for (int index = 0; index < getSize(); index++) {
            checkViewElement(getListView().getAdapter().getView(index, null, null), index);
        }
    }

    @Test
    public void checkIfViewIsRecycled() throws Exception {
        for (int index = 0; index < getSize(); index++) {
            View view = getViewAtIndex(index, null);
            View viewRecycled = getViewAtIndex(index, view);
            assertSame(view, viewRecycled);
        }
    }

    private View getViewAtIndex(int index, View parent) {
        return getListView().getAdapter().getView(index, parent, null);
    }
}
