package com.south.analyzer;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class TesteGeracao {
	
	public static void main(String[] args) {
		try {
			File file = new File(System.getenv("HOMEPATH") + "/data/in/loteTeste.dat");
			FileOutputStream outFile;
			outFile = new FileOutputStream(file);
			DataOutputStream outStream = new DataOutputStream(outFile);
			
			String inserir = "001ç1234567891234çPedroç50000\r\n" + 
					"001ç3245678865434çPauloç40000.99\r\n" + 
					"002ç2345675434544345çJose da SilvaçRural\r\n" + 
					"002ç2345675433444345çEduardo PereiraçRural\r\n" + 
					"003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro\r\n" + 
					"003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo\r\n" + 
					"003ç05ç[1-32-23.35,2-20-2.90,3-18-1.30]çPaulo";

			outStream.write(inserir.getBytes());
			
			System.out.println(outStream.size()+" bytes were written");
			outStream.close();
			outFile.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
