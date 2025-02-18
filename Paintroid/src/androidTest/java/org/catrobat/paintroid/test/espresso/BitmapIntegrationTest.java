/*
 * Paintroid: An image manipulation application for Android.
 * Copyright (C) 2010-2015 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.paintroid.test.espresso;

import android.content.Context;
import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;

import org.catrobat.paintroid.MainActivity;
import org.catrobat.paintroid.test.utils.ScreenshotOnFailRule;
import org.catrobat.paintroid.tools.ToolType;
import org.catrobat.paintroid.tools.Workspace;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static org.catrobat.paintroid.test.espresso.util.wrappers.ToolBarViewInteraction.onToolBarView;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class BitmapIntegrationTest {
	@Rule
	public ActivityTestRule<MainActivity> launchActivityRule = new ActivityTestRule<>(MainActivity.class);

	@Rule
	public ScreenshotOnFailRule screenshotOnFailRule = new ScreenshotOnFailRule();

	@Before
	public void setUp() {
		onToolBarView()
				.performSelectTool(ToolType.BRUSH);
	}

	@Test
	public void drawingSurfaceBitmapIsDisplaySize() {
		MainActivity activity = launchActivityRule.getActivity();
		Workspace workspace = activity.workspace;
		final int bitmapWidth = workspace.getWidth();
		final int bitmapHeight = workspace.getHeight();
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
			WindowManager windowManager = (WindowManager) activity.getBaseContext().getSystemService(Context.WINDOW_SERVICE);
			WindowMetrics windowMetrics = windowManager.getCurrentWindowMetrics();
			Insets windowInsets = windowMetrics.getWindowInsets().getInsetsIgnoringVisibility(
					WindowInsets.Type.navigationBars() | WindowInsets.Type.displayCutout()
			);
			float insetsWidth = windowInsets.right + windowInsets.left;
			float insetsHeight = windowInsets.top + windowInsets.bottom;
			Rect b = windowMetrics.getBounds();
			float width = b.width() - insetsWidth;
			float height = b.height() - insetsHeight;
			assertEquals(width, bitmapWidth, 1f);
			assertEquals(height, bitmapHeight, 1f);
		} else {
			DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
			assertEquals(metrics.widthPixels, bitmapWidth);
			assertEquals(metrics.heightPixels, bitmapHeight);
		}
	}
}
