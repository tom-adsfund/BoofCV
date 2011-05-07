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

package gecv.struct.pyramid;

import gecv.struct.image.ImageFloat32;

/**
 * Implementation of {@link ImagePyramid} for {@link ImageFloat32}.
 *
 * @author Peter Abeles
 */
public class ImagePyramid_F32 extends ImagePyramid<ImageFloat32> {
	/**
	 * Specifies input image size and behavior of top most layer.
	 *
	 * @param topWidth			  Width of original full resolution image.
	 * @param topHeight			 Height of original full resolution image.
	 * @param saveOriginalReference If a reference to the full resolution image should be saved instead of  copied.
	 */
	public ImagePyramid_F32(int topWidth, int topHeight, boolean saveOriginalReference) {
		super(topWidth, topHeight, saveOriginalReference);
	}

	@Override
	protected ImageFloat32 createImage(int width, int height) {
		return new ImageFloat32(width, height);
	}

	@Override
	public Class<ImageFloat32> getImageType() {
		return ImageFloat32.class;
	}
}