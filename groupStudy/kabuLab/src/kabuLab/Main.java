/**
 *
 */
package kabuLab;

/**
 * @author 17ec084(http://github.com/17ec084)
 *
 */
public class Main
{
	/**
	 * ListToCSVのテスト
	 * @param args
	 */
 /*   public static void main(String args[])
    {
    	List<String> list = new ArrayList<String>();

        list.add("1");
        list.add("2");
        list.add("3");
        list.add("");
        list.add("");
        list.add("4");


        ListToCSV list2csv = new ListToCSV(list);
        //System.out.println(list2csv.putCSV());
        list2csv.putCSV("aaa.csv");
        return;
    }
*/
	public static void main(String args[])
    {
    	String csvPass="ccc.csv";
        ShowCSV arr= new ShowCSV(csvPass, true);
        int sizeC = arr.getCntColumn();
        int sizeR = arr.getCntRow();
        System.out.println("sizeC="+sizeC);
        System.out.println("sizeR="+sizeR);
        for(int i=0; i<sizeR; i++)
        {
        	for(int j=0; j<sizeC; j++)
        	{
        		System.out.print("("+i+","+j+")"+arr.getCell(i,j)+" ");
        	}
        	System.out.print("\n");
        }
        return;
    }


/*	public static void main(String args[])
    {
    	String csvPass="bbb.csv";
        ReadCSV arr= new ReadCSV(csvPass, true);
        return;
    }
*/
}
