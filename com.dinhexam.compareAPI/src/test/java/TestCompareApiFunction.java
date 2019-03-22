

import java.io.File;

import com.dinhexam.compareAPI.libs.CompareAPI;

public class TestCompareApiFunction {

	public static void main(String[] args) {
		
		String curDir =  System.getProperty("user.dir");
		CompareAPI obj = new CompareAPI();
		File file01 = new File(curDir + "/src/test/resources/file01.txt");
		File file02 = new File(curDir + "/src/test/resources/file02.txt");
		obj.compare2Api(file01, file02);
		
	}

}
