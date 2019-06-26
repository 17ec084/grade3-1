package kabuLab.ArrayListEditor;

import java.util.ArrayList;

class Main2
{

	public static void main(String[] args)
	{
		String str="";
		ArrayList<ArrayList<String>> arrTable = new ArrayList<ArrayList<String>>();
		ArrayList<String> arrRow = new ArrayList<String>();
		ArrayList<Integer> arr = new ArrayList<Integer>();
		arrRow.add("hello,world");
		arrTable.add(arrRow);
		TPI a=new TPI(arrTable);
		System.out.println(a.getResult());
        a=new TPI(arrRow);
		System.out.println(a.getResult());
		arrRow.clear();
		a=new TPI(arrTable);
		System.out.println(a.getResult());
        a=new TPI(arrRow);
		System.out.println(a.getResult());
        a=new TPI(arr);
		System.out.println(a.getResult());
		arr.add(0);
        a=new TPI(arr);
		System.out.println(a.getResult());
		arrRow.add(null);
        a=new TPI(arr);
		System.out.println(a.getResult());
	}

}