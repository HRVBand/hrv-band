package hrv.band.app.adapter;

import android.app.Activity;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 24.01.2017
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public abstract class AbstractAdapterTest {

    public abstract BaseAdapter getAdapter();
    public abstract int getSize();
    public abstract Activity getActivity();
    public abstract void checkViewElement(View view, int position);
    public abstract Object getItemAtIndex(int index);
    public abstract View getItemLayout();

    @Test
    public void shouldNotBeNull() throws Exception {
        assertNotNull(getAdapter());
    }

    @Test
    public void getCountShouldReturnProperCount() throws Exception {
        assertEquals(getAdapter().getCount(), getSize());
    }

    @Test
    public void getItemShouldReturnProperItem() throws Exception {
        for (int index = 0; index < getSize(); index++) {
            assertEquals(getAdapter().getItem(index), getItemAtIndex(index));
        }
    }

    @Test
    public void getItemIdShouldReturnProperItemId() throws Exception {
        for (int index = 0; index < getSize(); index++) {
            assertEquals(getAdapter().getItemId(index), index );
        }
    }

    @Test
    public void viewShouldNotBeNull() throws Exception {
        assertNotNull(getItemLayout());
    }

    @Test
    public void viewReturnedByGetViewShouldHaveName() throws Exception {
        for (int index = 0; index < getSize(); index++) {
            checkViewElement(getViewAtIndex(index, null), index);
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
        return getAdapter().getView(index, parent, null);
    }
}
