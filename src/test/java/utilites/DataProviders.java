package utilites;

import java.io.IOException;

import org.testng.annotations.DataProvider;

public class DataProviders {

    @DataProvider(name = "LoginData")
    public String[][] getData() throws IOException {

        String path = "./testData/LoginData.xlsx";//taking xl file from testdata

        ExcelUtility xlutil = new ExcelUtility(path);//creating an object for xlutility

        int totalRows = xlutil.getRowCount("Sheet1");
        int totalCols = 3; //username, password, res

        String[][] loginData = new String[totalRows][totalCols];//creating for two dimensiuon array which can store

        for (int i = 1; i <= totalRows; i++) {      // rows
            for (int j = 0; j < totalCols; j++) {   // columns 1 is row j is col
                loginData[i-1][j] = xlutil.getCellData("Sheet1", i, j); //1,0
            }
        }
        return loginData;
    }//returning  two dimension array
}