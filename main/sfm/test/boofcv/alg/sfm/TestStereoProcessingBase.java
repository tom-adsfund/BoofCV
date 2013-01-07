/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://boofcv.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package boofcv.alg.sfm;

import boofcv.alg.misc.ImageStatistics;
import boofcv.struct.calib.IntrinsicParameters;
import boofcv.struct.calib.StereoParameters;
import boofcv.struct.image.ImageUInt8;
import georegression.geometry.RotationMatrixGenerator;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.se.Se3_F64;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Peter Abeles
 */
public class TestStereoProcessingBase {

	int width = 320;
	int height = 240;

	/**
	 * Center a point in the left and right images.  Search for the point and see if after rectification
	 * the point can be found on the same row in both images.
	 */
	@Test
	public void checkRectification() {
		// point being viewed
		Point3D_F64 X = new Point3D_F64(-0.01,0.1,3);

		StereoParameters param = createStereoParam(width,height,false);

		// create input images by rendering the point in both
		ImageUInt8 left = new ImageUInt8(width,height);
		ImageUInt8 right = new ImageUInt8(width,height);

		// compute the view in pixels of the point in the left and right cameras
		Point2D_F64 lensLeft = new Point2D_F64();
		Point2D_F64 lensRight = new Point2D_F64();
		SfmTestHelper.renderPointPixel(param,X,lensLeft,lensRight);

		// render the pixel in the image
		left.set((int)lensLeft.x,(int)lensLeft.y,200);
		right.set((int)lensRight.x,(int)lensRight.y,200);

		// test the algorithm
		StereoProcessingBase<ImageUInt8> alg = new StereoProcessingBase<ImageUInt8>(ImageUInt8.class);
		alg.setCalibration(param);

		alg.setImages(left,right);
		alg.initialize();

		// Test tolerances are set to one pixel due to discretization errors in the image
		// sanity check test
		assertFalse(Math.abs(lensLeft.y - lensRight.y) <= 1);

		// check properties of a rectified image and stereo pairs
		Point2D_F64 foundLeft = centroid(alg.getImageLeftRect());
		Point2D_F64 foundRight = centroid(alg.getImageRightRect());

		assertTrue(Math.abs(foundLeft.y - foundRight.y) <= 1);
		assertTrue(foundRight.x < foundLeft.x);
	}

	@Test
	public void compute3D() {
		fail("Implement");
	}

	/**
	 * Finds the mean point in the image weighted by pixel intensity
	 */
	public static Point2D_F64 centroid( ImageUInt8 image ) {
		double meanX = 0;
		double meanY = 0;
		double totalPixel = ImageStatistics.sum(image);

		for( int i = 0; i < image.height; i++ ) {
			for( int j = 0; j < image.width; j++ ) {
				meanX += image.get(j,i)*j;
				meanY += image.get(j,i)*i;
			}
		}

		meanX /= totalPixel;
		meanY /= totalPixel;

		return new Point2D_F64(meanX,meanY);
	}

	public static StereoParameters createStereoParam( int width , int height , boolean flipY ) {
		StereoParameters ret = new StereoParameters();

		ret.setRightToLeft(new Se3_F64());
		ret.getRightToLeft().getT().set(-0.2,0.001,-0.012);
		RotationMatrixGenerator.eulerXYZ(0.001, -0.01, 0.0023, ret.getRightToLeft().getR());

		ret.left = new IntrinsicParameters(300,320,0,width/2,height/2,width,height, flipY, new double[]{0.1,1e-4});
		ret.right = new IntrinsicParameters(290,310,0,width/2+2,height/2-6,width,height, flipY, new double[]{0.05,-2e-4});

		return ret;
	}

}
