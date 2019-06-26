package kabuLab2;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, ParseException
	{
		CSVReader arr = new CSVReader("A1\036B1\035A2\036B2\035A3\036B3\0");
        int sizeC = arr.getCntOfC();
		ArrayList<ArrayList<String>> arrTable = arr.getArrTable();
        int sizeR = arr.getCntOfR();
        System.out.println("sizeC="+sizeC);
        System.out.println("sizeR="+sizeR);
        for(int i=0; i<sizeR; i++)
        {
        	for(int j=0; j<sizeC; j++)
        	{
        		System.out.print("("+i+","+j+")"+getCell(i,j, arrTable)+" ");
        	}
        	System.out.print("\n");
        }
	}

	static String getCell(int rowIndex, int columnIndex, ArrayList<ArrayList<String>> arrTable)
	{
		boolean isExist=arrTable.size()>rowIndex;
		isExist=isExist && arrTable.get(rowIndex).size()>columnIndex;
		if(isExist)
		{
			return arrTable.get(rowIndex).get(columnIndex);
	    }
		else
		{
			return "";
	    }
	}
}
