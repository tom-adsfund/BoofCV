/*
 * Copyright 2011 Peter Abeles
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package gecv.alg.transform.pyramid;

import gecv.core.image.GeneralizedImageOps;
import gecv.struct.gss.TestScaleSpacePyramid;
import gecv.struct.image.ImageBase;
import gecv.struct.image.ImageFloat32;
import gecv.struct.image.ImageUInt8;
import gecv.struct.pyramid.DiscreteImagePyramid;
import gecv.struct.pyramid.ImagePyramid;
import org.junit.Before;

import java.util.Random;


/**
 * @author Peter Abeles
 */
public class BasePyramidTests {

	protected Random rand = new Random(234);
	protected int width = 50;
	protected int height = 40;

	protected ImageFloat32 inputF32;
	protected ImageUInt8 inputU8;

	@Before
	public void setup() {
		inputF32 = new ImageFloat32(width,height);
		inputU8 = new ImageUInt8(width,height);

		GeneralizedImageOps.randomize(inputF32,rand,0,30);
		GeneralizedImageOps.randomize(inputU8,rand,0,30);
	}

	public <T extends ImageBase> ImagePyramid<T> createPyramid( boolean saveOriginal , int ...scales ) {
		return new DiscreteImagePyramid<T>(saveOriginal,new TestScaleSpacePyramid.DoNothingUpdater<T>(),scales);
	}
}
