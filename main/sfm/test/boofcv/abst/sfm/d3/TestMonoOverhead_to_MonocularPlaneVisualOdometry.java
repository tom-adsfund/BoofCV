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

package boofcv.abst.sfm.d3;

import boofcv.abst.feature.detect.interest.ConfigGeneralDetector;
import boofcv.abst.feature.tracker.PkltConfig;
import boofcv.abst.feature.tracker.PointTracker;
import boofcv.factory.feature.tracker.FactoryPointTracker;
import boofcv.factory.sfm.FactoryVisualOdometry;
import boofcv.struct.image.ImageSInt16;
import boofcv.struct.image.ImageType;
import boofcv.struct.image.ImageUInt8;

/**
 * @author Peter Abeles
 */
public class TestMonoOverhead_to_MonocularPlaneVisualOdometry extends CheckVisualOdometryMonoPlaneSim<ImageUInt8> {

	public TestMonoOverhead_to_MonocularPlaneVisualOdometry() {
		super(ImageUInt8.class,-20,0.04);

		setAlgorithm(createAlgorithm());
	}

	protected MonocularPlaneVisualOdometry<ImageUInt8> createAlgorithm() {

		PkltConfig config =
				PkltConfig.createDefault(ImageUInt8.class, ImageSInt16.class);
		config.pyramidScaling = new int[]{1,2,4,8};
		config.templateRadius = 3;
		config.typeInput = ImageUInt8.class;
		config.typeDeriv = ImageSInt16.class;
		ConfigGeneralDetector configDetector = new ConfigGeneralDetector(600,3,1);

		PointTracker<ImageUInt8> tracker = FactoryPointTracker.klt(config, configDetector);

		double cellSize = 0.015;
		double ransacTol = 0.2;

		return FactoryVisualOdometry.monoPlaneOverhead(
				cellSize, 25,0.5, ransacTol,300,2,30,0.5,0.3, tracker, ImageType.single(ImageUInt8.class));

	}

}
