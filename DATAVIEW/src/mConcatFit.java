import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import dataview.models.DATAVIEW_BigFile;
import dataview.models.Dataview;
import dataview.models.InputPort;
import dataview.models.OutputPort;
import dataview.models.Port;
import dataview.models.Task;



public class mConcatFit extends Task{
	int num_mDiffFit = 8;
	public mConcatFit()
	{
		super("mConcatFit", "e difference images are then fitted using a least squares algorithm");
		
		ins = new InputPort[num_mDiffFit];
		outs = new OutputPort[1];
		
		Dataview.debugger.logObjectValue("num_mDiffFit", num_mDiffFit);
		
		for(int i = 0; i < num_mDiffFit; i++){
			ins[i] = new InputPort("in"+i, Port.DATAVIEW_BigFile, "");
		}
		outs[0] = new OutputPort("out0", Port.DATAVIEW_String, "");				
	}
	@Override
	public void run() {
		DATAVIEW_BigFile input = (DATAVIEW_BigFile) ins[0].read();
		File input0 = input.getFile();
		long countOfLines = 0;
		try {
			FileReader fr = new FileReader(input0);
			LineNumberReader lnr = new LineNumberReader(fr);
			while (lnr.readLine() != null) {
				countOfLines++;
			}
			lnr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int availableThreads = (Runtime.getRuntime().availableProcessors());
		long partLines = countOfLines / availableThreads;
		List<Thread> threadList = new ArrayList<Thread>();
		for (int i = 0; i < availableThreads; i++) {
			threadList.add(new Thread(new MyRunnable(partLines, input0)));

		}
		for (int i = 0; i < availableThreads; i++) {
			threadList.get(i).start();
		}
		for (int i = 0; i < availableThreads; i++) {
			try {
				threadList.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		StringBuilder output = new StringBuilder();
		try {
			BufferedReader obuffer = new BufferedReader(new FileReader(input0));
			int ncount = 0;
			String line = obuffer.readLine();
			while (line != null) {
				ncount++;

				for (int i = 0; i < 8; i++) {
					output.append(line);
					output.append("\n");
				}

				if (ncount > countOfLines / 4) {
					break;
				}

			}
			obuffer.close();
		} catch (IOException e) {
		}
		outs[0].write(output.toString());
	}

	public static class MyRunnable implements Runnable {
		long partLines;
		File inputFile;

		public MyRunnable(long partLines, File inputFile) {
			this.partLines = partLines;
			this.inputFile = inputFile;
		}

		@Override
		public void run() {
			try {
				BufferedReader obuffer = new BufferedReader(new FileReader(inputFile));
				int ncount = 0;
				String line = obuffer.readLine();
				while (line != null) {
					ncount++;
					calculate(line);
					if (ncount > partLines)
						break;
				}
				obuffer.close();
			} catch (IOException e) {
			}
		}

		private void calculate(String line) {
			String[] parts = line.split(",");
			for (int i = 0; i < parts.length; i++) {
				int element = Integer.parseInt(parts[i]);
				isPrime(element);
			}
		}

		private static boolean isPrime(Integer num) {
			int i, m = 0;
			m = num / 2;
			if (num == 0 || num == 1) {
				return true;
			} else {
				for (i = 2; i <= m; i++) {
					if (num % i == 0) {
						return true;
					}
				}
				return false;
			}
		}

	}

}
