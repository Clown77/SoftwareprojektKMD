package classes;

public class PatternFinder2 {

	public static String[] c = {"C1","C2","C3","C4","C5"};
	public static String[] h = {"H1","H2","H3","H4","H5"};
	
	public static String[][] getPattern(String[] s){
		int a = 0;
		int i = 0;
		String[][] result = new String[100][4];
		boolean b1,b2,b3,b4,d1,d2,d3,d4;
		
		for(; i <= s.length-4; i++){
			 b1 = false;
			 b2 = false;
			 b3 = false;
			 b4 = false;
			
			 d1 = false;
			 d2 = false;
			 d3 = false;
			 d4 = false;
			
			for(int j = 0; j < c.length ; j++){
				if(s[i].equals(c[j])) b1 = true;
				if(s[i+1].equals(c[j]))	b2 = true;
				if(s[i+2].equals(c[j]))	b3 = true;
				if(s[i+3].equals(c[j]))	b4 = true;
			}
			for(int j = 0; j < h.length; j++){
				if(s[i].equals(h[j]))	d1 = true;
				if(s[i+1].equals(h[j]))	d2 = true;
				if(s[i+2].equals(h[j]))	d3 = true;
				if(s[i+3].equals(h[j]))	d4 = true;
			}
			
			if(b1 == true && d2 == true && b3 == true){ //CHC
				result [a][0] = s[i];
				result [a][1] = s[i+1];
				result [a][2] = s[i+2];
				a++;
			}
			
			if(b1 == true && d2 == true && b3 == true && d4 == true){//CHCH
				result [a][0] = s[i];
				result [a][1] = s[i+1];
				result [a][2] = s[i+2];
				result [a][3] = s[i+3];
				a++;
			}
			
			if(b1 == true && d2 == true && d3 == true && b4 == true){//CHHC
				result [a][0] = s[i];
				result [a][1] = s[i+1];
				result [a][2] = s[i+2];
				result [a][3] = s[i+3];
				a++;
			}
			
			if(d1 == true && b2 == true && d3 == true && b4 == true){//HCHC
				result [a][0] = s[i];
				result [a][1] = s[i+1];
				result [a][2] = s[i+2];
				result [a][3] = s[i+3];
				a++;
			}
		}
		
		return result;
	}
	
	
	public static void main(String[] args) {
		String s1[] = {"C1","H2","C5","H3","A","C3","H2","H2","C4"};
		String s2[][] = new String[3][4];
		s2 = getPattern(s1);
		for(int i = 0; i <= 2; i++){
			for(int j = 0; j <= 3; j++){
				if(s2[i][j]!=null) System.out.print(s2[i][j]);
			}	
			System.out.println();
		}

	}

}
