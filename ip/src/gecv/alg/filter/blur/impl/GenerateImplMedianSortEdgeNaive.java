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

package gecv.alg.filter.blur.impl;

import gecv.misc.CodeGeneratorBase;
import gecv.misc.CodeGeneratorUtil;
import gecv.misc.TypeImage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * @author Peter Abeles
 */
public class GenerateImplMedianSortEdgeNaive extends CodeGeneratorBase {
	String className = "ImplMedianSortEdgeNaive";

	PrintStream out;

	TypeImage imageIn;

	public GenerateImplMedianSortEdgeNaive() throws FileNotFoundException {
		out = new PrintStream(new FileOutputStream(className + ".java"));
	}

	@Override
	public void generate() throws FileNotFoundException {
		printPreamble();

		printFunction(TypeImage.F32);
		printFunction(TypeImage.I);

		out.print("\n" +
				"}\n");
	}

	private void printPreamble() {
		out.print(CodeGeneratorUtil.copyright);
		out.print("package gecv.alg.filter.blur.impl;\n" +
				"\n" +
				"import gecv.struct.image.ImageFloat32;\n" +
				"import gecv.struct.image.ImageInteger;\n" +
				"import pja.sorting.QuickSelectArray;\n" +
				"\n" +
				"/**\n" +
				" * <p>\n" +
				" * Median filter which process only the image edges and uses quick select find the median.\n" +
				" * </p>\n" +
				" *\n" +
				" * <p>\n" +
				" * radius: size of the filter's box.<br>\n" +
				" * storage:  Used to store local values.  If null an array will be declared.\n" +
				" * </p>\n" +
				" * \n" +
				" * <p>\n" +
				" * DO NOT MODIFY: This class was automatically generated by {@link GenerateImplMedianSortEdgeNaive}\n" +
				" * </p>\n" +
				" *\n" +
				" * @author Peter Abeles\n" +
				" */\n" +
				"public class "+className+" {\n\n");
	}

	private void printFunction( TypeImage image ) {

		String primitiveType = image.getDataType();
		String sumType = image.getSumType();

		out.print("\tpublic static void process("+image.getImageName()+" input, "+image.getImageName()+" output, int radius , "+sumType+" storage[] )\n" +
				"\t{\n" +
				"\t\tint w = 2*radius+1;\n" +
				"\t\tif( storage == null ) {\n" +
				"\t\t\tstorage = new "+sumType+"[ w*w ];\n" +
				"\t\t} else if( storage.length < w*w ) {\n" +
				"\t\t\tthrow new IllegalArgumentException(\"'storage' must be at least of length \"+(w*w));\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tfor( int y = 0; y < radius; y++ ) {\n" +
				"\t\t\tint minI = y - radius;\n" +
				"\t\t\tint maxI = y + radius+1;\n" +
				"\t\t\tif( minI < 0 ) minI = 0;\n" +
				"\t\t\tif( maxI > input.height ) maxI = input.height;\n" +
				"\t\t\t\n" +
				"\t\t\tfor( int x = 0; x < input.width; x++ ) {\n" +
				"\t\t\t\tint minJ = x - radius;\n" +
				"\t\t\t\tint maxJ = x + radius+1;\n" +
				"\n" +
				"\t\t\t\t// bound it ot be inside the image\n" +
				"\t\t\t\tif( minJ < 0 ) minJ = 0;\n" +
				"\t\t\t\tif( maxJ > input.width ) maxJ = input.width;\n" +
				"\n" +
				"\t\t\t\tint index = 0;\n" +
				"\n" +
				"\t\t\t\tfor( int i = minI; i < maxI; i++ ) {\n" +
				"\t\t\t\t\tfor( int j = minJ; j < maxJ; j++ ) {\n" +
				"\t\t\t\t\t\tstorage[index++] = input.get(j,i);\n" +
				"\t\t\t\t\t}\n" +
				"\t\t\t\t}\n" +
				"\t\t\t\t\n" +
				"\t\t\t\t// use quick select to avoid sorting the whole list\n" +
				"\t\t\t\t"+sumType+" median = QuickSelectArray.select(storage,index/2,index);\n" +
				"\t\t\t\toutput.set(x,y, median );\n" +
				"\t\t\t}\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tfor( int y = input.height-radius; y < input.height; y++ ) {\n" +
				"\t\t\tint minI = y - radius;\n" +
				"\t\t\tint maxI = y + radius+1;\n" +
				"\t\t\tif( minI < 0 ) minI = 0;\n" +
				"\t\t\tif( maxI > input.height ) maxI = input.height;\n" +
				"\n" +
				"\t\t\tfor( int x = 0; x < input.width; x++ ) {\n" +
				"\t\t\t\tint minJ = x - radius;\n" +
				"\t\t\t\tint maxJ = x + radius+1;\n" +
				"\n" +
				"\t\t\t\t// bound it ot be inside the image\n" +
				"\t\t\t\tif( minJ < 0 ) minJ = 0;\n" +
				"\t\t\t\tif( maxJ > input.width ) maxJ = input.width;\n" +
				"\n" +
				"\t\t\t\tint index = 0;\n" +
				"\n" +
				"\t\t\t\tfor( int i = minI; i < maxI; i++ ) {\n" +
				"\t\t\t\t\tfor( int j = minJ; j < maxJ; j++ ) {\n" +
				"\t\t\t\t\t\tstorage[index++] = input.get(j,i);\n" +
				"\t\t\t\t\t}\n" +
				"\t\t\t\t}\n" +
				"\n" +
				"\t\t\t\t// use quick select to avoid sorting the whole list\n" +
				"\t\t\t\t"+sumType+" median = QuickSelectArray.select(storage,index/2,index);\n" +
				"\t\t\t\toutput.set(x,y, median );\n" +
				"\t\t\t}\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tfor( int y = radius; y < input.height-radius; y++ ) {\n" +
				"\t\t\tint minI = y - radius;\n" +
				"\t\t\tint maxI = y + radius+1;\n" +
				"\t\t\tfor( int x = 0; x < radius; x++ ) {\n" +
				"\t\t\t\tint minJ = x - radius;\n" +
				"\t\t\t\tint maxJ = x + radius+1;\n" +
				"\n" +
				"\t\t\t\t// bound it ot be inside the image\n" +
				"\t\t\t\tif( minJ < 0 ) minJ = 0;\n" +
				"\t\t\t\tif( maxJ > input.width ) maxJ = input.width;\n" +
				"\n" +
				"\t\t\t\tint index = 0;\n" +
				"\n" +
				"\t\t\t\tfor( int i = minI; i < maxI; i++ ) {\n" +
				"\t\t\t\t\tfor( int j = minJ; j < maxJ; j++ ) {\n" +
				"\t\t\t\t\t\tstorage[index++] = input.get(j,i);\n" +
				"\t\t\t\t\t}\n" +
				"\t\t\t\t}\n" +
				"\n" +
				"\t\t\t\t// use quick select to avoid sorting the whole list\n" +
				"\t\t\t\t"+sumType+" median = QuickSelectArray.select(storage,index/2,index);\n" +
				"\t\t\t\toutput.set(x,y, median );\n" +
				"\t\t\t}\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tfor( int y = radius; y < input.height-radius; y++ ) {\n" +
				"\t\t\tint minI = y - radius;\n" +
				"\t\t\tint maxI = y + radius+1;\n" +
				"\t\t\tfor( int x = input.width-radius; x < input.width; x++ ) {\n" +
				"\t\t\t\tint minJ = x - radius;\n" +
				"\t\t\t\tint maxJ = x + radius+1;\n" +
				"\n" +
				"\t\t\t\t// bound it ot be inside the image\n" +
				"\t\t\t\tif( minJ < 0 ) minJ = 0;\n" +
				"\t\t\t\tif( maxJ > input.width ) maxJ = input.width;\n" +
				"\n" +
				"\t\t\t\tint index = 0;\n" +
				"\n" +
				"\t\t\t\tfor( int i = minI; i < maxI; i++ ) {\n" +
				"\t\t\t\t\tfor( int j = minJ; j < maxJ; j++ ) {\n" +
				"\t\t\t\t\t\tstorage[index++] = input.get(j,i);\n" +
				"\t\t\t\t\t}\n" +
				"\t\t\t\t}\n" +
				"\n" +
				"\t\t\t\t// use quick select to avoid sorting the whole list\n" +
				"\t\t\t\t"+sumType+" median = QuickSelectArray.select(storage,index/2,index);\n" +
				"\t\t\t\toutput.set(x,y, median );\n" +
				"\t\t\t}\n" +
				"\t\t}\n" +
				"\t}\n\n");
	}

	public static void main( String args[] ) throws FileNotFoundException {
		GenerateImplMedianSortEdgeNaive app = new GenerateImplMedianSortEdgeNaive();
		app.generate();
	}
}
